package net.anei.cadpage.parsers.dispatch;

import java.util.Locale;
import java.util.Properties;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.anei.cadpage.parsers.FieldProgramParser;
import net.anei.cadpage.parsers.MsgInfo.Data;
import net.anei.cadpage.parsers.MsgInfo.MsgType;
/**
 * Base class for parsing Spillman CAD system alerts
 */
public class DispatchA19Parser extends FieldProgramParser {

  private boolean refLatLong = false;
  private double refLat;
  private double refLong;
  private String times;

  public DispatchA19Parser(String defCity, String defState, double refLat, double refLong) {
    this(defCity, defState);
    refLatLong = true;
    this.refLat = refLat;
    this.refLong = refLong;
  }

  public DispatchA19Parser(String defCity, String defState) {
    this(null, defCity, defState);
  }

  public DispatchA19Parser(Properties cityCodes, String defCity, String defState) {
    super(cityCodes, defCity, defState,
          "( CALL ADDR/Z Call_Narrative%EMPTY/R INFO/N+ CAD_Call_ID_#:ID! END " +
          "| Incident_#:ID! CAD_Call_ID_#:ID! Type:SKIP/R! Date/Time:TIMEDATE! ( Address:ADDR! EMPTY? City:CITY? Contact:NAME? Contact_Address:SKIP? Contact_Phone:PHONE? | ) Nature:CALL! Nature_Description:INFO/N? Determinant:CODE Determinant_Desc:CALL/SDS Complaint_Name:NAME Complainant_Phone:PHONE Comments:INFO/N INFO/N+? TIME_MARK TIMES/N+ " +
          "| NATURE:CALL! CASE_NUM:ID! CALL_TYPE:SKIP! DETERM_CODE:CODE! DETERM_DESC:CALL/SDS! DETAILS:INFO! INFO/N+ CITY:CITY! ZONE:MAP! ADDRESS:ADDR! DIRECTIONS:FINFO! CROSS_STREETS:X! COORDINATES:GPS! CONTACT:NAME! PHONE_NUM:PHONE! RESP_UNITS:UNIT! " +
          "| INCIDENT:ID LONG_TERM_CAD:ID ACTIVE_CALL:ID PRIORITY:PRI REPORTED:TIMEDATE ( Determinants/Desc:CODE | Determinant:CODE Desc:CALL | ) Nature:CALL? Type:SKIP BADTIME? ( Address:ADDR! Zone:MAP? | Zone:MAP! Address:ADDR! ) City:CITY? Contact:NAME Phone:PHONE ( Nature:CALL! Determinant:CODE! Desc:CALL! | ) SearchAddresss:SKIP? LAT-LON:GPS? Reported:TIMEDATE? Responding_Units:UNIT Directions:INFO/N? INFO/N+ Cross_Streets:X? X/Z+? ( LAT-LON | XY_Coordinates:XYPOS | XCoords:XY_COORD ) Comments:INFO/N? INFO/N+ Contact:NAME Phone:PHONE )");
  }

  private static final Pattern SUBJECT_PTN = Pattern.compile("(?:DISPATCH)?INCIDENT # ([-,A-Z0-9]+)");
  private static final Pattern HASH_DELIM = Pattern.compile("(?<=[A-Z]) ?#(?= )");
  private static final Pattern FIELD_BREAK = Pattern.compile(" (City|ACTIVE CALL|Desc|REPORTED|Type|Zone|(?<=\n ?Contact:.{10,70} )Phone):");
  private static final Pattern FIELD_DELIM = Pattern.compile(" *\n+ *");

  @Override
  protected boolean parseMsg(String subject, String body, Data data) {

    Matcher match = SUBJECT_PTN.matcher(subject);
    if (match.matches()) data.strCallId = match.group(1);

    if (body.startsWith("TIME - ")) {
      int pt = body.indexOf('\n');
      if (pt < 0) return false;
      body = body.substring(pt+1).trim();
    }



    times = "";
    body = HASH_DELIM.matcher(body).replaceAll(":");
    body = FIELD_BREAK.matcher(body).replaceAll("\n$1:");
    body = body.replace(",\nUNC:", ", UNC:");
    body = body.replace(", UNC:\n", ", UNC: ");
    body = body.replace(",\nLat:", ", Lat:");
    body = body.replace(", Lat:\n", ", Lat: ");
    body = body.replace(",\nLong:", ", Long:");
    body = body.replace(", Long:\n", ", Long: ");
    if (!parseFields(FIELD_DELIM.split(body), data)) return false;
    if (data.msgType == MsgType.RUN_REPORT) data.strSupp = append(times, "\n", data.strSupp);
    return !data.strCall.isEmpty();
  }

  @Override
  public Field getField(String name) {
    if (name.equals("ID")) return new BaseIdField();
    if (name.equals("TIMEDATE")) return new BaseTimeDateField();
    if (name.equals("ADDR")) return new BaseAddressField();
    if (name.equals("CALL")) return new BaseCallField();
    if (name.equals("INFO")) return new BaseInfoField();
    if (name.equals("FINFO")) return new BaseFrontInfoField();
    if (name.equals("X")) return new BaseCrossField();
    if (name.equals("LAT-LON")) return new BaseLatLonField();
    if (name.equals("XYPOS")) return new BaseXYPosField();
    if (name.equals("XY_COORD")) return new BaseXYCoordField();
    if (name.equals("PHONE")) return new BasePhoneField();
    if (name.equals("TIME_MARK")) return new SkipField(".*Responding Units:", true);
    if (name.equals("TIMES")) return new BaseTimesField();
    if (name.equals("BADTIME")) return new SkipField("TIME - .*", true);
    return super.getField(name);
  }

  private class BaseIdField extends IdField {
    @Override
    public void parse(String field, Data data) {
      data.strCallId = append(data.strCallId, "/", field);
    }
  }

  private class BaseTimeDateField extends TimeDateField {
    @Override
    public void parse(String field, Data data) {
      if (field.length() == 0) return;
      super.parse(field, data);
    }
  }

  private static final Pattern ADDR_APT_PTN = Pattern.compile("(?:APT|LOT|RM|ROOM|SUITE)[: ]*(.*)|\\d+[A-Z]?", Pattern.CASE_INSENSITIVE);
  private static final Pattern ADDR_CITY_ST_PTN = Pattern.compile("(.*)(?:, +| {3,})@?([ A-Z]*), *@?([A-Z]{2})");
  private static final Pattern ADDR_CITY_ZIP_PTN = Pattern.compile("(.*) - ([ A-Z]+) - \\d{5}");
  private class BaseAddressField extends AddressField {
    @Override
    public void parse(String field, Data data) {

      // Reverse any accidently hash -> colon transformations
      field = field.replace(": ", " # ");

      Matcher match = ADDR_CITY_ST_PTN.matcher(field);
      if (match.matches()) {
        field = match.group(1).trim();
        data.strCity = match.group(2).trim();
        data.strState = match.group(3);
      }
      else if ((match = ADDR_CITY_ZIP_PTN.matcher(field)).matches()) {
        field = match.group(1).trim();
        data.strCity = match.group(2).trim();
      }

      String apt = null;
      String place = null;
      int pt = field.lastIndexOf(';');
      if (pt >= 0) {
        apt = field.substring(pt+1).trim();
        field = field.substring(0,pt);
        pt = field.lastIndexOf(';');
        if (pt >= 0) {
          place = field.substring(pt+1).trim();
          field = field.substring(0,pt).trim();
        }
      }
      if (apt != null) {
        if (place != null) {
          String tmp = checkApt(place);
          if (tmp != null) {
            place = apt;
            apt = tmp;
          }
        } else {
          String tmp = checkApt(apt);
          if (tmp != null) {
            apt = tmp;
          } else {
            place = apt;
            apt = "";
          }
        }
      }
      if (place == null) {
        pt = field.indexOf(" - ");
        if (pt >= 0) {
          place = field.substring(pt+3).trim();
          field = field.substring(0,pt).trim();
        }
      }
      if (place != null) data.strPlace = place;

      super.parse(field, data);
      if (apt != null && !apt.equals(data.strApt)) data.strApt = append(data.strApt, "-", apt);
    }

    private String checkApt(String field) {
      Matcher match = ADDR_APT_PTN.matcher(field);
      if (match.matches()) {
        String apt = match.group(1);
        if (apt == null) apt = field;
        return apt;
      }
      if (field.startsWith("#")) {
        field = field.substring(1).trim();
        if (!field.contains(" ")) return field;
      }
      return null;
    }

    @Override
    public String getFieldNames() {
      return "ADDR PLACE APT CITY ST";
    }
  }

  private class BaseCallField extends CallField {
    @Override
    public void parse(String field, Data data) {
      data.strCall = append(field, " - ", data.strCall);
    }
  }

  private static final Pattern TIME_DATE_OPER_PTN = Pattern.compile("(\\d\\d:\\d\\d:\\d\\d) (\\d\\d/\\d\\d/\\d{4}) - .*");
  private static final Pattern DATE_TIME_OPER_PTN = Pattern.compile("(\\d\\d)/(\\d\\d)/(\\d\\d) (\\d\\d:\\d\\d:\\d\\d) .+(?::|From:.*)");
  private static final Pattern PHONE_GPS1_PTN = Pattern.compile("CALLBACK=([-()\\d]+) LAT=([-+]\\d+\\.\\d+) LON=([-+]\\d+\\.\\d+) UNC=\\d+");
  private static final Pattern PHONE_GPS2_PTN = Pattern.compile("[A-Za-z0-9]{3,4} data\\. (?:Caller Name:.*?, )?Phone: (?:\\(   \\) +- +|(.+?)), UNC: \\d+, Lat: (\\S+), Long: (\\S+)(?:,.*)?");
  private static final Pattern INFO_JUNK_PTN = Pattern.compile("ProQA Fire.*|[A-Za-z0-9 ]+:");
  private class BaseInfoField extends InfoField {
    @Override
    public void parse(String field, Data data) {
      Matcher match = TIME_DATE_OPER_PTN.matcher(field);
      if (match.matches()) {
        data.strTime = match.group(1);
        data.strDate = match.group(2);
        return;
      }

      match = DATE_TIME_OPER_PTN.matcher(field);
      if (match.matches()) {
        data.strDate = match.group(2)+'/'+match.group(1)+'/'+match.group(3);
        data.strTime = match.group(4);
        return;
      }

      match = PHONE_GPS1_PTN.matcher(field);
      if (match.matches()) {
        data.strPhone = match.group(1);
        if (data.strGPSLoc.isEmpty()) setGPSLoc(match.group(2) + "," + match.group(3), data);
        return;
      }

      match = PHONE_GPS2_PTN.matcher(field);
      if (match.matches()) {
        data.strPhone = getOptGroup(match.group(1));
        if (data.strGPSLoc.isEmpty()) setGPSLoc(match.group(2) + "," + match.group(3), data);
        return;
      }

      if (INFO_JUNK_PTN.matcher(field).matches()) return;

      super.parse(field, data);
    }

    @Override
    public String getFieldNames() {
      return "INFO TIME DATE PHONE? GPS";
    }
  }

  private class BaseFrontInfoField extends InfoField {
    @Override
    public void parse(String field, Data data) {
      data.strSupp = append(field, "\n", data.strSupp);
    }
  }

  private class BaseCrossField extends CrossField {
    @Override
    public void parse(String field, Data data) {
      Matcher match = LAT_LON_PTN.matcher(field);
      if (match.matches()) {
        if (data.strGPSLoc.isEmpty()) {
          setGPSLoc(match.group(1)+','+match.group(2), data);
        }
      } else {
        int pt = field.indexOf(":");
        if (pt >= 0) field = field.substring(pt+1).trim();
        for (String street : field.split("&")) {
          street = street.trim();
          if (street.equals("<not found>")) continue;
          if (!data.strAddress.contains(street) && !data.strCross.contains(street)) {
            super.parse(street, data);
          }
        }
      }
    }

    @Override
    public String getFieldNames() {
      return "X GPS?";
    }
  }

  private static final Pattern LAT_LON_PTN = Pattern.compile("Lat= *(\\S*) +Lon= *(\\S*)");
  private class BaseLatLonField extends GPSField {

    @Override
    public boolean canFail() {
      return true;
    }

    @Override
    public boolean checkParse(String field, Data data) {
      Matcher match = LAT_LON_PTN.matcher(field);
      if (!match.matches()) return false;
      setGPSLoc(match.group(1)+','+match.group(2), data);
      return true;
    }

    public void parse(String field, Data data) {
      if (!checkParse(field, data)) abort();
    }
  }

  // Internal spillman X-Y Coordinates
  private static final Pattern XYPOS_PTN = Pattern.compile("xpos: *([-+]?\\d+) +ypos: *([-+]?\\d+)");
  private class BaseXYPosField extends GPSField {
    @Override
    public void parse(String field, Data data) {
      if (!refLatLong) return;
      if (data.strGPSLoc.length() > 0) return;
      Matcher match = XYPOS_PTN.matcher(field);
      if (!match.matches()) abort();
      double lat = refLat + Integer.parseInt(match.group(2))/100000.;
      double lon = refLong + Integer.parseInt(match.group(1))/100000.;
      data.strGPSLoc = String.format(Locale.US, "%+8.6f,%+8.6f", lat, lon);
    }
  }

  private static final Pattern XYCOORD_PTN = Pattern.compile("([-+]?\\d{7,}|) +YCoords: *([-+]?\\d{7,}|)");
  private class BaseXYCoordField extends GPSField {
    @Override
    public void parse(String field, Data data) {
      if (data.strGPSLoc.length() > 0) return;
      Matcher match = XYCOORD_PTN.matcher(field);
      if (!match.matches()) abort();
      String lat = match.group(2);
      String lon = match.group(1);
      if (lat.length() == 0 || lon.length() == 0) return;
      setGPSLoc(normalize(lat) + ',' + normalize(lon), data);
    }

    private String normalize(String field) {
      int pt = 0;
      char chr = field.charAt(pt);
      if (chr == '+' || chr == '-') {
        pt++;
        chr = field.charAt(pt);
      }
      if (chr == '0' || chr == '1') pt++;
      pt += 2;
      return field.substring(0,pt) + '.' + field.substring(pt);
    }
  }

  // phone field has to contain a digit
  private static final Pattern LEGIT_PHONE_PTN = Pattern.compile(".*\\d.*");
  private class BasePhoneField extends PhoneField {
    @Override
    public void parse(String field, Data data) {

      int pt = field.indexOf(',');
      if (pt >= 0) {
        if (data.strGPSLoc.isEmpty()) {
          String gps = field.substring(pt+1).trim();
          setGPSLoc(gps, data);
        }
        field = field.substring(0,pt).trim();
      }

      if (!LEGIT_PHONE_PTN.matcher(field).matches()) return;
      super.parse(field, data);
    }

    @Override
    public String getFieldNames() {
      return super.getFieldNames() + " GPS?";
    }
  }

  private static final Pattern TIMES_PTN = Pattern.compile("(\\S+ +[A-Z]+ +\\d\\d:\\d\\d:\\d\\d +\\d\\d/\\d\\d/\\d\\d)\\b *.*");
  private class BaseTimesField extends InfoField {
    @Override
    public void parse(String field, Data data) {
      Matcher match = TIMES_PTN.matcher(field);
      if (match.matches()) {
        times = append(times, "\n", match.group(1));
      }
    }
  }
}
