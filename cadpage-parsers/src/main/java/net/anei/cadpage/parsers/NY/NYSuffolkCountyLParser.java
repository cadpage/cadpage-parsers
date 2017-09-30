package net.anei.cadpage.parsers.NY;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.anei.cadpage.parsers.MsgInfo.Data;
import net.anei.cadpage.parsers.MsgParser;

public class NYSuffolkCountyLParser extends MsgParser {
  
  public NYSuffolkCountyLParser() {
    super("SUFFOLK COUNTY", "NY");
    setFieldList("TIME CALL ADDR APT CITY INFO");
  }
  
  @Override
  public String getFilter() {
    return "alertpage@alertpage.net";
  }
  
  private static final Pattern MASTER = Pattern.compile("(\\d\\d:\\d\\d): ([-/ A-Za-z0-9]*?); (.*?), ([ A-Z]+?) \\((.*)\\)");
  
  @Override
  protected boolean parseMsg(String subject, String body, Data data) {
    if (!subject.startsWith("Dispatches from Suffolk County Fire,")) return false;
    
    int pt = body.indexOf('\n');
    if (pt >= 0) body = body.substring(0,pt).trim();
    
    Matcher match = MASTER.matcher(body);
    if (!match.matches()) return false;
    data.strTime = match.group(1);
    data.strCall = match.group(2).trim();
    if (data.strCall.length() == 0) data.strCall = "ALERT";
    parseAddress(match.group(3).trim(), data);
    data.strCity = match.group(4).trim();
    data.strSupp = match.group(5);
    return true;
  }

}
