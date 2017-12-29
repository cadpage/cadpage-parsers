package net.anei.cadpage.parsers.AL;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.anei.cadpage.parsers.MsgInfo.Data;
import net.anei.cadpage.parsers.MsgParser;

public class ALJeffersonCountyKParser extends MsgParser {
  
  public ALJeffersonCountyKParser() {
    super("JEFFERSON COUNTY", "AL");
    setFieldList("CODE CALL ADDR APT PHONE X");
  }
  
  @Override
  public String getFilter() {
    return "911-CENTER@yourdomain.com";
  }
  
  private static final Pattern CODE_CALL_PTN = Pattern.compile("(\\d\\d|MED) {4,}(.+)");
  private static final Pattern ADDR_PHONE_PTN = Pattern.compile("(.*?) {10}(\\d+)");
  
  @Override
  protected boolean parseMsg(String subject, String body, Data data) {
    Matcher match = CODE_CALL_PTN.matcher(subject);
    if (!match.matches()) return false;
    data.strCode = match.group(1);
    data.strCall = match.group(2);
    
    String[] lines = body.split("\n");
    if (lines.length > 2) return false;
    String addr = lines[0];
    if (lines.length > 1) data.strCross = lines[1];
    
    match = ADDR_PHONE_PTN.matcher(addr);
    if (match.matches()) {
      addr = match.group(1);
      data.strPhone = match.group(2);
    }
    
    parseAddress(addr, data);
    
    return true;
  }

}
