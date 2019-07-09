package net.anei.cadpage.parsers.GA;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.anei.cadpage.parsers.MsgInfo.Data;
import net.anei.cadpage.parsers.MsgParser;

public class GAHenryCountyCParser extends MsgParser {
  
  public GAHenryCountyCParser() {
    super("HENRY COUNTY", "GA");
    setFieldList("CALL DATE TIME ADDR APT CITY UNIT INFO");
  }
  
  @Override
  public String getFilter() {
    return "henry@henry.dapage.net";
  }
  
  private static final Pattern MASTER = Pattern.compile("(.*?)/(\\d\\d?/\\d\\d?/\\d{4}) (\\d\\d:\\d\\d:\\d\\d)/ ([^,/]+?)(?:, *([^/]+))? /([A-Z0-9, ]+)/");
  
  @Override
  protected boolean parseMsg(String subject, String body, Data data) {
    
    if (!subject.equals("HENRY COUNTY FIRE DEPT CHIEFS")) return false;
    
    Matcher match = MASTER.matcher(body);
    if (!match.lookingAt()) return false;
    
    data.strCall = match.group(1).trim();
    data.strDate = match.group(2);
    data.strTime = match.group(3);
    parseAddress(match.group(4), data);
    String city = match.group(5);
    if (city !=  null) data.strCity = city;
    data.strUnit = match.group(6).trim();
    data.strSupp = body.substring(match.end()).trim();
    return true;
  }

}
