package net.anei.cadpage.parsers.MO;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.anei.cadpage.parsers.MsgInfo.Data;
import net.anei.cadpage.parsers.MsgParser;


public class MOCapeGirardeauCountyBParser extends MsgParser {
  
  private static final Pattern MASTER = Pattern.compile("FDTYPE:([- A-Z]+?), LOCATION:(.*?)(?:[\\[,].*)?");
  
  public MOCapeGirardeauCountyBParser() {
    super("CAPE GIRARDEAU COUNTY", "MO");
    setFieldList("CALL ADDR APT");
  }
  
  @Override
  public String getFilter() {
    return "jpd@cityofcapegirardeau.org";
  }
  
  @Override
  protected boolean parseMsg(String body, Data data) {
    Matcher match = MASTER.matcher(body);
    if (!match.matches()) return false;
    data.strCall = match.group(1).trim();
    parseAddress(match.group(2).trim(), data);
    return true;
  }
}
