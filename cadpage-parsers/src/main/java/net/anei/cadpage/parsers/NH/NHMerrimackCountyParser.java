package net.anei.cadpage.parsers.NH;

import java.text.SimpleDateFormat;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.anei.cadpage.parsers.FieldProgramParser;
import net.anei.cadpage.parsers.MsgInfo.Data;



public class NHMerrimackCountyParser extends FieldProgramParser {

  public NHMerrimackCountyParser() {
    super("MERRIMACK COUNTY","NH",
          "ID ADDRCITYST! Time_reported:DATETIME! Unit(s)_responded:UNIT! END");
  }

  @Override
  public String getFilter() {
    return "notification@nhpd.cloud";
  }

  @Override
  public int getMapFlags() {
    return MAP_FLG_PREFER_GPS;
  }

  @Override
  protected boolean parseMsg(String subject, String body, Data data) {
    if (subject.isEmpty()) return false;
    return parseFields(body.split("\n"), data);
  }

  @Override
  public Field getField(String name) {
    if (name.equals("ID")) return new IdField("\\d{7} \\(P#:\\d\\d-\\d{6}\\)", true);
    if (name.equals("ADDRCITYST")) return new MyAddressCityStateField();
    if (name.equals("DATETIME")) return new MyDateTimeField();
    if (name.equals("UNIT")) return new MyUnitField();
    return super.getField(name);
  }

  private static final Pattern CALL_ADDR_GPS_PTN = Pattern.compile("(.*?) at (.*?)(?: \\(([-0-9.,]+)\\))?");
  private class MyAddressCityStateField extends AddressCityStateField {
    @Override
    public void parse(String field, Data data) {
      Matcher match = CALL_ADDR_GPS_PTN.matcher(field);
      if (!match.matches()) abort();
      data.strCall = match.group(1).trim();
      super.parse(match.group(2).trim(), data);
      String gps = match.group(3);
      if (gps != null) setGPSLoc(gps, data);
    }

    @Override
    public String getFieldNames() {
      return "CALL ADDR APT CITY ST GPS";
    }
  }

  private static final Pattern DATE_TIME_PTN = Pattern.compile("(\\d\\d/\\d\\d/\\d\\d) +(\\d\\d?:\\d\\d:\\d\\d [AP]M)");
  private static final SimpleDateFormat TIME_FMT = new SimpleDateFormat("hh:mm:ss aa");
  private class MyDateTimeField extends DateTimeField {
    @Override
    public void parse(String field, Data data) {
      Matcher match = DATE_TIME_PTN.matcher(field);
      if (!match.matches()) abort();
      data.strDate = match.group(1);
      setTime(TIME_FMT, match.group(2), data);
    }
  }

  private class MyUnitField extends UnitField {
    @Override
    public void parse(String field, Data data) {
      field = field.replace(' ', '_');
      super.parse(field, data);
    }
  }
}
