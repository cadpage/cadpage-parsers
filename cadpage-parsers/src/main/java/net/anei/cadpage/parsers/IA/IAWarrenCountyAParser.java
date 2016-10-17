package net.anei.cadpage.parsers.IA;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.anei.cadpage.parsers.MsgInfo.Data;
import net.anei.cadpage.parsers.dispatch.DispatchA38Parser;



public class IAWarrenCountyAParser extends DispatchA38Parser {
  
  private static final Pattern MISSING_CFS_PTN = Pattern.compile("\\d{4}-\\d+\n");
  
  public IAWarrenCountyAParser() {
    super("WARREN COUNTY", "IA");
  }
  
  @Override
  public String getFilter() {
    return "messaging@iamresponding.com,7127390583";
  }
  
  @Override
  public boolean parseMsg(String subject, String body, Data data) {
    data.strSource = subject;
    
    
    // Fix up some IAR scrambling :(
    Matcher match = MISSING_CFS_PTN.matcher(body);
    if (match.lookingAt()) {
      body =  "CFS#: " + body;
    }
    
    else if (body.startsWith(":")) {
      body = "CFS#: 0000-00000\nCallType" + body;
    }
    
    if (!super.parseMsg(body, data)) return false;
    if (data.strCallId.equals("0000-00000")) data.strCallId = "";
    return true;
  }
  
  @Override
  public String getProgram() {
    return "SRC " + super.getProgram();
  }
}
