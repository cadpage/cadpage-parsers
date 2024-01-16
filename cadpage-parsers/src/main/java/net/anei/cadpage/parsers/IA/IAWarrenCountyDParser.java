package net.anei.cadpage.parsers.IA;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import java.text.DateFormat;
import java.text.SimpleDateFormat;

import net.anei.cadpage.parsers.FieldProgramParser;
import net.anei.cadpage.parsers.MsgInfo.Data;

public class IAWarrenCountyDParser extends FieldProgramParser {

  public IAWarrenCountyDParser() {
    super("WARREN COUNTY", "IA",
          "Problem_Nature:CALL! Address:ADDR! Loc_Name:PLACE! Bldg:APT! Unit:APT! City:CITY! County:SKIP! Time_of_Call:DATETIME! Assigned_Units:UNIT! Incident_Comments:INFO! Latitude-Longitude:GPS/d! END");
  }

  @Override
  protected boolean parseMsg(String body, Data data) {
    body = body.replace("** / **", "");
    return super.parseMsg(body, data);
  }

  @Override
  public Field getField(String name) {
    if (name.equals("DATETIME")) return new MyDateTimeField();
    if (name.equals("INFO")) return new MyInfoField();
    return super.getField(name);
  }

  private static final Pattern INFO_BRK_PTN = Pattern.compile("(?: *\\[Shared\\])?[ ,]*(\\[\\d{1,2}\\]) (?:\\1 )? *");
  private class MyInfoField extends InfoField {
    @Override
    public void parse(String field, Data data) {
      field = stripFieldEnd(field, ",");
      field = INFO_BRK_PTN.matcher(field).replaceAll("\n").trim();
      field = stripFieldEnd(field, "[Shared]");
      super.parse(field, data);
    }
  }

  private static final Pattern DATE_TIME_PTN = Pattern.compile("(\\d\\d/\\d\\d/\\d{4}) (\\d\\d:\\d\\d:\\d\\d(?: [ap]m)?)");
  private static final DateFormat TIME_FMT = new SimpleDateFormat("hh:mm:ss aa");

  private class MyDateTimeField extends DateTimeField {
    @Override
    public void parse(String field, Data data) {
      if (field.isEmpty()) return;
      Matcher match = DATE_TIME_PTN.matcher(field);
      if (!match.matches()) abort();
      data.strDate = match.group(1);
      String time = match.group(2);
      if (time.endsWith("m")) {
        setTime(TIME_FMT, time, data);
      } else {
        data.strTime = time;
      }
    }
  }
}
