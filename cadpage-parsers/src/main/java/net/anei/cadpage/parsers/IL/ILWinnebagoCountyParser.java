package net.anei.cadpage.parsers.IL;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.anei.cadpage.parsers.MsgParser;
import net.anei.cadpage.parsers.MsgInfo.Data;


public class ILWinnebagoCountyParser extends MsgParser {
  
  private static Pattern MARKER = Pattern.compile(": (?:Med|Fire):");
  
  public ILWinnebagoCountyParser() {
    super("WINNEBAGO COUNTY", "IL");
    setFieldList("SRC ADDR APT CALL");
  }
  
  @Override
  public String getFilter() {
    return "hrfdfiresvcrunnotification@gmail.com";
  }
  
  @Override
  protected boolean parseMsg(String subject, String body, Data data) {
    
    // If there is no call description, the message presparsing logic forces
    // what would normally be the subject into the message body.  We have to 
    // reverse this
    if (subject.length() == 0) {
      subject = body;
      body = "";
    }
    
    Matcher match = MARKER.matcher(subject);
    if (!match.find()) return false;
    data.strSource = subject.substring(0, match.start()).trim();
    parseAddress(subject.substring(match.end()).trim(), data);
    data.strCall = body;
    return true;
  }
}
