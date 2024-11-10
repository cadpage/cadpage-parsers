package net.anei.cadpage.parsers.AL;

import java.util.Properties;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.anei.cadpage.parsers.FieldProgramParser;
import net.anei.cadpage.parsers.MsgInfo.Data;

public class ALMadisonCountyBParser extends FieldProgramParser {

  public ALMadisonCountyBParser() {
    this("MADISON COUNTY", "AL");
  }

  ALMadisonCountyBParser(String defCity, String defState) {
    super(CITY_CODES, defCity, defState,
          "LOC:ADDR/S! XSTR1:X! XSTRT2:X! LAT:GPS1! LONG:GPS2! EVT#:ID! TYPE:CALL! TIME:TIME! REMARK:INFO! UNIT:UNIT! END");
    setBreakChar(')');
  }

  @Override
  public String getFilter() {
    return "pageout@madco911.com,pageout@hmc911.com";
  }

  @Override
  public int getMapFlags() {
    return MAP_FLG_PREFER_GPS;
  }

  @Override
  protected boolean parseMsg(String subject, String body, Data data) {

    if (!subject.startsWith("MADCO to ACTIVE911 [")) return false;

    // All leading keywords got merged into the subject, and have to be restored
    int pt = subject.lastIndexOf("]|");
    if (pt < 0) return false;
    StringBuilder sb = new StringBuilder();
    for (String key : subject.substring(pt+2).split("\\|")) {
      sb.append('(');
      sb.append(key);
      sb.append(") ");
    }
    sb.append(body);
    body = sb.toString();

    if (!super.parseMsg(body, data)) return false;

    // Intersections are a bit strange
    if (data.strCity.length() == 0) {
      String city = CITY_CODES.getProperty(data.strAddress);
      if (city != null) {
        data.strCity = city;
        data.strAddress = "";
        parseAddress(data.strCross, data);
        data.strCross = "";
      } if (data.strAddress.isEmpty()) {
        parseAddress(data.strCross, data);
        data.strCross = "";
      }
    }
    return true;
  }

  @Override
  public Field getField(String  name) {
    if (name.equals("TIME")) return new TimeField("\\d\\d:\\d\\d:\\d\\d", true);
    if (name.equals("INFO")) return new MyInfoField();
    if (name.equals("UNIT")) return new MyUnitField();
    if (name.equals("X")) return new MyCrossField();

    return super.getField(name);
  }

  private static final Pattern INFO_GPS_PTN = Pattern.compile("[-+]?\\d{3}\\.\\d{6} +[-+]?\\d{3}\\.\\d{6}");
  private static final Pattern INFO_PHONE_PTN = Pattern.compile("ALT# (\\d{3}-\\d{3}-\\d{4})\\b.*");
  private class MyInfoField extends InfoField {
    @Override
    public void parse(String field, Data data) {
      for (String line : field.split("\n")) {
        line = line.trim();
        Matcher match = INFO_GPS_PTN.matcher(line);
        if (match.matches()) {
          setGPSLoc(line, data);
        } else if ((match = INFO_PHONE_PTN.matcher(line)).matches()) {
          data.strPhone = match.group(1);
        } else {
          data.strSupp = append(data.strSupp, "\n", line);
        }
      }
    }

    @Override
    public String getFieldNames() {
      return "GPS PHONE INFO";
    }
  }

  private class MyUnitField extends UnitField {
    @Override
    public void parse(String field, Data data) {
      field = field.replace('\n', ',');
      super.parse(field, data);
    }
  }

  private class MyCrossField extends CrossField {
    @Override
    public void parse(String field, Data data) {
      field = stripFieldStart(field, "CROSSOVER");
      super.parse(field, data);
    }
  }

  static final Properties CITY_CODES = buildCodeTable(new String[]{
      "ARMR",           "ARDMORE",
      "BRWN",           "BROWNSBORO",
      "GRLY",           "GURLEY",
      "HRST",           "HARVEST",
      "HSV",            "HUNTSVILLE",
      "HZGR",           "HAZEL GREEN",
      "LIME",           "LIMESTONE COUNTY",
      "LIME MAD",       "MADISON",
      "MAD",            "MADISON",
      "MDCO",           "MADISON COUNTY",
      "MRDV",           "MERDIANVILLE",
      "NEWH",           "NEW HOPE",
      "NWMK",           "NEW MARKET",
      "NWMK MDCO",      "NEW MARKET",
      "OXRD",           "OWENS CROSS ROADS",
      "RED",            "REDSTONE ARSENAL",
      "TONY",           "TONEY",
      "TRI",            "TRIANA"
  });

}
