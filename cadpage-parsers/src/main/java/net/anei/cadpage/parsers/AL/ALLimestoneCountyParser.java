package net.anei.cadpage.parsers.AL;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.anei.cadpage.parsers.MsgInfo.Data;
import net.anei.cadpage.parsers.MsgParser;

public class ALLimestoneCountyParser extends MsgParser {
  
  private static final Pattern MASTER = Pattern.compile("([^,;]*?)(?:,([ A-Z]+)(?:, *([A-Z]{2})(?: \\d{5})?)?)? +((?:ALHA|[A-Z]+FD)(?:; [A-Z]+)*)$");
  
  public ALLimestoneCountyParser() {
    super("LIMESTONE COUNTY", "AL");
    setFieldList("CALL ADDR APT CITY ST UNIT");
  }
  
  @Override
  public String getFilter() {
    return "911dispatch@alc911.org";
  }
  
  @Override
  public boolean parseMsg(String subject, String body, Data data) {
    
    data.strCall = subject;
    if (data.strCall.length() == 0) data.strCall = "ALERT";
    
    Matcher match = MASTER.matcher(body);
    if (!match.matches()) return false;
    parseAddress(match.group(1).trim(), data);
    data.strCity = getOptGroup(match.group(2));
    data.strState = getOptGroup(match.group(3));
    data.strUnit = match.group(4).replace(";", "");
    return true;
  }
}
