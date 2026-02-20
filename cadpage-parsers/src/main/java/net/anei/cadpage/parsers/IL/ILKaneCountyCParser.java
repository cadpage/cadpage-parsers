package net.anei.cadpage.parsers.IL;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.anei.cadpage.parsers.FieldProgramParser;
import net.anei.cadpage.parsers.MsgInfo.Data;


public class ILKaneCountyCParser extends FieldProgramParser {

  public ILKaneCountyCParser() {
    super("KANE COUNTY", "IL",
          "( CALL_TIME:DATETIME! CALL_TYPE:CALL! COMMON_NAME:PLACE! ADDRESS:ADDRCITY1/S6! CROSS_STREETS:X! UNITS_DUE:UNIT! NARRATIVE:INFO! INFO/N+ LAT/LON:GPSX1! CALLERS_TX:PHONE! " +
          "| DATETIME_CALL ADDRCITY2/S6 UNIT_MAP_ID_INFO! INFO/N+? GPSX2! " +
          ") END");
  }

  @Override
  public String getFilter() {
    return "shfradio@co.kane.il.us";
  }

  @Override
  protected boolean parseMsg(String subject, String body, Data data) {

    if (!subject.equals("!")) return false;
    return parseFields(body.split("\n"), data);
  }

  @Override
  public Field getField(String name) {
    if (name.equals("DATETIME")) return new DateTimeField("\\d\\d?/\\d\\d?/\\d{4} +\\d\\d:\\d\\d:\\d\\d", true);
    if (name.equals("ADDRCITY1")) return new MyAddressCity1Field();
    if (name.equals("GPSX1")) return new MyGPSX1Field();
    if (name.equals("GPSX2")) return new GPSField("\\d\\d\\.\\d+ +-\\d\\d\\.\\d+", true);
    if (name.equals("DATETIME_CALL")) return new MyDateTimeCallField();
    if (name.equals("ADDRCITY2")) return new MyAddressCity2Field();
    if (name.equals("UNIT_MAP_ID_INFO")) return new MyUnitMapIdInfoField();
    return super.getField(name);
  }

  private class MyGPSX1Field extends GPSField {
    public MyGPSX1Field() {
      super("\\d\\d\\.\\d+-\\d\\d\\.\\d+", true);
    }

    @Override
    public void parse(String field, Data data) {
      field = field.replace("-", ",-");
      super.parse(field, data);
    }
  }

  private static final Pattern DATE_TIME_CALL_PTN = Pattern.compile("(\\d\\d?/\\d\\d?/\\d{4}) +(\\d\\d:\\d\\d:\\d\\d) +(.*)");
  private class MyDateTimeCallField extends Field {
    @Override
    public void parse(String field, Data data) {
      Matcher match = DATE_TIME_CALL_PTN.matcher(field);
      if (!match.matches()) abort();
      data.strDate = match.group(1);
      data.strTime = match.group(2);
      data.strCall = match.group(3);
    }

    @Override
    public String getFieldNames() {
      return "DATE TIME CALL";
    }
  }

  private class MyAddressCity1Field extends AddressCityField {
    @Override
    public void parse(String field, Data data) {
      field = field.replace('@', '&');
      super.parse(field, data);
    }
  }

  private static final Pattern ADDRCITY_PLACE_X_PTN = Pattern.compile("(.*?)    (.*?)(?:    (.*))?");
  private static final Pattern ADDRCITY_PLACE_X_PTN2 = Pattern.compile("(.*?)  (.*?)(?:  (.*))?");
  private class MyAddressCity2Field extends AddressCityField {
    @Override
    public void parse(String field, Data data) {
      Matcher match = ADDRCITY_PLACE_X_PTN.matcher(field);
      boolean good = match.matches();
      if (!good) {
        match = ADDRCITY_PLACE_X_PTN2.matcher(field);
        good = match.matches();
      }
      if (good) {
        field = match.group(1);
        data.strPlace = match.group(2).trim();
        data.strCross = getOptGroup(match.group(3));
      }
      field = field.replace('@', '&');
      super.parse(field, data);
    }

    @Override
    public String getFieldNames() {
      return super.getFieldNames() + " PLACE X";
    }
  }

  private static final Pattern UNIT_MAP_ID_INFO_PTN1 = Pattern.compile("(?:(\\S+) +)?([A-Z]+-\\d+)(?: +(\\d{4}-\\d{8} \\([A-Z]{2}\\d+\\)))? *(.*)");
  private static final Pattern UNIT_MAP_ID_INFO_PTN2 = Pattern.compile("(?:(\\S+) +)?(\\d{4}-\\d{8} \\([A-Z]{2}\\d+\\)) *(.*)");
  private class MyUnitMapIdInfoField extends Field {
    @Override
    public void parse(String field, Data data) {
      Matcher match = UNIT_MAP_ID_INFO_PTN1.matcher(field);
      if (match.matches()) {
        data.strUnit = getOptGroup(match.group(1));
        data.strMap = match.group(2);
        data.strCallId = getOptGroup(match.group(3));
        data.strSupp = getOptGroup(match.group(4));
        return;
      }
      match = UNIT_MAP_ID_INFO_PTN2.matcher(field);
      if (match.matches()) {
        data.strUnit = getOptGroup(match.group(1));
        data.strCallId = getOptGroup(match.group(2));
        data.strSupp = getOptGroup(match.group(3));
        return;
      }
      abort();
    }

    @Override
    public String getFieldNames() {
      return "UNIT MAP ID INFO";
    }

  }
}
