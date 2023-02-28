package net.anei.cadpage.parsers.WV;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.anei.cadpage.parsers.FieldProgramParser;
import net.anei.cadpage.parsers.MsgInfo.Data;

public class WVBooneCountyCParser extends FieldProgramParser {
  
  public WVBooneCountyCParser() {
    super("BOONE COUNTY", "WV", 
          "Incident_Type:CALL! Address:ADDRCITYST! END");
  }
  
  @Override
  public String getFilter() {
    return "zuercher@boonewv.com";
  }
  
  private static final Pattern SUBJECT_PTN = Pattern.compile("CFS - .* - #(CFS\\d+)");
  
  @Override
  protected boolean parseMsg(String subject, String body, Data data) {
    Matcher match = SUBJECT_PTN.matcher(subject);
    if (!match.matches()) return false;
    data.strCallId = match.group(1);
    
    return parseFields(body.split("\n"), data);
  }
  
  @Override
  public String getProgram() {
    return "ID " + super.getProgram();
  }

}
