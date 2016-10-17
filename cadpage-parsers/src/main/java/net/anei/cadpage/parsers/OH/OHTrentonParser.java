package net.anei.cadpage.parsers.OH;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.anei.cadpage.parsers.MsgInfo.Data;
import net.anei.cadpage.parsers.dispatch.DispatchEmergitechParser;


public class OHTrentonParser extends DispatchEmergitechParser {
  
  private static final Pattern DATE_TIME_PTN = Pattern.compile("(.*?) +(?:-(?:\\d+|TRAIN))?\\((\\d\\d?/\\d\\d?/\\d{4}) (\\d\\d?:\\d\\d:\\d\\d [AP]M)\\)");
  private static final DateFormat TIME_FMT = new SimpleDateFormat("hh:mm:ss aa"); 
  
  public OHTrentonParser() {
    super("", CITY_LIST, "TRENTON", "OH");
  }

  @Override
  public String getFilter() {
    return "paging@ci.trenton.oh.us,@cityoftrenton.com";
  }
  
  
  @Override
  public boolean parseMsg(String subject, String body, Data data) {
    
    String[] subflds = subject.split("\\|");
    if (subflds.length != 2 || !subflds[0].equals("Trenton Paging System")) return false;
    if (!body.startsWith("-")) return false;
    body = '[' + subflds[1] + ']' + body;
    
    Matcher match = DATE_TIME_PTN.matcher(body);
    if (match.matches()) {
      body = match.group(1).trim();
      data.strDate = match.group(2);
      setTime(TIME_FMT, match.group(3), data);
    }
    return super.parseMsg(body, data);
  }
  
  @Override
  public String getProgram() {
    return super.getProgram() + " DATE TIME";
  }

  private static final String[] CITY_LIST = new String[]{
    "MIDDLETOWN",
    "TRENTON"
  };
}
