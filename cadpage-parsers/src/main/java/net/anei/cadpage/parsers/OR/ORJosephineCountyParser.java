package net.anei.cadpage.parsers.OR;

import java.text.SimpleDateFormat;
import java.text.DateFormat;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.anei.cadpage.parsers.FieldProgramParser;
import net.anei.cadpage.parsers.MsgInfo.Data;


public class ORJosephineCountyParser extends FieldProgramParser {

  public ORJosephineCountyParser() {
    super("JOSEPHINE COUNTY", "OR",
          "( ID CALL ( DATETIME ADDRCITY/S6 CITY PLACE X UNIT! END " +
                    "| ADDRCITY/SXa PLACE X/Z? SRC DATETIME! UNIT " +
                    ") " +
          "| DATETIME_CALL CALL2? ADDR_CITY_X/SXa! X2? ( Units:UNIT | UNIT2? ) " +
          "| CALL ADDRCITY/SXa PLACE DATETIME ID! UNIT " +
          ") GPS1? GPS2? Notes:INFO? INFO/S+");
  }

  @Override
  public String getFilter() {
    return "Dispatch@Pacific.com,dispatch@grantspassoregon.gov";
  }

  @Override
  public int getMapFlags() {
    return MAP_FLG_PREFER_GPS;
  }

  private static final Pattern LAT_LON_PTN = Pattern.compile("\\bLAT: *([-+]?[\\d\\.]+),? +LON: *([-+]?[\\d\\.]+)\\b");
  private static final Pattern LAT_LON_PTN2 = Pattern.compile("\\bLAT:([-+]?[\\d\\.]+) LON:([-+]?[\\d\\.]+)\\b");
  private static final Pattern UNITS_PTN = Pattern.compile("Units: +");
  private static final Pattern GPS_PTN = Pattern.compile(":([-+]?\\d{2,3}\\.\\d{6,}):([-+]?\\d{2,3}\\.\\d{6,}) ");
  private static final Pattern DELIM = Pattern.compile("\n|: +|(?<!Units|Notes| LAT| LON):(?!\\d\\d[: ])");
  private static final Pattern DELIM2 = Pattern.compile("\n *| {2,}");

  @Override
  protected boolean parseMsg(String subject, String body, Data data) {
    if (subject.length() == 0 && body.startsWith("!  / ")) {
      subject = "!";
      body = body.substring(5).trim().replace("=\n", "").replace("=0A", "\n");
    }

    if (! subject.trim().equals("!")) return false;

    int pt = body.indexOf("\n\n\n");
    if (pt >= 0) body = body.substring(0, pt).trim();

    body = LAT_LON_PTN.matcher(body).replaceAll("LAT:$1 LON:$2");
    body = UNITS_PTN.matcher(body).replaceFirst("Units:");
    body = GPS_PTN.matcher(body).replaceFirst(": $1: $2: ");

    String[] flds = body.split(";", -1);
    if (flds.length < 8) {
      flds = DELIM.split(body);
      if (flds.length < 3 || flds[0].contains("  ")) flds = DELIM2.split(body);
    }
    if (!parseFields(flds, data)) return false;
    data.strAddress = LAT_LON_PTN2.matcher(data.strAddress).replaceFirst("LAT: $1, LON: $2");
    return true;
  }

  private static final String GPS_PTN_STR = "[-+]?\\d{2,3}\\.\\d{6,}";

  @Override
  public Field getField(String name) {
    if (name.equals("DATETIME")) return new MyDateTimeField();
    if (name.equals("DATETIME_CALL")) return new MyDateTimeCallField();
    if (name.equals("CALL2")) return new MyCall2Field();
    if (name.equals("ADDR_CITY_X")) return new MyAddressCityCrossField();
    if (name.equals("ADDRCITY")) return new MyAddressCityField();
    if (name.equals("PLACE")) return new MyPlaceField();
    if (name.equals("X")) return new MyCrossField();
    if (name.equals("X2")) return new MyCross2Field();
    if (name.equals("SRC")) return new SourceField("\\d{4}(?:, *\\d{4})*");
    if (name.equals("ID")) return new IdField("\\d+|ODF", true);
    if (name.equals("GPS1")) return new GPSField(1, GPS_PTN_STR, true);
    if (name.equals("GPS2")) return new GPSField(2, GPS_PTN_STR, true);
    if (name.equals("UNIT2")) return new UnitField("[A-Z0-9,t]*", true);
    return super.getField(name);
  }

  private static final Pattern DATE_TIME_PTN = Pattern.compile("(\\d\\d?/\\d\\d?/\\d{4}) +(\\d\\d?:\\d\\d:\\d\\d(?: [AP]M)?)");
  private static final DateFormat TIME_FMT = new SimpleDateFormat("hh:mm:ss aa");
  private class MyDateTimeField extends Field {
    @Override
    public boolean canFail() {
      return true;
    }

    @Override
    public boolean checkParse(String field, Data data) {
      Matcher match = DATE_TIME_PTN.matcher(field);
      if (!match.matches()) return false;
      data.strDate = match.group(1);
      String time = match.group(2);
      if (time.endsWith("M")) {
        setTime(TIME_FMT, match.group(2), data);
      } else {
        data.strTime = time;
      }
      return true;
    }

    @Override
    public void parse(String field, Data data) {
      if (!checkParse(field, data)) abort();
    }

    @Override
    public String getFieldNames() {
      return "DATE TIME CALL";
    }
  }

  private static final Pattern DATE_TIME_CALL_PTN = Pattern.compile("(\\d\\d?/\\d\\d?/\\d{4}) +(\\d\\d?:\\d\\d:\\d\\d(?: [AP]M)?)\\b(?: +(.*))?");
  private class MyDateTimeCallField extends Field {
    @Override
    public boolean canFail() {
      return true;
    }

    @Override
    public boolean checkParse(String field, Data data) {
      Matcher match = DATE_TIME_CALL_PTN.matcher(field);
      if (!match.matches()) return false;
      data.strDate = match.group(1);
      String time = match.group(2);
      if (time.endsWith("M")) {
        setTime(TIME_FMT, time, data);
      } else {
        data.strTime = time;
      }
      data.strCall = getOptGroup(match.group(3));
      return true;
    }

    @Override
    public void parse(String field, Data data) {
      if (!checkParse(field, data)) abort();
    }

    @Override
    public String getFieldNames() {
      return "DATE TIME CALL";
    }
  }


  private class MyCall2Field extends CallField {
    @Override
    public boolean canFail() {
      return true;
    }

    @Override
    public boolean checkParse(String field, Data data) {
      if (data.strCall.length() > 0) return false;
      super.parse(field, data);
      return true;
    }
  }

  private class MyAddressCityCrossField extends AddressField {
    @Override
    public void parse(String field, Data data) {
      int pt1 = field.indexOf(',');
      if (pt1 < 0) pt1 = field.length();
      int pt2 = field.indexOf("  ");
      if (pt2 < 0) pt2 = field.length();
      String addr;
      if (pt1 < pt2) {
        addr = field.substring(0,pt1).trim();
        data.strCity = field.substring(pt1+1, pt2).trim();
      } else {
        addr = field.substring(0,pt2);
      }
      String cross = field.substring(pt2).trim();

      int pt = addr.indexOf(" - ");
      if (pt >= 0) {
        data.strPlace = addr.substring(0, pt).trim();
        addr = addr.substring(pt+3).trim();
      }
      super.parse(addr.replace('@', '&'), data);
      if (data.strApt.endsWith(" JCT") || isValidAddress(data.strApt)) {
        data.strCross = data.strApt;
        data.strApt = "";
      }
      if (!cross.equals("No Cross Streets Found")) {
        data.strCross = append(data.strCross, " / ", cross);
      }
    }

    @Override
    public String getFieldNames() {
      return "PLACE? " + super.getFieldNames() + " CITY X";
    }
  }

  private static final Pattern INTERSECT_MARKER = Pattern.compile(" *@ *");
  private class MyAddressCityField extends AddressCityField {
    @Override
    public void parse(String field, Data data) {
      int pt = field.indexOf(" - ");
      if (pt >= 0) {
        data.strPlace = field.substring(0, pt).trim();
        field = field.substring(pt+3).trim();
      }
      field = INTERSECT_MARKER.matcher(field).replaceAll(" & ");
      super.parse(field, data);
    }

    @Override
    public String getFieldNames() {
      return "PLACE? " + super.getFieldNames();
    }
  }

  private class MyPlaceField extends PlaceField {
    @Override
    public void parse(String field, Data data) {
      if (data.strPlace.contains(field)) return;
      else if (field.contains(data.strPlace)) data.strPlace = field;
      else super.parse(field, data);
    }
  }

  private class MyCrossField extends CrossField {
    @Override
    public void parse(String field, Data data) {
      if (field.equals("No Cross Streets Found")) return;
      super.parse(field, data);
    }
  }

  private static final Pattern PLACE_X2_PTN = Pattern.compile("(.*[a-z]\\S*) *(.*)");
  private class MyCross2Field extends CrossField {
    @Override
    public boolean canFail() {
      return true;
    }

    @Override
    public boolean checkParse(String field, Data data) {
      if (data.strCross.length() > 0) return false;
      if (field.endsWith("No Cross Streets Found")) {
        data.strPlace = append(data.strPlace, " - ", field.substring(0, field.length()-22).trim());
        return true;
      }
      if (!field.contains(" / ")) return false;
      Matcher match = PLACE_X2_PTN.matcher(field);
      if (match.matches()) {
        data.strPlace = append(data.strPlace, " - ", match.group(1));
        field = match.group(2);
      }
      parse(field, data);
      return true;
    }

    @Override
    public String getFieldNames() {
      return "PLACE X";
    }
  }


  @Override
  public String adjustMapAddress(String address) {
    // There is a street named JUMP OFF JOE CREED RD that trips the OFF mapping logic
    return address.replace("JUMP OFF", "JUMP0OFF");
  }

  @Override
  public String postAdjustMapAddress(String address) {
    return address.replace("JUMP0OFF", "JUMP OFF");
  }
}
