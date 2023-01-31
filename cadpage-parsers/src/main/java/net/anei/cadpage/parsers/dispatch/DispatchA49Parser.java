package net.anei.cadpage.parsers.dispatch;

import java.util.Properties;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.anei.cadpage.parsers.FieldProgramParser;
import net.anei.cadpage.parsers.MsgInfo.Data;
import net.anei.cadpage.parsers.MsgInfo.MsgType;

/*
Lafayette Parish, LA
*/

public class DispatchA49Parser extends FieldProgramParser {

  private final boolean checkCity;
  private final Properties callCodes;

  public  DispatchA49Parser(String defCity, String defState) {
    this(null, defCity, defState, null);
  }

  public  DispatchA49Parser(String defCity, String defState, Properties callCodes) {
    this(null, defCity, defState, callCodes);
  }

  public  DispatchA49Parser(Properties cityCodes, String defCity, String defState) {
    this(cityCodes, defCity, defState, null);
  }

  public DispatchA49Parser(Properties cityCodes, String defCity, String defState, Properties callCodes) {
    super(cityCodes, defCity, defState,
          "( RPT#:ID EQPT:UNIT! ADDR:ADDR! INFO/R! TIMES! TIMES+? " +
          "| Call_Number:ID! Date/Time:DATETIME! Address:ADDR! " +
          "| date:DATE! time:TIME! inc#:ID! INFO/N+ " +
          "| CAD_Num:SKIP! Addr:ADDR/S! Times:EMPTY! INFO/R! INFO/N+ Rpt#:ID END " +
          "| ( Rpt#:ID! Addr:ADDR/S! Inc_Type:CODE! " +
            "| DATETIME2! ADDR:ADDR/S! RCN:UNIT! CALL " +
            "| DATE_TIME_SRC! Addr:ADDR/S! City:CITY? Cross:X? Inc_Type:CODE? Juris:SKIP? Report_#:ID? " +
            "| Date:DATE! Time:TIME! Inc#:ID! INFO/RN+ " +
            ") REMARKS? EXTRA+ )");
    checkCity = cityCodes != null;
    this.callCodes = callCodes;
  }

  private static final Pattern REMARKS_PTN = Pattern.compile("(\nRemarks)[: ]+");

  @Override
  protected boolean parseMsg(String body, Data data) {
    body = REMARKS_PTN.matcher(body).replaceFirst("$1:\n");
    if (body.startsWith("Call Number:") || body.startsWith("date:")) data.msgType = MsgType.RUN_REPORT;
    if (! parseFields(body.split("\n+"), data)) return false;

    if (data.msgType == MsgType.PAGE) {
      if (data.strCode.isEmpty() && data.strCall.isEmpty() && !data.strSupp.isEmpty()) {
        int pt = data.strSupp.indexOf('\n');
        if (pt >= 0) {
          data.strCode = data.strSupp.substring(0,pt).trim();
          data.strSupp = data.strSupp.substring(pt+1).trim();
        } else {
          data.strCode = data.strSupp;
          data.strSupp = "";
        }
      }

      if (data.strCall.isEmpty()) {
        if (callCodes != null) {
          String call = callCodes.getProperty(data.strCode);
          if (call != null) data.strCall = call;
        }
        if (data.strCall.isEmpty()) {
          data.strCall = data.strCode;
          data.strCode = "";
        }
      }
    }

    return true;
  }

  @Override
  public Field getField(String name) {
    if (name.equals("DATETIME")) return new DateTimeField("\\d\\d/\\d\\d/\\d{4} \\d\\d:\\d\\d:\\d\\d", true);
    if (name.equals("DATETIME2")) return new BaseDateTime2Field();
    if (name.equals("DATE")) return new DateField("\\d\\d/\\d\\d/\\d{4}", true);
    if (name.equals("TIME")) return new TimeField("\\d\\d:\\d\\d", true);
    if (name.equals("DATE_TIME_SRC")) return new BaseDateTimeSourceField();
    if (name.equals("ADDR")) return new BaseAddressField();
    if (name.equals("CITY")) return new BaseCityField();
    if (name.equals("TIMES")) return new BaseTimesField();
    if (name.equals("REMARKS")) return new SkipField("Remarks:", true);;
    if (name.equals("EXTRA")) return new BaseExtraField();
    return super.getField(name);
  }

  private static final Pattern DATE_TIME_SRC_PTN = Pattern.compile("Date:(\\d\\d/\\d\\d/\\d{4}) Time:(\\d\\d:\\d\\d)(?:EQPT:(\\S+)| Num:(\\S+)|)");
  private class BaseDateTimeSourceField extends Field {
    @Override
    public void parse(String field, Data data) {
      Matcher match = DATE_TIME_SRC_PTN.matcher(field);
      if (!match.matches()) abort();
      data.strDate = match.group(1);
      data.strTime = match.group(2);
      data.strSource = getOptGroup(match.group(3));
      data.strCallId = getOptGroup(match.group(4));
    }

    @Override
    public String getFieldNames() {
      return "DATE TIME SRC ID";
    }
  }

  private static final Pattern DATE_TIME2_PTN = Pattern.compile("DATE:(\\d{6}) TIME:(\\d{6})");
  private class BaseDateTime2Field extends DateTimeField {
    @Override
    public void parse(String field, Data data) {
      Matcher match = DATE_TIME2_PTN.matcher(field);
      if (!match.matches()) abort();
      String date = match.group(1);
      data.strDate = date.substring(2,4)+'/'+date.substring(4)+'/'+date.substring(0,2);
      String time = match.group(2);
      data.strTime = time.substring(0,2)+':'+time.substring(2,4)+':'+time.substring(4);
    }
  }

  private class BaseAddressField extends AddressField {
    @Override
    public void parse(String field, Data data) {
      field = stripFieldStart(field, "\".");
      if (!checkCity) {
        parseAddress(field, data);
      } else {
        super.parse(field, data);
      }
    }
  }

  private class BaseCityField extends CityField {
    @Override
    public void parse(String field, Data data) {
      if (field.length() == 0) return;
      super.parse(field, data);
    }
  }

  private static final Pattern TIMES_PTN = Pattern.compile("[A-Z]+: *(\\d\\d:\\d\\d:\\d\\d)");
  private class BaseTimesField extends InfoField {
    @Override
    public boolean canFail() {
      return true;
    }

    @Override
    public boolean checkParse(String field, Data data) {
      Matcher match = TIMES_PTN.matcher(field);
      if (match.matches()) return false;
      data.msgType = MsgType.RUN_REPORT;
      if (!match.group(1).equals("00:00:00")) super.parse(field, data);
      return true;
    }

    @Override
    public void parse(String field, Data data) {
      data.strSupp = append(data.strSupp, "\n", field);
    }
  }

  private static final Pattern EXTRA_JUNK_PTN = Pattern.compile("(.*?) +(?:[A-Z]{2}\\d{3},)?\\d{6}");
  private static final Pattern EXTRA_ID_PTN  = Pattern.compile(">(?:RPT#|AC)< *([-\\d]+)");
  private static final Pattern EXTRA_CALL_PTN = Pattern.compile("[A-Z]>>?IC< *(?:[A-Z]\\.)? *(.*?)");
  private static final Pattern EXTRA_GPS_PTN = Pattern.compile("\\bLat=([-+]\\d+\\.\\d{4,}) Long=([-+]\\d+\\.\\d{4,})\\b");
  private static final Pattern EXTRA_TIME_OP_PTN = Pattern.compile("(.*) \\d{4},\\d{3}");
  private static final Pattern EXTRA_PREFIX_PTN = Pattern.compile("(?:[A-Z]>)?(?:>(?:AC|E9|IC)<)? *(?:[A-Z]\\.)? *(.*)");
  private static final Pattern EXTRA_PHONE_PTN1 = Pattern.compile("(?:\\d{3})?\\d{7}");
  private static final Pattern EXTRA_PHONE_PTN2 = Pattern.compile("(?:LOC = .*\\bP#|>P#< P-ANI:) *(\\(?\\d{3}\\)? \\d{3}-\\d{4})\\b.*", Pattern.CASE_INSENSITIVE);
  private static final Pattern EXTRA_LEAD_GT_PTN = Pattern.compile(">(?![A-Z]+<)");
  private class BaseExtraField extends Field {
    @Override
    public void parse(String field, Data data) {

      Matcher match = EXTRA_JUNK_PTN.matcher(field);
      if (match.matches()) field = match.group(1);

      match = EXTRA_ID_PTN.matcher(field);
      if (match.matches()) {
        data.strCallId = match.group(1);
        return;
      }

      match = EXTRA_CALL_PTN.matcher(field);
      if (match.matches()) {
        String call = fixCall(match.group(1));
        if (call == null) return;
        if (data.strCall.contains(call)) return;
        data.strCall = append(data.strCall, " / ", call);
        return;
      }

      match = EXTRA_GPS_PTN.matcher(field);
      if (match.find()) {
        setGPSLoc(match.group(1) + ',' + match.group(2), data);
        return;
      }

      match = EXTRA_TIME_OP_PTN.matcher(field);
      if (match.matches()) field = match.group(1).trim();

      match = EXTRA_PREFIX_PTN.matcher(field);
      if (match.matches()) field = match.group(1);

      // Old data changes are not worth keeping
      if (field.startsWith(">UG<")) return;

      // Place name is worth extracting
      if (field.startsWith(">CP<")) {
        field = field.substring(4).trim();
        if (field.toUpperCase().startsWith("ALARM =")) {
          field = field.substring(7).trim();
          int pt = field.indexOf(',');
          if (pt >= 0) field = field.substring(pt+1).trim();
        }
        data.strPlace = append(data.strPlace, " - ", field);
        return;
      }

      if (EXTRA_PHONE_PTN1.matcher(field).matches()) {
        data.strPhone = field;
        return;
      }

      // Drop useless cell tower address
      if (field.startsWith("ADDR =")) return;

      // and coordinate comment
      if (field.startsWith("WPH2 COORDS.")) return;

      // Cell phone number
      match = EXTRA_PHONE_PTN2.matcher(field);
      if (match.matches()) {
        data.strPhone = match.group(1);
        return;
      }

      // Cell phone name
      if (field.startsWith("NAME =")) {
        data.strName = cleanWirelessCarrier(field.substring(6).trim());
        return;
      }

      match = EXTRA_LEAD_GT_PTN.matcher(field);
      if (match.lookingAt()) field = field.substring(match.end()).trim();

      data.strSupp = append(data.strSupp, "\n", field);
    }

    @Override
    public String getFieldNames() {
      return "ID CALL PLACE NAME PHONE INFO GPS";
    }
  }

  protected String fixCall(String call) {
    call = stripFieldEnd(call, " Y");
    if (call.equals("DEFAULT")) return null;
    return call;
  }
}
