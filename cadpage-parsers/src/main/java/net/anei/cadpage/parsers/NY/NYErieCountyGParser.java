package net.anei.cadpage.parsers.NY;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.anei.cadpage.parsers.MsgInfo.Data;
import net.anei.cadpage.parsers.MsgParser;

public class NYErieCountyGParser extends MsgParser {
  
  public NYErieCountyGParser() {
    super("ERIE COUNTY", "NY");
    setFieldList("SRC ADDR APT CALL INFO");
  }
  
  @Override
  public String getFilter() {
    return "messaging@iamresponding.com";
  }
  
  private static final Pattern MASTER = Pattern.compile("([^,\n]+), ([^:\n]+): (.+)");
  
  @Override
  protected boolean parseMsg(String subject, String body, Data data) {
    if (subject.length() == 0) return false;
    data.strSource = subject;
    
    Matcher match = MASTER.matcher(body);
    if (!match.matches()) return false;
    parseAddress(match.group(1).trim(), data);
    data.strCall = match.group(2).trim();
    data.strSupp = match.group(3).trim();
    return true;
  }

}
