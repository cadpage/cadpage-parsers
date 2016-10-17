package net.anei.cadpage.parsers.MO;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.anei.cadpage.parsers.FieldProgramParser;
import net.anei.cadpage.parsers.MsgInfo.Data;

public class MOJeffersonCityParser extends FieldProgramParser {
 
  public MOJeffersonCityParser() {
    super("JEFFERSON CITY", "MO",
          "CALL UNIT ADDR/S6! X INFO+");
    setupSaintNames("LOUIS");
    removeWords("X");
  }
  
  @Override
  public String getFilter() {
    return "paging@jeffcitymo.org";
  }
  
  @Override
  public int getMapFlags() {
    return MAP_FLG_PREFER_GPS | MAP_FLG_SUPPR_DIRO;
  }

  private static final Pattern TRAIL_GPS_PTN = Pattern.compile(" +[-+]?\\d{2,3}\\.\\d+ ++[-+]?\\d{2,3}\\.\\d+");
  private static final Pattern TRAIL_OPERATOR_PTN = Pattern.compile(" +[a-z]+$");
  private static final Pattern TRAIL_TIME_PTN = Pattern.compile(" +(\\d\\d:\\d\\d)$");

  @Override
  public boolean parseMsg(String subject, String body, Data data) {
    
    if (!subject.equals("DONOTREPLY")) return false;
    
    Matcher match = TRAIL_GPS_PTN.matcher(body);
    if (match.find()) {
      setGPSLoc(match.group(), data);
      body = body.substring(0,match.start());
    }
    
    match = TRAIL_OPERATOR_PTN.matcher(body);
    if (match.find()) body = body.substring(0,match.start());
    
    match = TRAIL_TIME_PTN.matcher(body);
    if (match.find()) {
      data.strTime = match.group(1);
      body = body.substring(0,match.start());
    }
    
    return parseFields(body.split("\n"), data);
  }
  
  @Override
  public String getProgram() {
    return super.getProgram() + " TIME GPS";
  }
}
