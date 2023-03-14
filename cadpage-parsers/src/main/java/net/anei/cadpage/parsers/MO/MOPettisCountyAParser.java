package net.anei.cadpage.parsers.MO;


import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.anei.cadpage.parsers.MsgInfo.Data;
import net.anei.cadpage.parsers.MsgInfo.MsgType;
import net.anei.cadpage.parsers.SmartAddressParser;



public class MOPettisCountyAParser extends SmartAddressParser {
  
  private static final Pattern RESPOND_PTN = Pattern.compile("(?:([^ ]+) +)?RESPOND +(?:TO +|FOR +)?(.*?)", Pattern.CASE_INSENSITIVE);
 
  public MOPettisCountyAParser() {
    super("PETTIS COUNTY", "MO");
    setFieldList("UNIT CALL ADDR APT INFO");
  }
  
  @Override
  public String getFilter() {
    return "888777";
  }
  
  @Override
  public boolean parseMsg(String body, Data data) {
    if (!body.startsWith("PETTIS CO SHERIFF: ")) return false;
    body = body.substring(19).trim();
    Matcher match = RESPOND_PTN.matcher(body);
    if (match.matches()) {
      data.strUnit = getOptGroup(match.group(1));
      body = match.group(2);
    }
    Result res = parseAddress(StartType.START_CALL, body);
    if (!res.isValid()) {
      data.msgType = MsgType.GEN_ALERT;
      data.strSupp = body;
      return true;
    }

    res.getData(data);
    String left = res.getLeft();
    if (data.strCall.length() == 0) data.strCall = left;
    else data.strSupp = left;
    return true;
  }
}