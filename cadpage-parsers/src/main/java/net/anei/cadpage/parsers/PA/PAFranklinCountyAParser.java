package net.anei.cadpage.parsers.PA;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.anei.cadpage.parsers.FieldProgramParser;
import net.anei.cadpage.parsers.MsgInfo.Data;
import net.anei.cadpage.parsers.MsgInfo.MsgType;

/**
 * Franklin County, PA
 */
public class PAFranklinCountyAParser extends FieldProgramParser {

  public PAFranklinCountyAParser() {
    super("FRANKLIN COUNTY", "PA",
          "ID:ID! CALL:CALL! DATE:DATETIME! ADDR:ADDR! UNIT:UNIT? INFO:TIMES! ( COMMENTS:INFO! INFO/N+ | ) GPS:GPS!");
  }

  @Override
  public int getMapFlags() {
    return MAP_FLG_PREFER_GPS;
  }

  String times;

  private static final Pattern DELIM = Pattern.compile("\\n| (?=COMMENTS:)");

  @Override
  protected boolean parseMsg(String body, Data data) {
    times = "";
    if (!parseFields(DELIM.split(body), data)) return false;
    data.strSupp = append(data.strSupp, "\n\n", times);
    times = null;
    return true;
  }

  @Override
  public Field getField(String name) {
    if (name.equals("DATETIME")) return new MyDateTimeField();
    if (name.equals("ADDR")) return new MyAddressField();
    if (name.equals("TIMES"))  return new MyTimesField();
    if (name.equals("INFO")) return new MyInfoField();
    return super.getField(name);
  }

  private static final Pattern DATE_TIME_PTN = Pattern.compile("(\\d\\d?/\\d\\d?/\\d{4}), (\\d\\d?:\\d\\d:\\d\\d)");

  private class MyDateTimeField extends DateTimeField {
    @Override
    public void parse(String field, Data data) {
      Matcher match = DATE_TIME_PTN.matcher(field);
      if (!match.matches()) abort();
      data.strDate = match.group(1);
      data.strTime = match.group(2);
    }
  }

  private static final Pattern APT_PTN = Pattern.compile("(?:APT|RM|ROOM|LOT) +(.*)|\\d{1,4}[A-Z]?|[A-Z]", Pattern.CASE_INSENSITIVE);
  private class MyAddressField extends AddressField {
    @Override
    public void parse(String field, Data data) {
      for (String part : field.split(";")) {
        part = part.trim();
        if (data.strAddress.isEmpty()) {
          parseAddress(part, data);
        } else {
          Matcher match = APT_PTN.matcher(part);
          if (match.matches()) {
            String tmp = match.group(1);
            if (tmp != null) part = tmp;
            data.strApt = append(data.strApt, "-", part);
          } else {
            data.strPlace = append(data.strPlace, " - ", part);
          }
        }
      }
    }

    @Override
    public String getFieldNames() {
      return "ADDR PLACE APT";
    }
  }

  private static final Pattern TIMES_BRK_PTN = Pattern.compile("::\\|\\|::");
  private static final Pattern TIMES_PTN = Pattern.compile("(\\d\\d:\\d\\d:\\d\\d) \\d\\d?/\\d\\d?/\\d{4}::([^:]+)::(.*)");
  private class MyTimesField extends InfoField {
    @Override
    public void parse(String field, Data data) {
      for (String part : TIMES_BRK_PTN.split(field)) {
        if (part.isEmpty()) continue;
        Matcher match = TIMES_PTN.matcher(part);
        if (!match.matches()) abort();
        String time = match.group(1);
        String unit = match.group(2);
        String status = match.group(3);
        if (unit.equals("undefined")) unit = "---";
        if (status.equals("CMPLT")) data.msgType = MsgType.RUN_REPORT;
        times = append(times, "\n", String.format("%-8s  %-6s  %s", time, unit, status));
      }
    }
  }

  private static final Pattern INFO_JUNK_PTN = Pattern.compile("\\d\\d?:\\d\\d:\\d\\d \\d\\d?/\\d\\d?/\\d{4} - .*");
  private static final Pattern INFO_GPS_PTN = Pattern.compile(".*Phone: (\\S+), UNC: \\S+, Lat: (\\S+), Long: (\\S+)");
  private class MyInfoField extends InfoField {
    @Override
    public void parse(String field, Data data) {
      if (INFO_JUNK_PTN.matcher(field).matches()) return;
      Matcher match = INFO_GPS_PTN.matcher(field);
      if (match.matches()) {
        data.strPhone = match.group(1);
        setGPSLoc(match.group(2)+','+match.group(3), data);
      }
      else {
        data.strSupp = append(data.strSupp, "\n", field);
      }
    }

    @Override
    public String getFieldNames() {
      return "PHONE GPS INFO";
    }
  }
}
