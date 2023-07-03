package net.anei.cadpage.parsers.IN;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.anei.cadpage.parsers.FieldProgramParser;
import net.anei.cadpage.parsers.MsgInfo.Data;

public class INFountainCountyBParser extends FieldProgramParser {

  public INFountainCountyBParser() {
    super("FOUNTAIN COUNTY", "IN",
          "CALL:CODE_CALL! ( ID:ID! PLACE:PLACE? ADDR CITY_ST_ZIP! | ADDR:ADDR! CITY:CITY! ID:ID! ) DATE:DATE! TIME:TIME! MAP:MAP! UNIT:UNIT! INFO:INFO! INFO/N+ DIRECTIONS:LINFO! WARNINGS:ALERT!");
  }

  @Override
  public String getFilter() {
    return "@fwrdc.net";
  }

  @Override
  protected boolean parseMsg(String body, Data data) {
    int pt = body.indexOf(" - CALL:");
    if (pt < 0) return false;
    body = body.substring(pt+3);

    if (body.contains("\nID:")) {
      return parseFields(body.split("\n"), data);
    } else {
      return super.parseMsg(body, data);
    }
  }

  @Override
  public Field getField(String name) {
    if (name.equals("CODE_CALL")) return new MyCodeCallField();
    if (name.equals("ADDR")) return new MyAddressField();
    if (name.equals("CITY_ST_ZIP")) return new MyCityStateZipField();
    if (name.equals("DATE")) return new DateField("\\d\\d/\\d\\d/\\d{4}", true);
    if (name.equals("TIME")) return new TimeField("\\d\\d:\\d\\d:\\d\\d", true);
    return super.getField(name);
  }

  private static final Pattern CODE_CALL_PTN = Pattern.compile("(\\d{4}) +(.*)");
  private class MyCodeCallField extends Field {

    @Override
    public void parse(String field, Data data) {
      Matcher match = CODE_CALL_PTN.matcher(field);
      if (!match.matches()) abort();
      data.strCode = match.group(1);
      data.strCall = match.group(2);
    }

    @Override
    public String getFieldNames() {
      return "CODE CALL";
    }
  }

  private static final Pattern ADDR_APT_PTN = Pattern.compile("(.*?)[, ]+Apt/Unit #?(?:APT +)?(.*)", Pattern.CASE_INSENSITIVE);
  private class MyAddressField extends AddressField {
    @Override
    public void parse(String field, Data data) {
      String apt = "";
      Matcher match = ADDR_APT_PTN.matcher(field);
      if (match.matches()) {
        field = match.group(1);
        apt = match.group(2);
      }
      super.parse(field, data);
      data.strApt = append(data.strApt, "-", apt);

    }
  }

  private static final Pattern CITY_ST_ZIP_PTN = Pattern.compile("(.*), ([A-Z]{2})(?: (\\d{5}))?");
  private class MyCityStateZipField extends Field {
    @Override
    public void parse(String field, Data data) {
      Matcher match = CITY_ST_ZIP_PTN.matcher(field);
      if (match.matches()) {
        field = match.group(1).trim();
        data.strState = match.group(2);
        if (field.isEmpty()) field = getOptGroup(match.group(3));
      }
      if (field.equals("A")) field = "ATTICA";
      data.strCity = field;
    }

    @Override
    public String getFieldNames() {
      return "CITY ST";
    }
  }
}
