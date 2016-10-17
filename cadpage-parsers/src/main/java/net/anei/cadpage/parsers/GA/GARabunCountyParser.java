package net.anei.cadpage.parsers.GA;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.anei.cadpage.parsers.MsgInfo.Data;
import net.anei.cadpage.parsers.MsgParser;


public class GARabunCountyParser extends MsgParser {
  
  private static final Pattern MASTER = Pattern.compile("([A-Z0-9]+) dispatched: (.*) at (.*)");
 
  public GARabunCountyParser() {
    super("RABUN COUNTY", "GA");
    setFieldList("UNIT CALL ADDR APT");
  }
  
  @Override
  public String getFilter() {
    return "noreply@emergencycallworx.com";
  }
  
  @Override
  public boolean parseMsg(String subject, String body, Data data) {
    if (!subject.equals("E911 Run Group Dispatched Notification")) return false;
    Matcher match = MASTER.matcher(body);
    if (!match.matches()) return false;
    data.strUnit = match.group(1);
    data.strCall = match.group(2).trim();
    parseAddress(match.group(3).trim(), data);
    return true;
  }
 }
