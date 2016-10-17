package net.anei.cadpage.parsers.IN;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.anei.cadpage.parsers.MsgInfo.Data;
import net.anei.cadpage.parsers.MsgParser;

/**
 * Henry County, IN
 */
public class INHenryCountyParser extends MsgParser {
  
  private static final Pattern MASTER1 = Pattern.compile("([- A-Z0-9]+)\\|([^:\n]+):([^\\|\n]*)\\|(.*)");
  private static final Pattern MASTER2 = Pattern.compile("([- A-Z0-9]+)@([^\\*\n]+)\\*([^\\|\n]*)\\|(.*)");
  
  public INHenryCountyParser() {
    super("HENRY COUNTY", "IN");
    setFieldList("SRC CALL ADDR APT CITY CALL INFO");
  }
  
  @Override
  public String getFilter() {
    return "hcradio@emgsvcs.net";
  }
  
  @Override
  public boolean parseMsg(String subject, String body, Data data) {
    if (!subject.startsWith("DISPATCH:")) return false;
    data.strSource = subject.substring(9).trim();
    
    int pt = body.indexOf('\n');
    if (pt >= 0) body = body.substring(0,pt).trim();
    
    Matcher match = MASTER1.matcher(body);
    if (!match.matches()) match = MASTER2.matcher(body);
    if (!match.matches()) return false;
    data.strCall = match.group(1).trim();
    parseAddress(match.group(2).trim(), data);
    data.strCity = match.group(3).trim();
    String call = match.group(4).trim();
    if (call.length() == 0) data.strCall = "ALERT";
    else if (call.length() <= 40) data.strCall = call;
    else data.strSupp = call;
    
    return true;
  }
  
  @Override
  public String getProgram() {
    return super.getProgram().replace("CITY", "CITY ST");
  }
}
