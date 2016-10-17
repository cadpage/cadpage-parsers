package net.anei.cadpage.parsers.CA;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.anei.cadpage.parsers.MsgInfo.Data;
import net.anei.cadpage.parsers.MsgParser;

public class CASanDiegoCountyDParser extends MsgParser {
  
  public CASanDiegoCountyDParser() {
    super("SAN DIEGO COUNTY", "CA");
    setFieldList("ID ADDR APT CITY CH MAP CALL UNIT INFO");
  }
  
  @Override
  public String getFilter() {
    return "mvucad@fire.ca.gov";
  }
  
  @Override
  public int getMapFlags() {
    return MAP_FLG_SUPPR_LA;
  }
  
  private static final Pattern MASTER = Pattern.compile("Inc# (\\d{6}): ([^,;]+)(?:,([A-Z_]+))? ; (Cmd: .*?; Tac: .*?): :Map +(\\S*?): (.*)");
  
  @Override
  protected boolean parseMsg(String subject, String body, Data data) {
    if (!subject.equals("CAD Page")) return false;
    
    Matcher match = MASTER.matcher(body);
    if (!match.matches()) return false;
    data.strCallId = match.group(1);
    parseAddress(match.group(2).trim(), data);
    data.strCity = getOptGroup(match.group(3)).replace('_', ' ').trim();
    data.strChannel = match.group(4).trim();
    data.strMap = match.group(5);
    
    String[] parts = match.group(6).trim().split(":");
    for (int ndx = 0; ndx < parts.length; ndx++) {
      String fld = parts[ndx].trim();
      switch (ndx) {
      case 0:
      case 1:
        data.strCall = append(data.strCall, " - ", fld);
        break;
        
      case 2:
        data.strUnit = fld;
        break;
      
      default:
        data.strSupp = append(data.strSupp, ": ", fld);
      }
    }
    return true;
  }

}
