package net.anei.cadpage.parsers.MN;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.anei.cadpage.parsers.FieldProgramParser;
import net.anei.cadpage.parsers.MsgInfo.Data;

/**
 * Hold for additional user input!!
 **/

public class MNNorthMemorialAmbulanceServiceParser extends FieldProgramParser {
  
  private static final Pattern RUN_REPORT_PTN = Pattern.compile("COMPLETED +([A-Z]{2}) +(\\d+) +.*");
  private static final Pattern MARKER = Pattern.compile("^(?:\\d{9} +)?(P\\d) +([A-Z]{2}\\d+) +");
  private static final Pattern DISPATCHER_PTN = Pattern.compile(" *<[A-Z0-9 ]+>$");
  
  public MNNorthMemorialAmbulanceServiceParser() {
    super("", "MN",
          "( CALL PT:PICKUP! ADDR/SC! COMT:INFO! ) City:CITY! MAP:MAP! X:X");
  }
  
  @Override
  public String getLocName() {
    return "North Memorial Ambulance Service, MN";
  }

  @Override
  protected boolean parseMsg(String body, Data data) {
    
    // Check for run report
    Matcher match = RUN_REPORT_PTN.matcher(body);
    if (match.matches()) {
      data.strCall = "RUN REPORT";
      data.strPlace = body;
      data.strCallId = match.group(1) + match.group(2);
      return true;
    }
    
    // Look for leading priority and ID sequence
    match = MARKER.matcher(body);
    if (!match.find()) return false;
    data.strPriority = match.group(1);
    data.strCallId = match.group(2);
    body = body.substring(match.end());
    
    // Strip off dispatcher name
    match = DISPATCHER_PTN.matcher(body);
    if (match.find()) body =  body.substring(0,match.start());
    
    // and call the main parse method
    return super.parseMsg(body, data);
  }
  
  @Override
  public String getProgram() {
    return "PRI ID " + super.getProgram();
  }
}
