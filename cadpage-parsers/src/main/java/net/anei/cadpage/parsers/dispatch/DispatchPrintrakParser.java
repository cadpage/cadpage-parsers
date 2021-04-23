package net.anei.cadpage.parsers.dispatch;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Properties;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.anei.cadpage.parsers.FieldProgramParser;
import net.anei.cadpage.parsers.MsgInfo.Data;
import net.anei.cadpage.parsers.MsgInfo.MsgType;


public class DispatchPrintrakParser extends FieldProgramParser {

  /**
   * Flag indicating we should use the CMT1 field to replace the normal call field
   */
  public static final int FLG_USE_CMT1_CALL = 0x10;

  /**
   * Format versions
   */
  private static final int FLG_VERSION_MASK = 0xF;
  public static final int FLG_VERSION_1 = 0x0;
  public static final int FLG_VERSION_2 = 0x1;

  public DispatchPrintrakParser(String defCity, String defState) {
    this((String[])null, defCity, defState, null, 0);
  }

  public DispatchPrintrakParser(String defCity, String defState, String expTerm) {
    this((String[])null, defCity, defState, expTerm, 0);
  }

  public DispatchPrintrakParser(String defCity, String defState, int flags) {
    this((String[])null, defCity, defState, null, flags);
  }

  public DispatchPrintrakParser(Properties cityCodes, String defCity, String defState) {
    this(cityCodes, defCity, defState, null, 0);
  }

  public DispatchPrintrakParser(String[] cityList, String defCity, String defState) {
    this(cityList, defCity, defState, null, 0);
  }

  public DispatchPrintrakParser(Properties cityCodes, String defCity, String defState, String expTerm) {
    this(cityCodes, defCity, defState, expTerm, 0);
  }

  public DispatchPrintrakParser(String[] cityList, String defCity, String defState, String expTerm) {
    this(cityList, defCity, defState, expTerm, 0);
  }

  public DispatchPrintrakParser(Properties cityCodes, String defCity, String defState, int flags) {
    this(cityCodes, defCity, defState, null, flags);
  }

  public DispatchPrintrakParser(String[] cityList, String defCity, String defState, int flags) {
    this(cityList, defCity, defState, null, flags);
  }

  private boolean useCmt1Call;

  public DispatchPrintrakParser(Properties cityCodes, String defCity, String defState, String expTerm, int flags) {
    super(cityCodes, defCity, defState, getProgramStr(expTerm, flags));
    useCmt1Call = (flags & FLG_USE_CMT1_CALL) != 0;
  }

  public DispatchPrintrakParser(String[] cityList, String defCity, String defState, String expTerm, int flags) {
    super(cityList, defCity, defState, getProgramStr(expTerm, flags));
    useCmt1Call = (flags & FLG_USE_CMT1_CALL) != 0;
  }

  private static String getProgramStr(String expTerm, int flags) {

    int version = (flags & FLG_VERSION_MASK);
    String program =
        (version == FLG_VERSION_1 ?
            "( TIME:TIME_INFO! TYP:CALL " +
            "| PRI:PRI1 INC:ID " +
                "( AD:ADDR! LOC:CITY TIME:TIME_INFO! TYP:CALL " +
                "| LOC:ADDR! AD:PLACE! ( APT:APT! CRSTR:X! CITY:CITY LAT:GPS1 LONG:GPS2 DESC:PLACE! TYP:CODE! TYPN:CALL% CMT1:INFO/N% CC_TEXT:CALL3 PROBLEM:INFO2/N CAD_RESPONSE:PRI2 DISPATCH_LEVEL:CODE2 TIME:DATETIME " +
                                      "| DESC:PLACE! BLD:APT! FLR:APT/D? APT:APT/D! TYP:CODE! MODCIR:CALL/SDS! CMT1:INFO/N " +
                                      ") UNS:UNIT INC:ID/L " +
                "| CODE:CODE TYP:CALL! BLD:APT APT:APT AD:ADDR! APT:APT ( CTY:CITY | CITY:CITY ) MAP:MAP LOC:PLACE CALLER:NAME XST:X CN:NAME CMT1:INFO/1N" +
                  " CMT2:INFO/N CMT3:INFO/N CMT4:INFO/N CMT5:INFO/N CMT6:INFO/N CMT7:INFO/N CMT8:INFO/N CMT9:INFO/N CE:INFO? CMT2:INFO TIME:TIME UNTS:UNIT XST:X XST2:X UNTS:UNIT XST:X XST2:X " +
                ") " +
           ") END"
        : version == FLG_VERSION_2 ?
            "TYP:CALL! LOC:PLACE! AD:ADDR/S! XST:X! CMT1:INFO! UNTS:UNIT!"
        : null);
    return setExpectFlag(program, expTerm);
  }

  private static final Pattern GEN_ALERT_PTN = Pattern.compile("(?:([-A-Z0-9]+) +)?TIME: *(\\d\\d:\\d\\d)\\b *(.*)", Pattern.DOTALL);
  private static final Pattern BREAK_PTN = Pattern.compile(" *[\n]+ *");
  private static final Pattern SRC_PTN = Pattern.compile("([^:]+?)(?: +|\n)([A-Z0-9]+:.*)", Pattern.DOTALL);

  private boolean rejectLatLongKeywords;

  @Override
  protected boolean parseMsg(String body, Data data) {

    Matcher match = GEN_ALERT_PTN.matcher(body);
    if (match.matches()) {
      setFieldList("SRC TIME INFO");
      data.msgType = MsgType.GEN_ALERT;
      data.strSource = getOptGroup(match.group(1));
      data.strTime = match.group(2);
      data.strSupp = match.group(3).trim();
      return true;
    }

    body = body.replace('\t', ' ');
    body = BREAK_PTN.matcher(body).replaceAll("\n");
    match = SRC_PTN.matcher(body);
    if (match.matches()) {
      data.strSource = match.group(1);
      body = match.group(2);
    }
    body = body.replace(" CMTS:", " CMT1:").replace("CMT:",  " CMT1:").replace("AD:", " AD:").replace("UNTS:",  " UNTS:").replace("UNITS:", " UNTS:").replace(" X-ST:", " XST:");
    body = body.replace("TYP:", " TYP:");
    body = body.replace(" CALLER / STATEMENT:", " CALLER STATEMENT:");
    body = body.replace(" CALLER CMT2:", " CMT2:");
    rejectLatLongKeywords = true;
    return super.parseMsg(body.trim(), data);
  }

  @Override
  public String getProgram() {
    return "SRC " + super.getProgram();
  }

  @Override
  protected boolean rejectBreakKeyword(String key) {
    if (key.equals("LAT") || key.equals("LONG")) return rejectLatLongKeywords;
    if (rejectLatLongKeywords && key.equals("CRSTR")) rejectLatLongKeywords = false;
    return false;
  }

  private static final DateFormat DATE_TIME_FMT = new SimpleDateFormat("EEEEEE, MMM dd, yyyy hh:mm:ss aa");

  @Override
  public Field getField(String name) {
    if (name.equals("PRI1")) return new BasePriorityField();
    if (name.equals("PRI2")) return new BasePriority2Field();
    if (name.equals("ADDR")) return new BaseAddressField();
    if (name.equals("APT")) return new BaseAptField();
    if (name.equals("PLACE")) return new BasePlaceField();
    if (name.equals("DATETIME")) return new DateTimeField(DATE_TIME_FMT, true);
    if (name.equals("TIME")) return new BaseTimeField();
    if (name.equals("TIME_INFO")) return new BaseTimeInfoField();
    if (name.equals("CALL3")) return new BaseCall3Field();
    if (name.equals("CODE2"))  return new BaseCode2Field();
    if (name.equals("INFO")) return new BaseInfoField();
    if (name.equals("INFO2")) return new BaseInfo2Field();
    if (name.equals("UNIT")) return new BaseUnitField();
    if (name.equals("X")) return new BaseCrossField();
    return super.getField(name);
  }

  private static final Pattern PRI_ID_PTN = Pattern.compile("(\\S*)\\s+([A-Z]{3}\\d{12})");
  private class BasePriorityField extends PriorityField {
    @Override
    public void parse(String field, Data data) {
      Matcher match = PRI_ID_PTN.matcher(field);
      if (match.matches()) {
        field = match.group(1);
        data.strCallId = match.group(2);
      }
      super.parse(field, data);
    }

    @Override
    public String getFieldNames() {
      return "PRI ID";
    }
  }

  private class BasePriority2Field extends PriorityField {
    @Override
    public void parse(String field, Data data) {
      field = field.replace('\n', ' ');
      int pt = field.indexOf(' ');
      if (pt >= 0) {
        String left = field.substring(pt+1).trim();
        field = field.substring(0, pt);
        if (!"DISPATCH LEVEL".startsWith(left)) {
          data.strSupp = append(data.strSupp, "\n", left);
        }
      }
      super.parse(field, data);
    }

    @Override
    public String getFieldNames() {
      return "PRI INFO";
    }
  }

  private static final Pattern APT_PTN = Pattern.compile("\\b(?:APT|RM|UNIT) +([-A-Z0-9]+)$", Pattern.CASE_INSENSITIVE);
  private class BaseAddressField extends AddressCityField {
    @Override
    public void parse(String field, Data data) {
      if (field.startsWith("LAT:")) {
        data.strAddress =  field.replaceAll("<", "").replaceAll(">", "");
        return;
      }

      int pt = field.indexOf(" LAT:");
      if (pt >= 0) {
        setGPSLoc(field.substring(pt+1), data);
        field = field.substring(0,pt).trim();
      }

      String apt = "";
      Matcher match = APT_PTN.matcher(field);
      if (match.find()) {
        apt = match.group(1);
        field = field.substring(0,match.start()).trim();
      }
      if (field.startsWith("LATLONG")) {
        parseAddress(field, data);
      } else {
        super.parse(field, data);
      }
      data.strApt = append(data.strApt, " - ", apt);
    }

    @Override
    public String getFieldNames() {
      return super.getFieldNames() + " GPS?";
    }
  }

  private class BaseAptField extends AptField {

    @Override
    public void parse(String field, Data data) {
      data.strApt = append(data.strApt, "-", field);
    }
  }

  private static final Pattern PLACE_PHONE_PTN = Pattern.compile("\\d{10}");
  private static final Pattern PLACE_GPS_PTN = Pattern.compile("([-+]?\\d{2,3}\\.\\d{2}\\.\\d{2}) +([-+]?\\d{2,3}\\.\\d{2}\\.\\d{2})");
  private class BasePlaceField extends PlaceField {
    @Override
    public void parse(String field, Data data) {
      if (field.equals("<UNKNOWN>")) return;
      Matcher match;
      if (field.startsWith("CALLBK=")) {
        data.strPhone = field.substring(7).trim();
      } else if (PLACE_PHONE_PTN.matcher(field).matches()) {
        data.strPhone = field;
      } else if (field.startsWith("LAT:")) {
        if (data.strGPSLoc.length() == 0) {
          setGPSLoc(field.replace("<", "").replaceAll(">", ""), data);
        }
      } else if ((match = PLACE_GPS_PTN.matcher(field)).matches()) {
        if (data.strGPSLoc.length() == 0) {
          field = (match.group(1)+','+match.group(2)).replace('.', ' ');;
          setGPSLoc(field, data);
        }
      } else {
        super.parse(field, data);
      }
    }

    @Override
    public String getFieldNames() {
      return "PLACE PHONE GPS";
    }
  }

  private static final Pattern DATE_TIME_PTN = Pattern.compile("(?:(?:(\\d\\d/\\d\\d/\\d{4})|.*) +)?(\\d\\d:\\d\\d(?::\\d\\d)?)(?![:0-9]) *(.*)");
  private class BaseTimeField extends Field {
    @Override
    public void parse(String field, Data data) {

      // Date time fields tend to be garbled
      // so just ignore anything that does not make sense
      if (field.length() == 0) return;
      Matcher match = DATE_TIME_PTN.matcher(field);
      if (!match.matches()) return;
      data.strDate = getOptGroup(match.group(1));
      data.strTime = match.group(2);
      data.strUnit = match.group(3);
    }

    @Override
    public String getFieldNames() {
      return "DATE TIME UNIT";
    }
  }

  private class BaseTimeInfoField extends TimeField {
    @Override
    public void parse(String field, Data data) {
      int pt = field.indexOf(' ');
      if (pt >= 0) {
        data.strSupp = append(data.strSupp, "\n", field.substring(pt+1).trim());
        field = field.substring(0,pt).trim();
      }
      super.parse(field, data);
    }

    @Override
    public String getFieldNames() {
      return "TIME INFO";
    }
  }

  private class BaseCall3Field extends CallField {
    @Override
    public void parse(String field, Data data) {

      // Second call field generally duplicates the first one.
      // Take whichever is the longer
      if (field.length() <= data.strCall.length())  return;
      data.strCall = field;
    }
  }

  private class BaseCode2Field extends CodeField {
    @Override
    public void parse(String field, Data data) {
      int pt = field.indexOf('\n');
      if (pt >= 0) {
        String trail = field.substring(pt+1).trim();
        field = field.substring(0,pt).trim();
        data.strSupp = append(data.strSupp, "\n", trail);
      }

      pt = field.indexOf(' ');
      if (pt >= 0) field = field.substring(0, pt);
      data.strCode = append(data.strCode, "/", field);
    }

    @Override
    public String getFieldNames() {
      return "CODE INFO";
    }
  }

  class BaseInfoField extends InfoField {

    private boolean cmt1;

    @Override
    public void setQual(String qual) {
      super.setQual(qual);
      cmt1 = qual != null && qual.contains("1");
    }

    @Override
    public void parse(String field, Data data) {
      field = field.replace(" CMT:", "\n");
      for (String line : field.split("\n")) {
        line = line.trim();
        if (line.startsWith("INCIDENT CLONED FROM ")) continue;
        if (line.startsWith("Original Date/Time for ")) continue;
        if (line.startsWith("IAAssocInc")) continue;
        if (line.startsWith("Parent Inc ")) continue;
        if (line.startsWith("Child Inc ")) continue;
        if (line.startsWith("SiblingInc ")) continue;
        if (line.startsWith("Initial Field Initiate ")) continue;
        if ("Original Location ".startsWith(line)) continue;
        int pt = line.indexOf("Original Location :");
        if (pt >= 0) {
          if (pt > 0) data.strSupp = append(data.strSupp, "\n", line.substring(0,pt).trim());
          line = line.substring(pt+19).trim();
          if (data.strPlace.length() == 0) {
            data.strPlace = line;
            continue;
          } else if (!line.startsWith(data.strPlace)) {
            continue;
          } else {
            line = line.substring(data.strPlace.length()).trim();
          }
        }

        if (useCmt1Call && cmt1) {
          line = stripFieldStart(line, "**");
          if (line.length() > 0) data.strCall = line;
        } else {
          data.strSupp = append(data.strSupp, "\n", line);
        }
      }
    }

    @Override
    public String getFieldNames() {
      return super.getFieldNames() + " PLACE";
    }
  }

  class BaseInfo2Field extends BaseInfoField {
    @Override
    public void parse(String field, Data data) {
      int pt = field.lastIndexOf('\n');
      if (pt >= 0) {
        String left = field.substring(pt+1).trim();
        if ("CAD RESPONSE".startsWith(left)) {
          field = field.substring(0, pt).trim();
        }
      }
      super.parse(field, data);
    }
  }

  private class BaseUnitField extends UnitField {
    @Override
    public void parse(String field, Data data) {
      if (data.strUnit.contains("field")) return;
      if (data.strUnit.length() > 0 && field.contains(data.strUnit)) {
        data.strUnit = field;
      } else {
        data.strUnit = append(data.strUnit, " ", field);
      }
    }
  }

  private class BaseCrossField extends CrossField {
    @Override
    public void parse(String field, Data data) {
      field = stripFieldStart(field, "UNKNOWN /");
      field = stripFieldStart(field, "/");
      field = stripFieldEnd(field, "/");
      super.parse(field, data);
    }
  }
}
