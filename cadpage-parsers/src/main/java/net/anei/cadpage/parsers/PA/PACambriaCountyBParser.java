package net.anei.cadpage.parsers.PA;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.anei.cadpage.parsers.CodeTable;
import net.anei.cadpage.parsers.FieldProgramParser;
import net.anei.cadpage.parsers.MsgInfo.Data;
import net.anei.cadpage.parsers.StandardCodeTable;

public class PACambriaCountyBParser extends FieldProgramParser {

  public PACambriaCountyBParser() {
    super("CAMBRIA COUNTY", "PA",
          "DATE:DATE! TIME:TIME! NATURE:CODE_CALL! ADDRESS:ADDRCITY! BLDG:PLACE! CROSS:X! COORDS:GPS! TAC:CH! STATIONS:UNIT! FIRE_AREA:MAP!");
  }

  @Override
  public String getFilter() {
    return "noreply@cambria911.com";
  }

  @Override
  public int getMapFlags() {
    return MAP_FLG_PREFER_GPS;
  }

  @Override
  protected boolean parseMsg(String body, Data data) {
    return parseFields(body.split("\n"), data);
  }

  @Override
  public Field getField(String name) {
    if (name.equals("DATE")) return new DateField("\\d\\d/\\d\\d/\\d{4}", true);
    if (name.equals("TIME")) return new TimeField("\\d\\d:\\d\\d:\\d\\d", true);
    if (name.equals("CODE_CALL")) return new MyCodeCallField();
    if (name.equals("ADDRCITY")) return new MyAddressCityField();
    if (name.equals("X")) return new MyCrossField();
    return super.getField(name);
  }

  private static final CodeTable CALL_CODES = new StandardCodeTable();
  private static final Pattern CODE_CALL_PTN = Pattern.compile("([A-Z0-9]+)- *(.*)");
  private class MyCodeCallField extends Field {

    @Override
    public void parse(String field, Data data) {
      Matcher match = CODE_CALL_PTN.matcher(field);
      if (!match.matches()) abort();
      data.strCode = match.group(1);
      String call = CALL_CODES.getCodeDescription(data.strCode);
      if (call != null) {
        data.strCall = call;
      } else {
        data.strCall = match.group(2);
      }
    }

    @Override
    public String getFieldNames() {
      return "CODE CALL";
    }
  }

  private static final Pattern ADDR_APT_CITY_PTN = Pattern.compile("(.*?) *\\bAPT +(.*?) +-(.*)");
  private static final Pattern CITY_COUNTY_PTN = Pattern.compile("(.*?)[- ]+[A-Z][a-z]+ Co(?:unty?)?", Pattern.CASE_INSENSITIVE);
  private class MyAddressCityField extends AddressCityField {
    @Override
    public void parse(String field, Data data) {
      Matcher match = ADDR_APT_CITY_PTN.matcher(field);
      if (!match.matches()) abort();
      String addr = match.group(1).trim();
      String apt = match.group(2).trim();
      String city = match.group(3).trim();
      if (city.isEmpty()) {
        int pt = addr.indexOf("- ");
        if (pt >= 0) {
          city = addr.substring(pt+2).trim();
          addr = addr.substring(0,pt).trim();
        }
      }

      parseAddress(addr, data);
      data.strApt = append(data.strApt, "-", apt);
      match = CITY_COUNTY_PTN.matcher(city);
      if (match.matches()) city = match.group(1).trim();
      data.strCity = stripFieldEnd(city, " Boro");
    }
  }

  private class MyCrossField extends CrossField {
    @Override
    public void parse(String field, Data data) {
      if (field.contains("&") || field.contains(" - ")) {
        super.parse(field, data);
      } else {
        parseAddress(StartType.START_ADDR, FLAG_ONLY_CROSS | FLAG_IMPLIED_INTERSECT | FLAG_ANCHOR_END, field, data);
      }
    }
  }
}
