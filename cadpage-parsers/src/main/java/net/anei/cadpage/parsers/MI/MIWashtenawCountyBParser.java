package net.anei.cadpage.parsers.MI;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.anei.cadpage.parsers.MsgInfo.Data;
import net.anei.cadpage.parsers.MsgInfo.MsgType;
import net.anei.cadpage.parsers.MsgParser;

public class MIWashtenawCountyBParser extends MsgParser {
  
  public MIWashtenawCountyBParser() {
    super("WASHTENAW COUNTY", "MI");
    setFieldList("ID PRI CALL ADDR APT CITY");
  }
  
  @Override
  public String getFilter() {
    return "noreply@emergenthealth.org";
  }
  
  private static final Pattern SUBJECT_PTN = Pattern.compile("(New Incident|Update to Incident|Incident Completed) - (\\d+)");
  private static final Pattern MASTER = Pattern.compile("New Incident:\n(\\S+) - (.*?) at (.*?)(?:, (.*?))?(?: (\\d{5}))?");
  
  @Override
  protected boolean parseMsg(String subject, String body, Data data) {
    Matcher match = SUBJECT_PTN.matcher(subject);
    if (!match.matches()) return false;
    String type = match.group(1);
    data.strCallId = match.group(2);
    
    match = MASTER.matcher(body);
    if (!match.matches()) return false;
    data.strPriority = match.group(1);
    data.strCall = match.group(2);
    parseAddress(match.group(3).trim(), data);
    data.strCity = getOptGroup(match.group(4));
    if (data.strCity.length() == 0) data.strCity = getOptGroup(match.group(5));
    
    if (type.equals("Incident Completed")) {
      data.msgType = MsgType.RUN_REPORT;
    } else if (type.equals("Update to Incident")) {
      data.strCall = append("(UPDATE)", " ", data.strCall);
    }
    return true;
  }

}
