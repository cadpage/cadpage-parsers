package net.anei.cadpage.parsers.MI;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.anei.cadpage.parsers.MsgInfo.Data;
import net.anei.cadpage.parsers.MsgParser;

public class MIOaklandCountyDParser extends MsgParser {
  
  public MIOaklandCountyDParser() {
    super("OAKLAND COUNTY", "MI");
    setFieldList("CALL TIME ID ADDR APT INFO");
  }
  
  @Override
  public String getFilter() {
    return "8557345699,71730";
  }
  
  private static final Pattern MASTER = 
      Pattern.compile("T:(\\S+) +T:(\\d\\d?)(\\d\\d) +R:(\\d+) +A:(.*?) - https://\\S+ +D: https://\\S+ *(.*)");
  
  @Override
  protected boolean parseMsg(String subject, String body, Data data) {
    if (!subject.equals("IRIS")) return false;
    Matcher match = MASTER.matcher(body);
    if (!match.matches()) return false;
    
    data.strCall = match.group(1);
    data.strTime = match.group(2)+':'+match.group(3);
    data.strCallId = match.group(4);
    parseAddress(match.group(5).trim(), data);
    data.strSupp = match.group(6);
    return true;
  }

}
