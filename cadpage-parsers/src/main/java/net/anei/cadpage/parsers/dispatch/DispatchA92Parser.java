package net.anei.cadpage.parsers.dispatch;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.anei.cadpage.parsers.HtmlProgramParser;
import net.anei.cadpage.parsers.MsgInfo.Data;
import net.anei.cadpage.parsers.MsgInfo.MsgType;

public class DispatchA92Parser extends HtmlProgramParser {

  public DispatchA92Parser(String defCity, String defState) {
    super(defCity, defState,
          "( Call:CALL! Incident:ID! ( Transfer_Pick-up:ADDRCITYST! Patient_Name:NAME! Transfer_Destination:INFO! Address_Comment:PLACE! Resource:UNIT! INFO/N+ " + 
                                    "| Address:ADDRCITYST! Coordinates:GPS! Address_Comment:PLACE! Resource:UNIT! Response:PRI! Notes:INFO! INFO/N+ " +
                                    ") " +
          "| CALL:CALL! PLACE:PLACE? ADDR:ADDRCITYST! ( APT:APT! CITY:CITY! LAT:GPS1/d! LON:GPS2/d! ID:ID! TIME:DATETIME2! UNIT:UNIT! " + 
                                                     "| ADDR_COMMENT:PLACE? ID:ID? PRI:PRI? DATE:DATETIME1? " + 
                                                     ") INFO:INFO? Additional_Info:INFO?" +
          "| INCIDENT_COMPLETE! Location:ADDRCITYST! Location_Comment:PLACE! Nature:CALL? INFO/N+ )");
  }

  @Override
  protected boolean parseHtmlMsg(String subject, String body, Data data) {
    if (subject.equals("Dispatch Non Emergency")) {
      return super.parseHtmlMsg(subject, body, data);
    } else {
      return parseMsg(subject, body, data);
    }
  }

  private static final Pattern SUBJECT_PTN = Pattern.compile("(New Incident|Update to Incident|Incident Completed|Incident Cancelled) - (\\d+)");
  private static final Pattern MASTER = Pattern.compile("New Incident:\n(.*?) - (.*?) at(?: (.*))?");
  private static final Pattern TRAIL_ST_ZIP_PTN = Pattern.compile("(.*?)(?:, *([A-Z]{2}))?(?: +(\\d{5}))");
  private static final Pattern ADDR_DELIM = Pattern.compile(" *, *");

  @Override
  protected boolean parseMsg(String subject, String body, Data data) {
    String type = "";
    Matcher match = SUBJECT_PTN.matcher(subject);
    if (match.matches()) {
      type = match.group(1);
      data.strCallId = match.group(2);
    }

    if (body.startsWith("CALL:") || body.startsWith("Call:")) {
      String delim = "\n";
      if (body.contains("; PLACE:")) {
        delim = ";";
        if (body.startsWith("Call:")) body = "CALL:" + body.substring(5).replace(" City:", " CITY:");
      }
      String[] flds = body.split(delim);
      if (flds.length >= 3) {
        if (!parseFields(flds, data)) return false;
      }
      else {
        body = body.replace("ID:", " ID:");
        if (!super.parseMsg(body, data)) return false;
      }
    }

    else if (body.startsWith("Incident ")) {
      if (!super.parseFields(body.split("\n"), data)) return false;
    }

    else {
      setFieldList("PRI CALL PLACE ADDR APT CITY ST INFO");

      match = MASTER.matcher(body);
      if (!match.matches()) return false;
      data.strPriority = match.group(1);
      data.strCall = match.group(2);
      String addr = getOptGroup(match.group(3));

      int pt = addr.indexOf(" -  - ");
      if (pt >= 0) {
        data.strSupp = addr.substring(pt+6).trim();
        addr = addr.substring(0,pt).trim();
      }

      pt = addr.indexOf('(');
      if (pt >= 0) {
        data.strPlace = addr.substring(0,pt).trim();
        int pt2 = addr.indexOf(')', pt+1);
        if (pt2 < 0) return false;
        addr = addr.substring(pt+1, pt2).trim();
      }

      String zip = null;
      match = TRAIL_ST_ZIP_PTN.matcher(addr);
      if (match.matches()) {
        addr = match.group(1).trim();
        data.strState = getOptGroup(match.group(2));
        zip = match.group(3);
      }

      String[] parts = ADDR_DELIM.split(addr);
      switch (parts.length) {
      case 1:
        if (data.strPlace.length() > 0) {
          parseAddress(data.strPlace, data);
          data.strCity = parts[0];
          data.strPlace = "";
        } else {
          parseAddress(parts[0], data);
        }
        break;

      case 2:
        parseAddress(parts[0], data);
        data.strCity = parts[1];
        break;

      case 3:
        parseAddress(parts[0], data);
        data.strPlace = append(parts[1], " - ", data.strPlace);
        data.strCity = parts[2];
        break;

      default:
        return false;
      }

      if (data.strCity.length() == 0 && zip != null) data.strCity = zip;
    }

    if (type.equals("Incident Completed")) {
      data.msgType = MsgType.RUN_REPORT;
    } else if (type.equals("Incident Cancelled")) {
      data.msgType = MsgType.RUN_REPORT;
      data.strCall = append("Cancelled", " - ", data.strCall);
    } else if (type.equals("Update to Incident")) {
      data.strCall = append("(UPDATE)", " ", data.strCall);
    }
    return true;
  }

  @Override
  public String getProgram() {
    return "CALL? ID " + super.getProgram();
  }

  @Override
  public Field getField(String name) {
    if (name.equals("CALL")) return new BaseCallField();
    if (name.equals("ADDRCITYST")) return new BaseAddressCityStateField();
    if (name.equals("GPS")) return new BaseGPSField();
    if (name.equals("DATETIME1")) return new BaseDateTime1Field();
    if (name.equals("DATETIME2")) return new DateTimeField("\\d\\d/\\d\\d/\\d{4} +\\d\\d:\\d\\d:\\d\\d");
    if (name.equals("INCIDENT_COMPLETE")) return new SkipField("Incident \\d+ Completed", true);
    return super.getField(name);
  }

  private static final Pattern CODE_CALL_PTN = Pattern.compile("(?:(\\d\\d[A-Z]\\d\\d(?: - [A-Z])?) - |(\\d\\d[A-Z]) ) *(.*)");
  private class BaseCallField extends CallField {
    @Override
    public void parse(String field, Data data) {

      String update = "";
      if (field.startsWith("(UPDATE)")) {
        update = field.substring(0,8);
        field = field.substring(8).trim();
      }

      for (String part : field.split(";")) {
        part = part.trim();
        if (part.isEmpty()) continue;
        Matcher match = CODE_CALL_PTN.matcher(part);
        if (match.matches()) {
          String code = match.group(1);
          if (code != null) data.strCode = code.replace(" - ", "");
          else if (data.strCode.isEmpty()) data.strCode = match.group(2);
          part = match.group(3);
        }
        data.strCall = part;
      }
      data.strCall = append(update, " ", data.strCall);
    }

    @Override
    public String getFieldNames() {
      return "CODE CALL";
    }

  }

  private static final Pattern APT_PFX_PTN = Pattern.compile("^(?:Apt|Rm|Room|Lot)[ .#]*", Pattern.CASE_INSENSITIVE);
  private static final Pattern CITY_ST_ZIP_PTN = Pattern.compile("(?:[A-Za-z ]+, *)?[A-Z]{2}(?: +\\d{5})?");
  private static final Pattern APT_PTN = Pattern.compile("\\d{1,5}[A-Z]?");
  private class BaseAddressCityStateField extends AddressCityStateField {
    @Override
    public void parse(String field, Data data) {
      field = parseCity(field, data);
      
      String city = data.strCity;
      String apt = data.strApt;
      data.strCity = data.strApt = "";

      super.parse(field, data);
      
      data.strApt = append(data.strApt, "-", apt);
      if (data.strCity.isEmpty() && city != null) data.strCity = city;
    }
    
    private String parseCity(String field, Data data) {
      String apt = "";
      String city = null;
      int pt2 = field.lastIndexOf(')');
      if (pt2 < 0) {
        int pt1 = field.lastIndexOf('(');
        if (pt1 >= 0) {
          data.strCity = field.substring(pt1+1).trim();
          field = field.substring(0,pt1).trim();
        }
        return field;
      }
      
      int pt1 = findOpenParen(field, pt2);
      if (pt1 < 0) return field;
    
      city = field.substring(pt1+1, pt2).trim();
      apt = field.substring(pt2+1).trim();
      if (apt.isEmpty()) {
        Matcher match = CITY_ST_ZIP_PTN.matcher(city);
        if (match.matches()) {
          return parseCity(field.substring(0, pt1).trim() + ',' + city, data);
        }
        else if (city.contains(",")) {
          String place = field.substring(0,pt1).trim();
          if (!place.startsWith(city)) {
            data.strPlace = append(data.strPlace, " - ", place);
          }
          return parseCity(city, data);
        }
        else if (city.contains("#") || APT_PTN.matcher(city).matches()) {
          apt = city;
          city = "";
        }
      }
      data.strCity = city;
      if (!apt.isEmpty()) {
        data.strApt = APT_PFX_PTN.matcher(apt).replaceFirst("");
      }
      return field.substring(0, pt1).trim();
    }
    
    private int findOpenParen(String field, int spt) {
      int cnt = 1;
      for (int pt = spt-1; pt > 0; pt--) {
        char chr = field.charAt(pt);
        if (chr == ')') cnt++;
        else if (chr == '(') {
          if (--cnt == 0) return pt;
        }
      }
      return -1;
    }

    @Override
    public String getFieldNames() {
      return "PLACE ADDR CITY ST APT";
    }
  }

  private class BaseGPSField extends GPSField {
    @Override
    public void parse(String field, Data data) {
      field = field.replace(';', ',');
      super.parse(field, data);
    }
  }

  private static final Pattern DATE_TIME1_PTN = Pattern.compile("(\\d\\d?-\\d\\d?-\\d{4}) (\\d\\d?:\\d\\d)");
  private class BaseDateTime1Field extends DateTimeField {
    @Override
    public void parse(String field, Data data) {
      Matcher match = DATE_TIME1_PTN.matcher(field);
      if (!match.matches()) abort();
      data.strDate = match.group(1).replace('-', '/');
      data.strTime = match.group(2);
    }
  }
}
