package net.anei.cadpage.parsers.NC;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.anei.cadpage.parsers.MsgParser;
import net.anei.cadpage.parsers.MsgInfo.Data;

/**
 * Craven County, NC
 */
public class NCCravenCountyDParser extends MsgParser {
  
  public NCCravenCountyDParser() {
    super("CRAVEN COUNTY", "NC");
    setFieldList("SRC ADDR APT GPS CITY CALL ID INFO");
  }
  
  @Override
  public String getFilter() {
    return "dispatch@cravencountync.gov";
  }
  
  @Override
  public int getMapFlags() {
    return MAP_FLG_PREFER_GPS;
  }
  
  private static final Pattern MASTER = Pattern.compile("(.+?) Location: (.*?) (\\d{2}\\.\\d{6,} -\\d{2}\\.\\d{6,}) (New Bern) (.+?) (?:(\\d{4}-\\d{8}) )?Narrative:\\s*(?s)(.*)");
  private static final Pattern BREAK_PTN = Pattern.compile(" +\n");
  
  @Override
  public boolean parseMsg(String subject, String body, Data data) {
    if (!subject.equals("[DISP]")) return false;
    Matcher match = MASTER.matcher(body);
    if (!match.matches()) return false;
    data.strSource = match.group(1).trim();
    parseAddress(match.group(2).trim(), data);
    setGPSLoc(match.group(3).trim(), data);
    data.strCity = match.group(4);
    data.strCall = match.group(5);
    data.strCallId = getOptGroup(match.group(6));
    data.strSupp = BREAK_PTN.matcher(match.group(7).trim()).replaceAll("\n");
    return true;
  }
}
