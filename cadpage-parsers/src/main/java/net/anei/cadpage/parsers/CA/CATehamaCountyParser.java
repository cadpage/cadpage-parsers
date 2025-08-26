package net.anei.cadpage.parsers.CA;

import java.util.Properties;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.anei.cadpage.parsers.FieldProgramParser;
import net.anei.cadpage.parsers.MsgInfo.Data;
import net.anei.cadpage.parsers.MsgInfo.MsgType;

public class CATehamaCountyParser extends FieldProgramParser {

  public CATehamaCountyParser() {
    super(CITY_CODES, "TEHAMA COUNTY", "CA",
          "( SELECT/RR ID CALL ADDRCITY INFO! INFO/N+ " +
          "| CALL ADDRCITY PLACE X MAP ( LAT:GPS1! LONG:GPS2! | ) ID CH UNIT! INFO/N+ )");
  }

  @Override
  public String getFilter() {
    return "tgucad@fire.ca.gov";
  }

  @Override
  protected boolean parseMsg(String subject, String body, Data data) {
    do {
      if (subject.equals("CAD Page")) break;

      if (body.startsWith("CAD Page / ")) {
        body = body.substring(11).trim();
        break;
      }

      return false;
    } while (false);

    if (body.startsWith("CLOSE: ")) {
      body = body.substring(7).trim();
      data.msgType = MsgType.RUN_REPORT;
      setSelectValue("RR");
    } else {
      setSelectValue("");
    }
    body = body.replace("\nInc# ", ";Inc# ");
    return parseFields(body.split(";"), data);
  }

  @Override
  public Field getField(String name) {
    if (name.equals("ADDRCITY")) return new MyAddressCityField();
    if (name.equals("X")) return new CrossField("Xstreet\\b *(.*)", true);
    if (name.equals("ID")) return new IdField("Inc# (\\d{6})", true);
    if (name.equals("INFO")) return new MyInfoField();
    return super.getField(name);
  }

  private class MyAddressCityField extends AddressCityField {
    @Override
    public void parse(String field, Data data) {
      int pt = field.indexOf('@');
      if (pt >= 0) {
        data.strPlace = field.substring(0,pt).trim();
        field = field.substring(pt+1).trim();
      }
      if (field.endsWith(")")) {
        pt = field.lastIndexOf('(');
        if (pt >= 0) {
          data.strPlace = append(data.strPlace, " - ", field.substring(pt+1,field.length()-1).trim());
          field = field.substring(0,pt).trim();
        }
      }
      super.parse(field, data);
    }

    @Override
    public String getFieldNames() {
      return "PLACE " + super.getFieldNames();
    }
  }

  private static final Pattern INFO_GPS_PTN = Pattern.compile("<a href=\"http://maps.google.com/\\?q=(\\d{2,3}\\.\\d{6},-\\d{2,3}\\.\\d{6})\\b.*");
  private class MyInfoField extends InfoField {
    @Override
    public void parse(String field, Data data) {
      int pt = field.indexOf(" <a ");
      if (pt >= 0) {
        String gps = field.substring(pt+1);
        field = field.substring(0,pt).trim();
        Matcher match = INFO_GPS_PTN.matcher(gps);
        if (match.matches()) setGPSLoc(match.group(1), data);
      }
      super.parse(field, data);
    }

    @Override
    public String getFieldNames() {
      return super.getFieldNames() + " GPS";
    }
  }

  @Override
  public String adjustMapCity(String city) {
    return convertCodes(city, MAP_CITY_TABLE);
  }

  private static final Properties MAP_CITY_TABLE = buildCodeTable(new String[]{
      "LAKE CALIFORNIA",    "COTTONWOOD"
  });

  private static final Properties CITY_CODES = buildCodeTable(new String[]{
      "ANTELOPE",     "RED BLUFF",
      "CNG",          "CORNING",
      "JELLYSFERRY",  "RED BLUFF",
      "LOSM",         "LOS MOLINOS",
      "LKCALIF",      "LAKE CALIFORNIA",
      "NRB",          "RED BLUFF",
      "PROBERTA",     "RED BLUFF",
      "RBCTY",        "RED BLUFF",
      "RTR",          "RANCHO TEHAMA RESERVE",
      "SRB",          "RED BLUFF",
      "WRB",          "RED BLUFF"
  });

}
