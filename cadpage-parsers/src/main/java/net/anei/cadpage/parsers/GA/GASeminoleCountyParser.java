package net.anei.cadpage.parsers.GA;

import net.anei.cadpage.parsers.MsgInfo.Data;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.anei.cadpage.parsers.SmartAddressParser;

public class GASeminoleCountyParser extends SmartAddressParser {
  
  public GASeminoleCountyParser() {
    super(CITY_LIST, "SEMINOLE COUNTY", "GA");
    setFieldList("ID CODE DATE TIME ADDR APT CITY CALL");
  }
  
  @Override
  public String getFilter() {
    return "seminolega@ez911mail.com";
  }
  
  private static final Pattern MASTER = Pattern.compile("(?:([-A-Z0-9]+) )?(\\d\\d?/\\d\\d?/\\d{4}) (\\d\\d?:\\d\\d:\\d\\d) {2,}(.*)");
  
  @Override
  protected boolean parseMsg(String subject, String body, Data data) {
    
    if (!subject.startsWith("Disp: *")) return false;
    data.strCallId = subject.substring(7).trim();
    
    int pt = body.indexOf("\nFrom :");
    if (pt < 0) return false;
    body = body.substring(0,pt).trim();
    
    Matcher match = MASTER.matcher(body);
    if (!match.matches()) return false;
    data.strCode = getOptGroup(match.group(1));
    data.strDate = match.group(2);
    data.strTime = match.group(3);
    parseAddress(StartType.START_ADDR, match.group(4), data);
    data.strCall = getLeft();
    return (data.strCall.length() > 0);
  }
  
  private static final String[] CITY_LIST = new String[]{
      "DESSER",
      "DONALSONVILLE",
      "IRON CITY",
      "LITTLE HOPE",
      "REYNOLDSVILLE"
  };

}
