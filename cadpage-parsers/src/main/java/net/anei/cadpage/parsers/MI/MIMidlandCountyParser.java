package net.anei.cadpage.parsers.MI;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.anei.cadpage.parsers.FieldProgramParser;
import net.anei.cadpage.parsers.MsgInfo.Data;


public class MIMidlandCountyParser extends FieldProgramParser {

  public MIMidlandCountyParser() {
    this("MIDLAND COUNTY", "MI");
  }

  MIMidlandCountyParser(String defCity, String defState) {
    super(defCity, defState,
          "( SELECT/1 ( UNIT_STAT! ( BUS:PLACE! ADDX:ADDR! APT:APT! CODE:CALL! http:GPS " +
                                  "| ADDR! APT:APT! CALL! http:GPS ) " +
                     "| BUS:PLACE! ADDX:ADDR! APT:APT! CODE:CALL! http:GPS " +
                     "| CALL_ADDR! COMMENTS! INFO/N+? X? PLACE/SDS+? http:GPS " +
                     "| ADDR! APT:APT! CALL! http:GPS ) END " +
           "| CALL_TYPE:CALL! ADDRESS:ADDRCITY! PRIORITY_COMMENT:INFO! INFO/N+ NARRATIVE:INFO/N INFO/N+ UNITS:UNIT " +
           ")");
  }

  @Override
  public String getAliasCode() {
    return "MIMidlandCounty";
  }

  @Override
  public String getFilter() {
    return "@midland911.org,9300";
  }

  @Override
  public int getMapFlags() {
    return MAP_FLG_PREFER_GPS;
  }

  private static final Pattern MARKER = Pattern.compile("CAD Page for CFS ([-A-Z0-9]+)(?:[ ,]+(.*))?", Pattern.DOTALL);

  @Override
  protected boolean parseMsg(String subject, String body, Data data) {
    if (subject.equals("CAD Paging")) {
      setSelectValue("2");
    } else {
      Matcher match = MARKER.matcher(subject);
      if (match.matches()) {
        data.strCallId = match.group(1);
      } else {
        match = MARKER.matcher(body);
        if (!match.matches()) return false;
        data.strCallId = match.group(1);
        body = match.group(2);
        if (body == null) return false;
      }
      setSelectValue("1");
    }

    body = body.replace(" APT:", "\nAPT:");
    return parseFields(body.split("\n"), data);
  }

  @Override
  public String getProgram() {
    return "ID " + super.getProgram();
  }

  @Override
  public Field getField(String name) {
    if (name.equals("UNIT_STAT")) return new UnitField("Unit (\\S+) +Status:.*", true);
    if (name.equals("CALL_ADDR")) return new MyCallAddressField();
    if (name.equals("COMMENTS")) return new MyCommentsField();
    if (name.equals("INFO")) return new MyInfoField();
    if (name.equals("X")) return new MyCrossField();
    if (name.equals("GPS")) return new MyGPSField();
    return super.getField(name);
  }

  private static final Pattern GPS_PTN = Pattern.compile("//maps.google.com/(?:maps)?\\?q=([+-]\\d+\\.\\d{5})(?: +|%2[0C])([+-]\\d+\\.\\d{5})");
  private class MyGPSField extends GPSField {
    public void parse(String field, Data data) {
      Matcher match = GPS_PTN.matcher(field);
      if (!match.matches()) return;
      setGPSLoc(match.group(1) + ',' + match.group(2), data);
    }
  }

  private class MyCallAddressField extends Field {
    @Override
    public boolean canFail() {
      return true;
    }

    @Override
    public boolean checkParse(String field, Data data) {
      int pt = field.lastIndexOf(',');
      if (pt < 0) return false;

      // Sometimes intersections contains a comma and might be
      // mistaken for a call/address field.  So check the next field
      // to rule that out
      if (getRelativeField(+1).startsWith("APT:")) return false;

      // We are good to go
      data.strCall = field.substring(0, pt).trim();
      parseAddress(field.substring(pt+1), data);
      return true;
    }

    @Override
    public void parse(String field, Data data) {
      if (!checkParse(field, data)) abort();
    }

    @Override
    public String getFieldNames() {
      return "CALL ADDR APT";
    }
  }

  private static final Pattern COMMENTS_PTN = Pattern.compile("(\\d\\d?:\\d\\d)(?: *COMMENTS:)?");
  private class MyCommentsField extends TimeField {
    @Override
    public void parse(String field, Data data) {
      Matcher match = COMMENTS_PTN.matcher(field);
      if (!match.matches()) abort();
      data.strTime = match.group(1);
    }
  }

  private static final Pattern INFO_JUNK_PTN = Pattern.compile("\\[\\d\\d?:\\d\\d:\\d\\d .*\\]");
  private static final Pattern INFO_CODE_PRI_PTN = Pattern.compile("dispatchlevel=(\\S+):cadresponse=(\\d+)");
  private class MyInfoField extends InfoField {
    @Override
    public boolean canFail() {
      return true;
    }

    @Override
    public boolean checkParse(String field, Data data) {
      if (field.startsWith("Cross Streets :") || field.startsWith("http:")) {
        return INFO_JUNK_PTN.matcher(getRelativeField(+1)).matches();
      }
      parse(field, data);
      return true;
    }

    @Override
    public void parse(String field, Data data) {

      Matcher match = INFO_JUNK_PTN.matcher(field);
      if (match.matches()) return;

      match = INFO_CODE_PRI_PTN.matcher(field);
      if (match.matches()) {
        data.strCode = match.group(1);
        data.strPriority = match.group(2);
        return;
      }

      super.parse(field, data);
    }

    @Override
    public String getFieldNames() {
      return "CODE PRI INFO";
    }
  }

  private class MyCrossField extends CrossField {
    @Override
    public boolean canFail() {
      return true;
    }

    @Override
    public boolean checkParse(String field, Data data) {
      if (!field.startsWith("Cross Streets :")) return false;
      field = field.substring(15).trim();
      field = field.replace('*', '/');
      super.parse(field, data);
      return true;
    }

    @Override
    public void parse(String field, Data data) {
      if (!checkParse(field, data)) abort();
    }
  }

  private static final Pattern HOUSE_RANGE_PTN = Pattern.compile("\\d+-\\d+ +(.*)");

  @Override
  public String adjustMapAddress(String address, String city, boolean cross) {
    if (cross) {
      Matcher match = HOUSE_RANGE_PTN.matcher(address);
      if (match.matches()) address = match.group(1);
    }
    return address;
  }
}
