package net.anei.cadpage.parsers.NY;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.anei.cadpage.parsers.FieldProgramParser;
import net.anei.cadpage.parsers.MsgInfo.Data;


public class NYNassauCountyKParser extends FieldProgramParser {
  
  private static final Pattern DELIM = Pattern.compile("\n(?:\\*\\* )?");
  
  public NYNassauCountyKParser() {
    super("NASSAU COUNTY", "NY",
          "CALL ( EMPTY NAME ADDRCITY/S6! | ADDR! ) CS:X EMPTY TOA:TIMEDATE! ID! INFO/N+");
  }
  
  @Override
  public String getFilter() {
    return "@firerescuesystems.xohost.com,wantaghpaging@gmail.com,@wantaghfireinfo.com,2083399144";
  }
  
  private static final Pattern SUBJECT_PTN = Pattern.compile("[A-Z]+");
  private static final Pattern SRC_MARKER_PTN = Pattern.compile("[A-Z]+: *\\((.*?)\\) *");
  
  @Override
  protected boolean parseMsg(String subject, String body, Data data) {
    
    // Reject NYNassauCountyH messages
    if (body.startsWith("**")) return false;
    
    if (SUBJECT_PTN.matcher(subject).matches()) {
      data.strSource = subject;
    }
    else {
      Matcher match = SRC_MARKER_PTN.matcher(body);
      if (match.lookingAt()) {
        data.strSource = match.group(1).trim();
        body = body.substring(match.end());
      }
    }
    
    return parseFields(DELIM.split(body), data);
  }
  
  @Override
  public String getProgram() {
    return "SRC " + super.getProgram();
  }
  
  @Override
  public Field getField(String name) {
    if (name.equals("TIMEDATE")) return new TimeDateField("\\d\\d:\\d\\d \\d\\d-\\d\\d-\\d\\d", true);
    if (name.equals("ID")) return new IdField("\\d{4}-\\d{6}", true);
    return super.getField(name);
  }
}
