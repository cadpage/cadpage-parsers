package net.anei.cadpage.parsers.dispatch;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.anei.cadpage.parsers.FieldProgramParser;
import net.anei.cadpage.parsers.MsgInfo.Data;
import net.anei.cadpage.parsers.MsgInfo.MsgType;

public class DispatchA27Parser extends FieldProgramParser {

  private Pattern unitPtn;


  private enum UnitMode {UNIT, TIMES, GPS, INFO};
  private UnitMode unitMode;
  private String gps = null;
  private String times;

  public DispatchA27Parser(String defCity, String defState) {
    this(null, defCity, defState, null);
  }

  public DispatchA27Parser(String defCity, String defState, String unitPtn) {
    this(null, defCity, defState, unitPtn);
  }

  public DispatchA27Parser(String[] cityList, String defCity, String defState) {
    this(cityList, defCity, defState, null);
  }

  public DispatchA27Parser(String[] cityList, String defCity, String defState, String unitPtn) {
    super(cityList, defCity, defState,
          "( SELECT/NEW ADDRCITY/SC Time_reported:DATETIME! Unit(s)_responded:UNIT! " +
          "| SELECT/NEW3 SRC_ID3 CALL ADDR CITY_ST BOX3 INFOX3 UNIT3 GPS1_3 GPS2_3 CODE3! TIME_LOG " +
          "| ADDRCITY/SC DUP? EMPTY+? ( MASH | SRC! Case_Nr:ID? TIMES+? ) Unit(s)_responded:UNIT2? UNIT2+ " +
          ")");
    this.unitPtn = unitPtn == null ? null : Pattern.compile(unitPtn);
  }

  private static final Pattern MARKER = Pattern.compile("Notification from (?:CIS )?[-A-Za-z0-9 ']+:");
  private static final Pattern MARKER2 = Pattern.compile("[A-Z]{3,4} \\d{12}; ");
  private static final Pattern DELIM_PTN = Pattern.compile("\n{2}");

  @Override
  public boolean parseMsg(String subject, String body, Data data) {

    Matcher match = MARKER.matcher(body);
    if (!match.lookingAt()) {
      match = MARKER2.matcher(body);
      if (!match.lookingAt()) return false;
      setSelectValue("NEW3");
      int pt = body.indexOf("\n\n");
      if (pt >= 0) body = body.substring(0, pt).trim();
      body = body.replace(";; ", "; ; ");
      return parseFields(body.split("; "), data);
    }
    body = body.substring(match.end()).trim();
    unitMode = UnitMode.UNIT;
    times = "";

    int pt = body.indexOf("\n<img src=");
    if (pt >= 0) body = body.substring(0,pt).trim();

    body = body.replace("\n\nIncident Nr:", "\nIncident Nr:");
    String[] flds = DELIM_PTN.split(body);
    if (flds.length > 1) {
      setSelectValue("OLD");
      if (!super.parseFields(flds, data)) return false;
    } else {
      setSelectValue("NEW");
      if (!super.parseFields(body.split("\n"), data)) return false;
    }

    if (data.strTime.length() == 0) return false;
    if (data.msgType == MsgType.RUN_REPORT) {
      data.strSupp = append(times, "\n", data.strSupp);
    }

    Parser p = new Parser(subject);
    p.get(';');
    p.get(';');
    data.strSupp = append(data.strSupp, "\n", p.get());

    return true;
  }

  @Override
  public String getProgram() {
    return super.getProgram() + " INFO";
  }

  @Override
  public Field getField(String name) {
      if (name.equals("ADDRCITY")) return new BaseAddressCityField();
      if (name.equals("DUP")) return new BaseDuplField();
      if (name.equals("DATETIME")) return new BaseDateTimeField();
      if (name.equals("MASH")) return new BaseMashField();
      if (name.equals("SRC")) return new BaseSrcField();
      if (name.equals("ID")) return new BaseIdField();
      if (name.equals("TIMES")) return new BaseTimesField();
      if (name.equals("UNIT2")) return new BaseUnit2Field();

      if (name.equals("SRC_ID3")) return new BaseSourceId3Field();
      if (name.equals("CITY_ST")) return new BaseCityStateField();
      if (name.equals("BOX3")) return new BoxField("Alrm Box +(.*)|", true);
      if (name.equals("INFOX3")) return new BaseInfoX3Field();
      if (name.equals("UNIT3")) return new UnitField("Units +(.*)|", true);
      if (name.equals("GPS1_3")) return new GPSField(1, "Lat +(.*)|", true);
      if (name.equals("GPS2_3")) return new GPSField(2, "Lon +(.*)|", true);
      if (name.equals("CODE3")) return new CodeField("Code +(.*)|", true);
      if (name.equals("TIME")) return new TimeField("\\d\\d:\\d\\d:\\d\\d", true);
      if (name.equals("TIME_LOG")) return new BaseTimeLogField();
    return super.getField(name);
  }

  private static final Pattern PTN_FULL_ADDR =
      Pattern.compile("(.*?)(?:, *(\\d{5}|0|))?(?:, *((?:[-+]?\\d+\\.\\d{4,}|0), *(?:[-+]?\\d+\\.\\d{4,}|0)))?(?: +(\\d{4}-\\d{6}|\\d{7}))?");
  private static final Pattern ADDR_APT_PTN = Pattern.compile("(.*?), *([A-Z]?\\d{1,4}[A-Z]?|[A-Z])", Pattern.CASE_INSENSITIVE);
  protected class BaseAddressCityField extends AddressCityStateField {

    @Override
    public void parse(String field, Data data) {
      String zip = null;
      field = field.replace(" -, ", ", -");
      Matcher m = PTN_FULL_ADDR.matcher(field);   // This will match address, city, and zip
      if(m.matches()) {                           // If we have a match
        field = m.group(1).trim();                // Remove the zipcode
        zip = m.group(2);
        setGPSLoc(getOptGroup(m.group(3)), data);
        data.strCallId = getOptGroup(m.group(4));
      }

      field = stripFieldEnd(field, ",");
      if (field.endsWith(")")) {
        int pt = field.indexOf('(');
        if (pt < 0) abort();
        data.strPlace = field.substring(pt+1, field.length()-1).trim();
        field = field.substring(0,pt).trim();
      }

      int x = field.indexOf("/unincorp");
      if(x >= 0) {
        field = field.substring(0, x) + field.substring(x+9);
      }

      field = field.replace('@',  '&').replace(" at ,", " at ");
      String apt = null;
      m = ADDR_APT_PTN.matcher(field);
      if (m.matches()) {
        field = m.group(1).trim();
        apt = m.group(2);
      }
      super.parse(field, data);

      if (apt == null) {
        int pt = data.strAddress.lastIndexOf(',');
        if (pt >= 0) {
          apt = data.strAddress.substring(pt+1).trim();
          data.strAddress = data.strAddress.substring(0,pt).trim();
        }
      }
      if (apt != null) data.strApt = append(data.strApt, "-", apt);

      if (data.strCity.isEmpty() && zip != null && !zip.equals("0")) data.strCity = zip;
    }

    @Override
    public String getFieldNames() {
      return super.getFieldNames() + " PLACE GPS ID?";
    }
  }

  private class BaseDuplField extends SkipField {

    @Override
    public boolean canFail() {
      return true;
    }

    @Override
    public boolean checkParse(String field, Data data) {
      return field.equals(getRelativeField(-1));
    }
  }

  private class BaseDateTimeField extends DateTimeField {
    @Override
    public void parse(String field, Data data) {
      if (!parseDateTime(field, data)) abort();
    }
  }

  private class BaseMashField extends Field {
    @Override
    public boolean canFail() {
      return true;
    }

    @Override
    public boolean checkParse(String field, Data data) {
      if (!field.startsWith("Agency:")) return false;
      parse(field, data);
      return true;
    }

    @Override
    public void parse(String field, Data data) {
      Parser p = new Parser(field);
      String agency;
      if ((agency = getValue(p, "Agency:")) == null) abort();
      int pt = agency.indexOf('\t');
      if (pt >= 0) agency = agency.substring(0,pt).trim();
      data.strSource = agency;
      if ((data.strCallId = getValue(p, "Incident Nr:")) == null) abort();
      String line = p.getLine();
      if (getValue(line, "Case Nr:") != null) line = p.getLine();
      if (getValue(line, "Location:") == null) abort();
      line = p.getLine();
      String gps1 = getValue(line, "Latitude:");
      if (gps1 != null) {
        line = p.getLine();
        String gps2 = getValue(line,  "Longitude:");
        if (gps2 == null) abort();
        setGPSLoc(gps1+','+gps2, data);
        line = p.getLine();
      }
      String place = getValue(line, "Common Name:");
      if (place != null) {
        data.strPlace = append(data.strPlace, " - ", stripFieldStart(place, "CPN:"));
        line = p.getLine();
      }
      String phone = getValue(line, "Phone:");
      if (phone != null) {
        data.strPhone = phone;
        line = p.getLine();
      }
      if (getValue(line, "Activity:") == null) abort();
      if (getValue(p, "Disposition:") == null) abort();
      String time = getValue(p,"Time reported:");
      if (time == null) abort();
      if (!parseDateTime(time, data)) abort();
    }

    private String getValue(Parser p, String key) {
      String line = p.getLine();
      if (line == null) return null;
      return getValue(line, key);
    }

    private String getValue(String line, String key) {
      if (!line.startsWith(key)) return null;
      return line.substring(key.length()).trim();
    }

    @Override
    public String getFieldNames() {
      return "SRC ID PLACE PHONE DATE TIME";
    }
  }

  private static final Pattern DATE_TIME_PTN = Pattern.compile("(\\d\\d?/\\d\\d?/(?:\\d{2}){1,2}) +(\\d\\d?:\\d\\d:\\d\\d(?: [AP]M)?)");

  private static boolean parseDateTime(String field, Data data) {
    Matcher match = DATE_TIME_PTN.matcher(field);
    if (!match.matches()) return false;
    data.strDate = match.group(1);
    field = match.group(2);
    if (field.endsWith("M")) {
      setTime(TIME_FMT, match.group(2), data);
    } else {
      data.strTime = field;
    }
    return true;
  }

  private class BaseSrcField extends SourceField {

    @Override
    public void parse(String field, Data data) {

      field = stripFieldStart(field, getRelativeField(-1));

      int delim = field.indexOf(" - ");
      if(delim >= 0) {
        data.strSource = field.substring(0, delim).trim();
        data.strCallId = field.substring(delim+3).trim();
      } else if (field.startsWith("- ")) {
        data.strCallId = field.substring(2).trim();
      } else super.parse(field, data);
    }

    @Override
    public String getFieldNames() {
      return super.getFieldNames() + " ID";
    }
  }

  private class BaseIdField extends IdField {
    @Override
    public void parse(String field, Data data) {
      if (data.strCallId.length() > 0) return;
      super.parse(field, data);
    }
  }

  private static final Pattern TIMES_PTN = Pattern.compile("([ \\w]+): (\\d\\d?/\\d\\d?/\\d{4}) +(\\d\\d?:\\d\\d:\\d\\d(?: [AP]M)?)|Dispo");
  private static final DateFormat TIME_FMT = new SimpleDateFormat("hh:mm:ss aa");
  private class BaseTimesField extends InfoField {
    @Override
    public boolean canFail() {
      return true;
    }

    @Override
    public boolean checkParse(String field, Data data) {
      if (field.startsWith("E911 CLASS:")) return false;
      parse(field, data);
      return true;
    }

    @Override
    public void parse(String field, Data data) {
      for (String line : field.split("\n")) {
        line = line.trim();
        Matcher match = TIMES_PTN.matcher(line);
        if (match.matches()) {
          String type = match.group(1);
          if (type.equals("Time reported")) {
            data.strDate = match.group(2);
            String time = match.group(3);
            if (time.endsWith("M")) {
              setTime(TIME_FMT, match.group(3), data);
            } else {
              data.strTime = time;
            }
          }
          else {
            if (type.equals("Time completed")) {
              data.msgType = MsgType.RUN_REPORT;
            }
          }
          times = append(times, "\n", line);
        } else if (line.startsWith("Disposition:")) {
          times = append(times, "\n", line);
        } else if (line.startsWith("CPN:")) {
          data.strPlace = append(data.strPlace, " - ", line.substring(4).trim());
        } else if (line.startsWith("Common Name:")) {
          data.strPlace = append(data.strPlace, " - ", stripFieldStart(line.substring(12).trim(), "CPN:"));
        } else if (line.startsWith("Activity:")) {
          continue;
        } else if (line.startsWith("Phone:")) {
          data.strPhone = line.substring(6).trim();
        } else {
          data.strSupp = append(data.strSupp, "\n", line);
        }
      }
    }

    @Override
    public String getFieldNames() {
      return "DATE TIME PLACE PHONE INFO";
    }
  }

  private class BaseUnit2Field extends UnitField {
    @Override
    public void parse(String field, Data data) {
      parseUnitField(false, field, data);
    }

    @Override
    public String getFieldNames() {
      return "UNIT PLACE PHONE GPS INFO";
    }
  }

  /**
   * Parse unit field for both regular pages and run reports
   * @param runReport true if parsing a run report
   * @param field Unit field to be parsed
   * @param data Data object
   */
  private void parseUnitField(boolean runReport, String field, Data data) {

    // Break field up into \n delimited tokens
    String info = "";
    for (String token : field.split("\n")) {
      token = token.trim();
      if (token.length() == 0) continue;
      token = token.replace('\t', ' ');

      // Check for tokens that force a mode switch
      if (token.equals("Incident Time:")) {
        if (unitMode == UnitMode.INFO) {
          data.strPlace = append(data.strPlace, " - ", info);
          info = "";
        }
        times = token;
        data.msgType = MsgType.RUN_REPORT;
        unitMode = UnitMode.TIMES;
        continue;
      }

      Matcher match = GPS_PTN.matcher(token);
      if (match.lookingAt()) {
        if (unitMode == UnitMode.INFO) {
          data.strPlace = append(data.strPlace, " - ", info);
          info = "";
        }

        gps = match.group(1);
        if (gps != null) {
          if (data.strGPSLoc.length() == 0) {
            setGPSLoc(gps+','+match.group(2), data);
          }
          gps = null;
          unitMode = UnitMode.INFO;
        } else {
          unitMode = UnitMode.GPS;
        }
        token = token.substring(match.end()).trim();
        if (token.length() > 0) {
          unitMode = UnitMode.INFO;
        } else {
          continue;
        }
      }

      // Switch on processing mode
      switch (unitMode) {

      // Unit mode
      case UNIT:
        String unit = token;
        int pt = unit.indexOf("   ");
        if (pt >= 0) unit = unit.substring(0,pt);
        if (unitPtn == null ? !unit.contains(" ") : unitPtn.matcher(unit).matches()) {
          data.strUnit = append(data.strUnit, " ", unit);
        } else {
          unitMode = UnitMode.INFO;
        }
        break;

      // Info mode is deferred because two other modes might switch to it
      case INFO:
        break;

      // times mode
      case TIMES:
        times = append(times, "\n", token);
        if (token.endsWith("Incident Re-Opened")) {
          data.msgType = MsgType.PAGE;
        } else if (token.endsWith("Incident Closed")) {
          data.msgType = MsgType.RUN_REPORT;
        }
        break;

      // GPS mode;
      case GPS:
        if (token.startsWith("LOC: ")) break;
        if (token.startsWith("LAT: ")) {
          gps = token.substring(5).trim();
          break;
        }
        if (gps != null && token.startsWith("LON: ")) {
          if (data.strGPSLoc.length() == 0) setGPSLoc(gps+','+token.substring(5).trim(), data);
          break;
        }
        if ((match = GPS_TRAIL_PTN.matcher(token)).lookingAt()) {
          token = token.substring(match.end()).trim();
        }
        unitMode = UnitMode.INFO;
        break;
      }

      if (unitMode == UnitMode.INFO) {
        token = INFO_JUNK_PTN.matcher(token).replaceAll("\n").trim();
        if (token.startsWith("CPN:")) {
          data.strPlace = append(data.strPlace, " - ", token.substring(4).trim());
        } else if (token.startsWith("Common Name:")) {
          data.strPlace = append(data.strPlace, " - ", stripFieldStart(token.substring(12).trim(), "CPN:"));
        } else if (token.startsWith("Phone:")) {
          data.strPhone = token.substring(6).trim();
        } else if (!token.equals(".")) {
          info = append(info, "\n", token);
        }
      }
    }

    data.strSupp = append(data.strSupp, "\n", info);
  }
  private static final Pattern GPS_PTN = Pattern.compile("E911 CLASS: *[A-Z]{1,4}\\d*(?: *LOC: .*?LAT: ([-+]?\\d+\\.\\d{6})\\d* *LON: ([-+]?\\d+\\.\\d{5,6})(?: *Lec:[a-z]{3,4}(?:[a-z]\\b)?|\\d* +T=\\S+(?: +CDMA)?(?: +S=[A-Z]+)?\\b)?)?", Pattern.CASE_INSENSITIVE);
  private static final Pattern GPS_TRAIL_PTN = Pattern.compile(" *Lec:[a-z]{3,4}|\\d* +T=\\S+(?: +CDMA)?(?: +S=[A-Z]+)?\\b");
  private static final Pattern INFO_JUNK_PTN = Pattern.compile(" *\\[\\d\\d/\\d\\d/\\d{4} +\\d\\d?:\\d\\d:\\d\\d : \\S+\\] *");

  private class BaseSourceId3Field extends Field {
    @Override
    public void parse(String field, Data data) {
      int pt = field.indexOf(' ');
      if (pt < 0) abort();
      data.strSource = field.substring(0,pt);
      data.strCallId = field.substring(pt+1).trim();
    }

    @Override
    public String getFieldNames() {
      return "SRC ID";
    }
  }

  private static final Pattern CITY_ST_PTN = Pattern.compile("(.*) ([A-Z]{2})");
  private class BaseCityStateField extends Field {
    @Override
    public void parse(String field, Data data) {
      Matcher match = CITY_ST_PTN.matcher(field);
      if (match.matches()) {
        field = match.group(1).trim();
        data.strState = match.group(2);
      }
      data.strCity = field;
    }

    @Override
    public String getFieldNames() {
      return "CITY ST";
    }
  }

  private class BaseInfoX3Field extends InfoField {
    @Override
    public void parse(String field, Data data) {
      if (field.equalsIgnoreCase("All") || field.length() == 0) return;
      data.strSupp = append(data.strSupp, "\n", field);
    }
  }

  private static final Pattern TIME_LOG_PTN = Pattern.compile("Time Reported: (\\d\\d:\\d\\d:\\d\\d) *(?:Time Completed: (\\d\\d:\\d\\d:\\d\\d) *)?(?:Caller's Phone: *(\\S*) *)?(?:Logs +<<Logs>>)? *(.*)", Pattern.DOTALL);
  private static final Pattern LOG_BRK_PTN = Pattern.compile(" *\\b\\d\\d:\\d\\d:\\d\\d [A-Za-z0-9]+: *");
  private class BaseTimeLogField extends Field {

    @Override
    public void parse(String field, Data data) {
      Matcher match = TIME_LOG_PTN.matcher(field);
      if (!match.matches()) abort();
      data.strTime = match.group(1);
      String compTime = match.group(2);
      data.strPhone = getOptGroup(match.group(3));
      if (compTime != null) {
        data.msgType = MsgType.RUN_REPORT;
        String times = "Time Reported: " + data.strTime + "\nTime Completed: " + compTime;
        data.strSupp = append(times, "\n", data.strSupp);
      }
      String logs = LOG_BRK_PTN.matcher(match.group(4)).replaceAll("\n").trim();
      data.strSupp = append(data.strSupp, "\n",  logs);
    }

    @Override
    public String getFieldNames() {
      return "TIME PHONE INFO";
    }

  }
}
