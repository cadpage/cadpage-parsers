package net.anei.cadpage.parsers.MD;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.anei.cadpage.parsers.FieldProgramParser;
import net.anei.cadpage.parsers.MsgInfo.Data;

public class MDStatePoliceBParser extends FieldProgramParser {

  public MDStatePoliceBParser() {
    super("", "MD",
          "UNIT_AGENCY_DESC%EMPTY! DISPATCH_NUMBER%EMPTY! CALL_DATE%EMPTY! CALL_TYPE_DESC%EMPTY! INCIDENT_LOCATION%EMPTY! " +
              "COMMON_PLACE_NAME%EMPTY! COUNTY%EMPTY! INC_REPORT_NUMBERS%EMPTY! INCIDENT_NOTE%EMPTY! OFFICER_AGENCY_DESC%EMPTY! " +
              "SRC ID DATETIME CALL ADDRCITYST PLACE UNIT ID/L! INFO! SKIP! END");
  }

  @Override
  public String getFilter() {
    return "msp.cadteam@maryland.gov";
  }

  @Override
  public String getLocName() {
    return "Maryland State Police";
  }

  @Override
  protected boolean parseMsg(String body, Data data) {
    body = body.replace("OFFICER AGENCY DESC\n", "OFFICER AGENCY DESC,");
    body = body.replace('\n', ' ');
    String[] flds = splitFields(body);
    if (flds == null) return false;
    return parseFields(flds, data);
  }

  private String[] splitFields(String body) {
    List<String> result = new ArrayList<>();
    StringBuilder sb = null;
    for (String token : body.split(",")) {
      token = token.trim();
      if (token.startsWith("\"")) {
        if (sb != null) return null;
        sb = new StringBuilder(token.substring(1));
      } else if (sb != null) {
        sb.append(", ");
        sb.append(token);
        if (token.endsWith("\"")) {
          sb.setLength(sb.length()-1);
          result.add(sb.toString());
          sb = null;
        }
      } else {
        result.add(token);
      }
    }
    if (sb != null) return null;
    return result.toArray(new String[0]);
  }

  @Override
  public Field getField(String name) {
    if (name.equals("DATETIME")) return new MyDateTimeField();
    if (name.equals("ADDRCITYST")) return new MyAddressCityStateField();
    if (name.equals("INFO")) return new MyInfoField();
    return super.getField(name);
  }

  private static final Pattern DATE_TIME_PTN = Pattern.compile("(\\d\\d/\\d\\d/\\d{4}) +(\\d\\d?:\\d\\d:\\d\\d [AP]M)");
  private static final DateFormat TIME_FMT = new SimpleDateFormat("hh:mm:ss aa");
  private class MyDateTimeField extends DateTimeField {
    @Override
    public void parse(String field, Data data) {
      Matcher match = DATE_TIME_PTN.matcher(field);
      if (!match.matches()) abort();
      data.strDate = match.group(1);
      setTime(TIME_FMT, match.group(2), data);
    }
  }

  private class MyAddressCityStateField extends AddressCityStateField {
    @Override
    public void parse(String field, Data data) {
      int pt = field.lastIndexOf(',');
      if (pt < 0) abort();
      String county = field.substring(pt+1).trim();
      field = field.substring(0,pt).trim();
      super.parse(field, data);
      if (data.strCity.isEmpty() && !county.isEmpty()) {
        data.strCity = county + " County";
      }
    }
  }

  private static final Pattern INFO_BRK_PTN = Pattern.compile(" *\\b\\d\\d/\\d\\d/\\d{4} \\d\\d:\\d\\d:\\d\\d \\S+: *");
  private class MyInfoField extends InfoField {
    @Override
    public void parse(String field, Data data) {
      if (field.isEmpty()) return;
      if (!field.startsWith("REMARKS/NARRATIVES:")) abort();
      field = field.substring(19).trim();
      field = INFO_BRK_PTN.matcher(field).replaceAll("\n").trim();
      super.parse(field, data);
    }
  }
}
