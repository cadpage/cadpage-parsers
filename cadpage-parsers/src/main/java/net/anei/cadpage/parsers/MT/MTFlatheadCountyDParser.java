package net.anei.cadpage.parsers.MT;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.anei.cadpage.parsers.FieldProgramParser;
import net.anei.cadpage.parsers.MsgInfo.Data;

public class MTFlatheadCountyDParser extends FieldProgramParser {
  
  public MTFlatheadCountyDParser() {
    super("FLATHEAD COUNTY", "MT", 
          "Call_Type:CALL? Address:ADDRCITY! Common_Name:PLACE! City:CITY! Closest_Intersection:X! Additional_Location_Info:INFO! " + 
              "Fire_Quadrant:MAP! EMS_District:MAP/L!  LAT/LONG:GPS! CFS_Number:SKIP! Caller's_Name:NAME! Caller's_Phone_Number:PHONE! " +
              "Call_Date/Time:DATETIME! Primary_Incident:ID! Dispatched_Units:UNIT! Incident_Numbers:SKIP! Narrative:INFO/N+");
  }
  
  @Override
  public String getFilter() {
    return "911@flatheadoes.mt.gov";
  }
  
  @Override
  public int getMapFlags() {
    return MAP_FLG_PREFER_GPS;
  }
  
  private static final Pattern MARKER = Pattern.compile("(?:SMS Notification 2017|Paging Group Notification 2019)\n\n");
  
  @Override
  protected boolean parseMsg(String subject, String body, Data data) {
    if (!subject.equals("Dispatch Information")) return false;
    Matcher match = MARKER.matcher(body);
    if (!match.lookingAt()) return false;
    body = body.substring(match.end()).trim();
    return parseFields(body.split("\n"), data);
  }
  
  @Override
  public Field getField(String name) {
    if (name.equals("NAME")) return new NameField("(.*?)[ ,]*");
    if (name.equals("DATETIME")) return new MyDateTimeField();
    return super.getField(name);
  }
  
  private static final Pattern DATE_TIME_PTN = Pattern.compile("(\\d\\d?/\\d\\d?/\\d{4}) (\\d\\d?:\\d\\d:\\d\\d(?: [AP]M)?)");
  private static final DateFormat TIME_FMT = new SimpleDateFormat("hh:mm:ss aa");
  private class MyDateTimeField extends DateTimeField {
    @Override
    public void parse(String field, Data data) {
      Matcher match = DATE_TIME_PTN.matcher(field);
      if (!match.matches()) abort();
      data.strDate = match.group(1);
      String time = match.group(2);
      if (time.endsWith("M")) {
        setTime(TIME_FMT, time, data);
      } else {
        data.strTime = time;
      }
    }
  }
}
