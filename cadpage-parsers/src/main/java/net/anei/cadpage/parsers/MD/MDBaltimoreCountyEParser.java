package net.anei.cadpage.parsers.MD;

import net.anei.cadpage.parsers.MsgInfo.Data;
import net.anei.cadpage.parsers.MsgInfo.MsgType;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.anei.cadpage.parsers.MsgParser;

public class MDBaltimoreCountyEParser extends MsgParser {
  
  public MDBaltimoreCountyEParser() {
    super("BALTIMORE COUNTY", "MD");
    setFieldList("MAP ID UNIT ADDR APT GPS INFO CALL");
  }
  
  @Override
  public String getFilter() {
    return "cadsys@baltimorecountmd.gov";
  }
  
  private static final Pattern MASTER = 
      Pattern.compile("CodeMessaging Message: b (\\d{3}-\\d{2}) +cc# (F\\d{4}-\\d{9}) +unit (\\S+) l (.*?) +#J? g (\\S+) (.*)? title (.*)");
  
  @Override
  protected boolean parseMsg(String subject, String body, Data data) {
    if (!subject.equals("CodeMessaging Unit Cleared")) return false;
    
    body = body.replace('\t', ' ');
    Matcher match = MASTER.matcher(body);
    if (!match.matches()) return false;
    data.msgType = MsgType.RUN_REPORT;
    data.strMap = match.group(1);
    data.strCallId = match.group(2);
    data.strUnit = match.group(3);
    parseAddress(match.group(4).trim(), data);
    setGPSLoc(match.group(5).trim(), data);
    data.strSupp = match.group(6).trim();
    data.strCall = match.group(7);
    return true;
  }
}
