package net.anei.cadpage.parsers.dispatch;

import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.anei.cadpage.parsers.FieldProgramParser;
import net.anei.cadpage.parsers.MsgInfo.Data;
import net.anei.cadpage.parsers.MsgInfo.MsgType;
/**
 * Base class for parsing Spillman CAD system alerts
 */
public class DispatchA19Parser extends FieldProgramParser {
  
  private static final Pattern SUBJECT_PTN = Pattern.compile("(?:DISPATCH)?INCIDENT # ([-,A-Z0-9]+)");
  private static final Pattern HASH_DELIM = Pattern.compile("(?<=[A-Z]) ?#(?= )");
  private static final Pattern FIELD_BREAK = Pattern.compile(" (ACTIVE CALL|REPORTED|Type|Zone|Phone):");
  private static final Pattern FIELD_DELIM = Pattern.compile(" *\n+ *");
  
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
    super(defCity, defState,
           "( Incident_#:ID! CAD_Call_ID_#:ID! Type:SKIP/R! Date/Time:TIMEDATE! ( Address:ADDR! Contact:NAME! Contact_Phone:PHONE? | ) Nature:CALL! Nature_Description:INFO! Comments:INFO+ Receiving_and_Responding_Units:SKIP TIMES/N+ " +
           "| INCIDENT:ID? LONG_TERM_CAD:ID? ACTIVE_CALL:ID? PRIORITY:PRI? REPORTED:TIMEDATE? Nature:CALL! Type:SKIP! Address:ADDR! Zone:MAP! City:CITY? SearchAddresss:SKIP? LAT-LON:GPS? Responding_Units:UNIT! Directions:INFO! INFO+ Cross_Streets:X? X/Z+? ( LAT-LON | XY_Coordinates:XYPOS | XCoords:XY_COORD ) Comments:INFO? INFO+ Contact:NAME Phone:PHONE )");
  }
  
  @Override
  protected boolean parseMsg(String subject, String body, Data data) {
    
    Matcher match = SUBJECT_PTN.matcher(subject);
    if (match.matches()) data.strCallId = match.group(1);
    
    times = "";
    body = HASH_DELIM.matcher(body).replaceAll(":");
    body = FIELD_BREAK.matcher(body).replaceAll("\n$1:");
    if (!parseFields(FIELD_DELIM.split(body), data)) return false;
    if (data.msgType == MsgType.RUN_REPORT) data.strSupp = append(times, "\n", data.strSupp);
    return true;
  }
  
  @Override
  public Field getField(String name) {
    if (name.equals("ID")) return new BaseIdField();
    if (name.equals("ADDR")) return new BaseAddressField();
    if (name.equals("INFO")) return new BaseInfoField();
    if (name.equals("X")) return new BaseCrossField();
    if (name.equals("LAT-LON")) return new BaseLatLonField();
    if (name.equals("XYPOS")) return new BaseXYPosField();
    if (name.equals("XY_COORD")) return new BaseXYCoordField();
    if (name.equals("PHONE")) return new BasePhoneField();
    if (name.equals("TIMES")) return new BaseTimesField();
    return super.getField(name);
  }
  
  private class BaseIdField extends IdField {
    @Override
    public void parse(String field, Data data) {
      data.strCallId = append(data.strCallId, "/", field);
    }
  }
  
  private static final Pattern ADDR_APT_PTN = Pattern.compile("(?:APT|RM|SUITE) *(.*)|\\d+[A-Z]?", Pattern.CASE_INSENSITIVE);
  private class BaseAddressField extends AddressField {
    @Override
    public void parse(String field, Data data) {
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
      if (apt != null && place == null) {
      }
      if (place != null) data.strPlace = place;
      super.parse(field, data);
      if (apt != null) data.strApt = append(data.strApt, "-", apt);
    }
    
    private String checkApt(String field) {
      Matcher match = ADDR_APT_PTN.matcher(field);
      if (!match.matches()) return null;
      String apt = match.group(1);
      if (apt == null) apt = field;
      return apt;
    }
    
    @Override
    public String getFieldNames() {
      return "PLACE " + super.getFieldNames();
    }
  }

  private static final Pattern DATE_TIME_OPER_PTN = Pattern.compile("(\\d\\d:\\d\\d:\\d\\d) (\\d\\d/\\d\\d/\\d{4}) - .*");
  private static final Pattern PHONE_GPS_PTN = Pattern.compile("CALLBACK=([-()\\d]+) LAT=([-+]\\d+\\.\\d+) LON=([-+]\\d+\\.\\d+) UNC=\\d+");
  private static final Pattern INFO_JUNK_PTN = Pattern.compile("ProQA Fire.*");
  private class BaseInfoField extends InfoField {
    @Override
    public void parse(String field, Data data) {
      Matcher match = DATE_TIME_OPER_PTN.matcher(field);
      if (match.matches()) {
        data.strTime = match.group(1);
        data.strDate = match.group(2);
        return;
      }
      
      match = PHONE_GPS_PTN.matcher(field);
      if (match.matches()) {
        data.strPhone = match.group(1);
        setGPSLoc(match.group(2) + "," + match.group(3), data);
        return;
      }
      
      if (INFO_JUNK_PTN.matcher(field).matches()) return;
      
      super.parse(field, data);
    }
    
    @Override
    public String getFieldNames() {
      return "INFO TIME DATE GPS";
    }
  }
  
  private class BaseCrossField extends CrossField {
    @Override
    public void parse(String field, Data data) {
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
      if (!LEGIT_PHONE_PTN.matcher(field).matches()) return;
      super.parse(field, data);
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
