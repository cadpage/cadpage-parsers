package net.anei.cadpage.parsers.NY;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.anei.cadpage.parsers.SmartAddressParser;
import net.anei.cadpage.parsers.MsgInfo.Data;



public class NYDixHillsParser extends SmartAddressParser {
  
  private static final Pattern MARKER = Pattern.compile("\\b(\\d{4}-\\d{6}) (\\d\\d:\\d\\d) \\*\\*\\* (.*?) \\*\\*\\* ");
  
  public NYDixHillsParser() {
    super("DIX HILLS", "NY");
    setFieldList("ID TIME CALL ADDR NAME INFO");
  }
  
  @Override
  public String getFilter() {
    return "paging@dixhillsfd.xohost.com,@firerescuesystems.xohost.com";
  }

  @Override
  protected boolean parseMsg(String body, Data data) {
    
    Matcher match = MARKER.matcher(body);
    if (!match.find()) return false;
    data.strCallId = match.group(1);
    data.strTime = match.group(2);
    data.strCall = append(match.group(3), " - ", body.substring(0,match.start()).trim());
    body = body.substring(match.end()).trim();
    body = cleanup(body);
    parseAddress(StartType.START_ADDR, body, data);
    String sExtra = getLeft();
    int pt = sExtra.indexOf(" Dix Hills ");
    if (pt >= 0) {
      data.strName = sExtra.substring(0,pt).trim();
      sExtra = sExtra.substring(pt+11).trim();
    }
    data.strSupp = sExtra;
    return true;
  }
    
    // For some totally bizarre reason, the first street of an intersection is
    // doubled and has to be cleaned up
  private String cleanup(String body) {
    
    int pt = body.indexOf(" & ");
    if (pt < 0) return body;
    
    int pta = body.indexOf(' ');
    String first = body.substring(0, pta+1);
    int ptb = body.indexOf(" " + first);
    if (ptb < 0 || ptb > pt) return body;
    return body.substring(0,ptb) + body.substring(pt);
  }
}
