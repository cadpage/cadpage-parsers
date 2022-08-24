package net.anei.cadpage.parsers.MO;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.anei.cadpage.parsers.FieldProgramParser;
import net.anei.cadpage.parsers.MsgInfo.Data;


public class MOIronCountyParser extends FieldProgramParser {
  
  public MOIronCountyParser() {
    super("IRON COUNTY", "MO", 
          "ADDR ( MapRegions:MAP! CrossStreets:X! Lat/Long:GPS! UNIT CALL Call_Number:ID! Caller:NAME! Dispatch:DATETIME! INFO/R INFO/N+ " +
               "| CALL! ( Primary_Incident:ID! Call_Number:ID/L! UNIT Dispatch:DATETIME! INFO/R INFO/N+ " + 
                       "| Description:INFO! INFO/N+ Dispatch:DATETIME! UNIT! Call_Number:ID Caller:NAME END " + 
                       ") " +
               ")");         
  }
  
  @Override
  public String getFilter() {
    return "dispatch@wccd911.org";
  }
  
  private static final Pattern DELIM = Pattern.compile("\n| +(?=(?:Call Number|CrossStreets|Lat/Long|Respond):)");
  
  @Override
  protected boolean parseMsg(String subject, String body, Data data) {
    if (!subject.equals("CAD CALL")  && !subject.equals("WCCD Call Report")) return false;
    body = body.replace(" Call Number:", "\nCall Number:").replace(" Respond:", "\nRespond:");
    return parseFields(DELIM.split(body), data);
  }
  
  @Override
  public Field getField(String name) {
    if (name.equals("DATETIME")) return new MyDateTimeField();
    return super.getField(name);
  }
  
  private static final Pattern DATE_TIME_PTN = Pattern.compile("(?:(\\d\\d?/\\d\\d?/\\d{4})|(\\d{4}-\\d\\d-\\d\\d)) (\\d\\d?:\\d\\d:\\d\\d)( [AP]M)?");
  private static final DateFormat TIME_FMT = new SimpleDateFormat("hh:mm:ss aa");
  private class MyDateTimeField extends DateTimeField {
    
    @Override
    public void parse(String field, Data data) {
      int pt = field.indexOf(" Dispatch:");
      if (pt >= 0) field = field.substring(0,pt).trim();
      Matcher match = DATE_TIME_PTN.matcher(field);
      if (!match.matches()) abort();
      String date = match.group(1);
      if (date == null) {
        date = match.group(2).replace('-', '/');
        date = date.substring(5) + '/' + date.substring(0,4);
      }
      data.strDate = date;
      String time = match.group(3);
      String aa = match.group(4);
      if (aa != null) {
        setTime(TIME_FMT, time + aa, data);
      } else {
        data.strTime = time;
      }
    }
  }
}
