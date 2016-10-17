package net.anei.cadpage.parsers.TX;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.anei.cadpage.parsers.MsgInfo.Data;
import net.anei.cadpage.parsers.MsgParser;

public class TXFlightVectorParser extends MsgParser {
  public TXFlightVectorParser() {
    super("", "TX");
    setFieldList("ID CALL UNIT PLACE ADDR DATE TIME");
  }
  
  @Override
  public String getFilter() {
    return "pager@flightvector.com";
  }

  @Override
  public String getLocName() {
    return "Flight Vector, TX";
  }
  
  // Pattern strings
  // Non-grouping
  private static final String CALL_ID_PATTERN_STRING
    = "\\d{2}\\-\\d{4}";
  private static final String UNIT_PATTERN_STRING
    = "AirLIFE +\\d{1,2}";
  private static final String RANGE_AZIMUTH_PATTERN_STRING
    = "(?:\\d{3}M +\\d{1,3}\\.\\d{2}nm|\\d{2}\\.\\d{1,3}nm +\\d{3}M)";
  // Grouping
  private static final String GPS_PATTERN_STRING
    = "\\[([^]]+)\\]";
  private static final String NON_GREEDY_FIELD_PATTERN_STRING
    = "(.*?)";
  private static final String DATE_TIME_PATTERN_STRING
    = "(\\d{2}[A-Z][a-z]{2}\\d{4}@\\d{2}\\:\\d{2})";
  private static final Pattern MASTER
    = Pattern.compile("\\s*("+CALL_ID_PATTERN_STRING+"|"+UNIT_PATTERN_STRING+")"
+                     "\\s*(Scene|Interhospital|Relocation +Flight)?"
+                     "\\s*("+UNIT_PATTERN_STRING+"|"+CALL_ID_PATTERN_STRING+")"
+                     "\\s*((?:Scene +)?L(?:AUNCH|aunch)|STANDBY +ON +SCENE)?"
+                     "\\s*("+RANGE_AZIMUTH_PATTERN_STRING+")"
+                     "\\s*"+NON_GREEDY_FIELD_PATTERN_STRING
+                     "\\s*"+GPS_PATTERN_STRING
+                     "\\s*"+NON_GREEDY_FIELD_PATTERN_STRING
+                     "\\s*"+DATE_TIME_PATTERN_STRING);
  private static final DateFormat MY_DATE_FORMAT = new SimpleDateFormat("ddMMMyyyy@hh:mm");
  @Override
  protected boolean parseMsg(String body, Data data) {
    Matcher m = MASTER.matcher(body);
    if (m.matches()) {
      fillIdOrUnit(m.group(1), data);
      data.strCall = getOptGroup(m.group(2));
      fillIdOrUnit(m.group(3), data);
      data.strCall = append(data.strCall, " ", getOptGroup(m.group(4)));
      data.strPlace = m.group(5);
      appendToPlace(m.group(6), data);
      data.strAddress = m.group(7);
      appendToPlace(m.group(8), data);
      setDateTime(MY_DATE_FORMAT, m.group(9), data);
      
      return true;
    }
    
    return false;
  }
  
  private void fillIdOrUnit(String field, Data data) {
    if (field.contains("AirLIFE"))
      data.strUnit = field;
    else
      data.strCallId = field;
  }
  
  private void appendToPlace(String field, Data data) {
    data.strPlace = append(data.strPlace, " - ", field);
  }
}
