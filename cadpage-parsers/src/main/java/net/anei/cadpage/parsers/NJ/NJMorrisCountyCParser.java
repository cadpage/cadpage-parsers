package net.anei.cadpage.parsers.NJ;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.anei.cadpage.parsers.MsgParser;
import net.anei.cadpage.parsers.MsgInfo.Data;

public class NJMorrisCountyCParser extends MsgParser {
  
  public NJMorrisCountyCParser() {
    super("MORRIS COUNTY", "NJ");
    setFieldList("CALL UNIT ID ADDR APT CITY");
  }
  
  private static final Pattern MASTER = Pattern.compile("([A-Z0-9]+) {2,}respond for call #(\\d{2}-\\d+) {2,}at (.*?) {2,}in (.*)");
  
  protected boolean parseMsg(String subject, String body, Data data) {
    
    if (!subject.equals("CAD Page")) return false;
    
    Matcher match = MASTER.matcher(body);
    if (!match.matches()) return false;
    
    data.strCall = "ALERT";
    data.strUnit = match.group(1);
    data.strCallId = match.group(2);
    parseAddress(match.group(3).trim(), data);
    data.strCity = match.group(4);
    return true;
  }

}
