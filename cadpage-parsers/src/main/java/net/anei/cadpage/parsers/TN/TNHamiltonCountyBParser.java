package net.anei.cadpage.parsers.TN;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.anei.cadpage.parsers.MsgInfo.Data;
import net.anei.cadpage.parsers.MsgParser;


public class TNHamiltonCountyBParser extends MsgParser {
  
  private static final Pattern MASTER = Pattern.compile("([^\\(]+) \\(([^\\)]+)\\) (.*?) ([^ ]+) \n : (.*)");
  
  public TNHamiltonCountyBParser() {
    super("HAMILTON COUNTY", "TN");
    setFieldList("PLACE ADDR APT CITY X SRC UNIT CALL");
  }
  
  public String getFilter() {
    return "7774";
  }
  
  @Override
  protected boolean parseMsg(String body, Data data) {
    
    Matcher match = MASTER.matcher(body);
    if (!match.matches()) return false;
    String addr =  match.group(1).trim();
    String cross =  match.group(2).trim();
    if (addr.startsWith("@")) {
      data.strPlace = addr.substring(1).trim();
      addr = cross;
      cross = "";
    }
    int pt = addr.lastIndexOf(',');
    if (pt < 0) return false;
    parseAddress(addr.substring(0,pt).trim(), data);
    data.strCity = addr.substring(pt+1).trim();
    data.strCross = cross;
    data.strSource = match.group(3).trim();
    data.strUnit = match.group(4);
    data.strCall = match.group(5).trim();
    return true;
  }
}
