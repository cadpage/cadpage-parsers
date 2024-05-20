package net.anei.cadpage.parsers.SC;

import java.util.Properties;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.anei.cadpage.parsers.FieldProgramParser;
import net.anei.cadpage.parsers.MsgInfo.Data;

public class SCBeaufortCountyParser extends FieldProgramParser {

  public SCBeaufortCountyParser() {
    super(CITY_CODES, "BEAUFORT COUNTY", "SC",
          "SKIP SRC CALL ADDRCITY UNIT INFO/N+? ID/Z DATE/Z! END ");
  }

  @Override
  public String getFilter() {
    return "noreply@cisusa.org";
  }

  private static final Pattern EXTRA_BRK_PTN = Pattern.compile("(?:<=Lat )\n|\n(?=Long )");

  @Override
  protected boolean parseMsg(String body, Data data) {
    body =  EXTRA_BRK_PTN.matcher(body).replaceAll(" ");
    return parseFields(body.split("\n"), data);
  }

  @Override
  public Field getField(String name) {
    if (name.equals("ADDRCITY")) return new MyAddressCityField();
    if (name.equals("INFO")) return new MyInfoField();
    if (name.equals("ID")) return new IdField("\\d+", true);
    if (name.equals("DATE")) return new SkipField("\\d\\d?/.*|\\d\\d?", true);
    return super.getField(name);
  }

  private class MyAddressCityField extends AddressField {
    @Override
    public void parse(String field, Data data) {
      Parser p = new Parser(field);
      data.strCity = convertCodes(p.getLastOptional(','), CITY_CODES);
      if (data.strCity.isEmpty()) abort();
      data.strPlace = p.getLastOptional(';');
      parseAddress(p.get(), data);
    }

    @Override
    public String getFieldNames() {
      return "ADDR APT PLACE CITY";
    }
  }

  private static final Pattern PHONE_GPS_PTN = Pattern.compile("(?:Caller Name +(.*?), )?Phone +(\\S*), UNC +\\S+, Lat +(\\S+), Long +(\\S+)");
  private class MyInfoField extends InfoField {

    @Override
    public void parse(String field, Data data) {
      if (field.startsWith("No data.")) return;
      if (field.startsWith("WPH2 data.")) {
        field = field.substring(10).trim();
        Matcher match = PHONE_GPS_PTN.matcher(field);
        if (match.matches()) {
          data.strName = getOptGroup(match.group(1));
          data.strPhone = match.group(2);
          setGPSLoc(match.group(3)+','+match.group(4), data);
        }
        return;
      }
      super.parse(field, data);
    }

    @Override
    public String getFieldNames() {
      return "NAME PHONE GPS " + super.getFieldNames();
    }
  }

  private static final Properties CITY_CODES = buildCodeTable(new String[] {
      "DAF", "DAUFSKI ISLAND"
  });

}
