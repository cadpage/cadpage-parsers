package net.anei.cadpage.parsers.FL;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.anei.cadpage.parsers.MsgInfo.Data;
import net.anei.cadpage.parsers.MsgParser;

public class FLSumterCountyParser extends MsgParser {
  
  public FLSumterCountyParser() {
    super("SUMTER COUNTY", "FL");
  }
  
  @Override
  public String getFilter() {
    return "777";
  }
  
  private static final Pattern MASTER1 = Pattern.compile("([A-Z0-9]+) +Inc#(\\d{8})  (.*?) +Apt:(.*?)City:(.*?) {3,}(.*?)Cross:(.*)");
  private static final Pattern MASTER2 = Pattern.compile("(.*?) {2,}(.*)  BLD/APT: *(.*)");
  
  @Override
  protected boolean parseMsg(String body, Data data) {
    Matcher match = MASTER1.matcher(body);
    if (match.matches()) {
      setFieldList("UNIT ID ADDR APT CITY CALL X");
      data.strUnit = match.group(1);
      data.strCallId = match.group(2);
      parseAddress(match.group(3).trim(), data);
      data.strApt = append(data.strApt, "-", match.group(4).trim());
      data.strCity = match.group(5).trim();
      data.strCall = match.group(6).trim();
      data.strCross = match.group(7).trim();
      return true;
    }
    
    if (body.startsWith("- TAC CHN:")) {
      body = body.substring(10).trim();
      int pt = body.indexOf("- TAC CHN:");
      if (pt >= 0) body = body.substring(0,pt).trim();
      match = MASTER2.matcher(body);
      if (!match.matches()) return false;
      setFieldList("CALL ADDR APT");
      data.strCall = match.group(1).trim();
      parseAddress(match.group(2).trim(), data);
      data.strApt = append(data.strApt, "-", match.group(3).trim());
      return true;
    }
    return false;
  }
}
