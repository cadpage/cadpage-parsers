package net.anei.cadpage.parsers.TX;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.anei.cadpage.parsers.MsgInfo.Data;
import net.anei.cadpage.parsers.MsgParser;


public class TXDecaturParser extends MsgParser {
  
  private static final Pattern MASTER = Pattern.compile("([^,]*), ([^,]*)(?:, (.*))?,  - (.*)");
  
  public TXDecaturParser() {
    super("DECATUR", "TX");
    setFieldList("SRC CALL ADDR APT X UNIT INFO");
  }
  
  public String getFilter() {
    return "messaging@iamresponding.com";
  }
  
  @Override
  public int getMapFlags() {
    return MAP_FLG_SUPPR_LA;
  }
  
  @Override
  protected boolean parseMsg(String subject, String body, Data data) {
    if (!subject.endsWith(" FD")) return false;
    data.strSource = subject;
    
    int pt = body.indexOf("\n\n");
    if (pt >= 0) {
      data.strSupp = body.substring(pt+2).trim();
      body = body.substring(0,pt).trim();
    }
    
    Matcher match = MASTER.matcher(body);
    if (!match.matches()) return false;
    
    data.strCall = match.group(1).trim();
    parseAddress(match.group(2).trim(), data);
    data.strCross = getOptGroup(match.group(3));
    data.strUnit = match.group(4).trim();
    return true;
  }
}
