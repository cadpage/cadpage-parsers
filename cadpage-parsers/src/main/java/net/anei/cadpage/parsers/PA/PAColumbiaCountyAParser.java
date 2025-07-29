package net.anei.cadpage.parsers.PA;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.anei.cadpage.parsers.FieldProgramParser;
import net.anei.cadpage.parsers.MsgInfo.Data;

public class PAColumbiaCountyAParser extends FieldProgramParser {

  public PAColumbiaCountyAParser() {
    super("COLUMBIA COUNTY", "PA",
          "CALL_TYPE:CALL! INCIDENT_LOCATION:ADDRCITY/S6! CROSS_STREETS:X! UNITS:UNIT! INCIDENT_DATE:DATETIME! INCIDENT_NUMBER:ID! GPS:GPS! " +
              "FIRE_GROUND_TALKGROUP:CH! DETAILS:INFO! INFO/N+");
    removeWords("STREET");
  }

  @Override
  public int getMapFlags() {
    return MAP_FLG_PREFER_GPS;
  }

  @Override
  protected boolean parseMsg(String subject, String body, Data data) {
    if (!subject.equals("Notification")) return false;
    return parseFields(body.split("\n"), data);
  }

  @Override
  public Field getField(String name) {
    if (name.equals("ADDRCITY")) return new MyAddressCityField();
    if (name.equals("X")) return new MyCrossField();
    if (name.equals("DATETIME")) return new DateTimeField("\\d\\d?/\\d\\d?/\\d{4} \\d\\d:\\d\\d:\\d\\d", true);
    if (name.equals("GPS")) return new GPSField("https:.*\\?q=(.*)", true);
    return super.getField(name);
  }

  private static final Pattern APT_CITY_PTN = Pattern.compile("[A-Z ]{4,}");
  private static final Pattern APT_PTN = Pattern.compile("\\d+.*|[A-Z]|(?:APT|ROOM|RM) +(.*)", Pattern.CASE_INSENSITIVE);
  private class MyAddressCityField extends AddressCityField {
    @Override
    public void parse(String field, Data data) {
      Parser p = new Parser(field+' ');
      super.parse(p.get(" - ").replace('@', '&'), data);
      data.strApt = stripFieldStart(data.strApt, "Apartment");

      if (data.strCity.isEmpty()) {
        if (data.strApt.startsWith("(") && data.strApt.endsWith(")")) {
          data.strCity = data.strApt.substring(1, data.strApt.length()-1).trim();
          data.strApt = "";
        } else if (APT_CITY_PTN.matcher(data.strApt).matches()) {
          data.strCity = data.strApt;
          data.strApt = "";
        }
      }
      String city = p.get(" - ");
      if (!city.isEmpty()) data.strCity = city;
      data.strPlace = p.get(" - ");
      String name = p.get();
      Matcher match = APT_PTN.matcher(name);
      if (match.matches()) {
        String apt = match.group(1);
        if (apt == null) apt =name;
        if (!apt.equals(data.strApt)) {
          data.strApt = append(data.strApt, "-", apt);
        }
      } else {
        data.strName = name;
      }
      data.strCity = stripFieldEnd(data.strCity, " BORO");
    }

    @Override
    public String getFieldNames() {
      return "ADDR APT CITY PLACE NAME";
    }
  }

  private class MyCrossField extends CrossField {
    @Override
    public void parse(String field, Data data) {
      if (field.equals("No Cross Streets Found")) return;
      super.parse(field, data);
    }
  }
}