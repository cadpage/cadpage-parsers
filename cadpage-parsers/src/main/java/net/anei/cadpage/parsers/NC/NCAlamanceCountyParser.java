package net.anei.cadpage.parsers.NC;

import java.util.Properties;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.anei.cadpage.parsers.MsgInfo.Data;

import net.anei.cadpage.parsers.dispatch.DispatchOSSIParser;

/**
 * Alamance county, NC
 */
public class NCAlamanceCountyParser extends DispatchOSSIParser {

  public NCAlamanceCountyParser() {
    super(CITY_CODES, "ALAMANCE COUNTY", "NC",
          "ID?: FYI? CALL ADDR! ( APT CITY | CITY | ) X+? ( GPS1 GPS2 | ) INFO/N+");
  }

  @Override
  public String getFilter() {
    return "CAD@alamance-nc.com";
  }

  @Override
  public int getMapFlags() {
    return MAP_FLG_PREFER_GPS;
  }

  @Override
  protected boolean parseMsg(String subject, String body, Data data) {
    boolean good = false;
    if (subject.contains(";")) {
      int pt = body.indexOf(":CAD:");
      if (pt < 0) return false;
      pt += 5;
      body = body.substring(0,pt) + subject + ' ' +  body.substring(pt);
      good = true;
    } else if (body.startsWith("CAD:")) {
      good = true;
    } else {
      body = "CAD:" + body;
      if (subject.equals("Text Message")) good = true;
    }
    if (!super.parseMsg(body, data)) return false;
    return good || data.strCity.length() > 0 || isPositiveId();
  }

  @Override
  public Field getField(String name) {
    if (name.equals("APT")) return new MyAptField();
    if (name.equals("GPS1")) return new MyGPSField(1);
    if (name.equals("GPS2")) return new MyGPSField(2);
    if (name.equals("INFO")) return new MyInfoField();
    return super.getField(name);
  }

  private static final Pattern APT_PTN = Pattern.compile("(?:APT|LOT|UNIT|RM|ROOM|STE|#)[- ]*(.*)");
  private class MyAptField extends AptField {
    @Override
    public void parse(String field, Data data) {
      if (field.startsWith("DIST")) {
        data.strSupp = field;
        return;
      }
      Matcher match = APT_PTN.matcher(field);
      if (match.matches()) field = match.group(1);
      super.parse(field, data);
    }

    @Override
    public String getFieldNames() {
      return "INFO APT";
    }
  }

  private static final Pattern GPS_PTN = Pattern.compile("[-+]?\\d{2,3}\\.\\d{4,}");
  private class MyGPSField extends GPSField {
    public MyGPSField(int type) {
      super(type);
      setPattern(GPS_PTN, false);
    }
  }

  private class MyInfoField extends InfoField {
    @Override
    public void parse(String field, Data data) {
      if (field.startsWith("Radio Channel:")) {
        data.strChannel = field.substring(14).trim();
        return;
      }
      super.parse(field, data);
    }

    @Override
    public String getFieldNames() {
      return "CH " + super.getFieldNames();
    }
  }

  private static final Properties CITY_CODES = buildCodeTable(new String[]{
      "BURL",  "BURLINGTON",
      "ELON",  "ELON",
      "GIB",   "GIBSONVILLE",
      "GRAH",  "GRAHAM",
      "GREE",  "GREEN LEVEL",
      "HAW",   "HAW RIVER",
      "LIB",   "LIBERTY",
      "MEB",   "MEBANE",
      "SNOW",  "SNOW CAMP",
      "SWEP",  "SWEPSONVILLE",

      // Chatham County
      "PIT",   "PITTSBORO",

      // Guilford County
      "WHIT",  "WHITSETT",

      // Orange County
      "ORG",   "ORANGE COUNTY"
  });
}
