package net.anei.cadpage.parsers.TX;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.anei.cadpage.parsers.MsgInfo.Data;
import net.anei.cadpage.parsers.MsgParser;


public class TXDallasCountyAParser extends MsgParser {
  
  private static final Pattern SUBJECT_PTN = Pattern.compile("CAD Message");
  private static final Pattern MASTER
    = Pattern.compile("From(.*?)CAD: +(\\d +Alarm +\\d\\d) +-(.*?):(.*?)\\bX-St\\b(.*?)\\bLoc\\b(.*?)\\bUnits\\b(.*?)\\bBox\\b(.*?)\\bMapsco\\b(.*?)\\bInc +(\\d{10}) +at (\\d\\d/\\d\\d/\\d{4}) +(\\d\\d:\\d\\d:\\d\\d)");
  
  public TXDallasCountyAParser() {
    super("DALLAS COUNTY", "TX");
    setFieldList("SRC PRI CALL ADDR X PLACE UNIT BOX MAP ID DATE TIME");
  }
  
  public String getFilter() {
    return "noreplyCAD@dallascityhall.com";
  }
  
  @Override
  public int getMapFlags() {
    return MAP_FLG_SUPPR_LA;
  }

  @Override
  protected boolean parseMsg(String subject, String body, Data data) {
    Matcher match = SUBJECT_PTN.matcher(subject);
    if (!match.matches()) return false;
    
    match = MASTER.matcher(body);
    if (!match.matches()) return false;
    data.strSource = match.group(1).trim();
    data.strPriority = match.group(2);
    data.strCall = match.group(3).trim();
    parseAddress(match.group(4), data);
    data.strCross = match.group(5).trim();
    data.strPlace = match.group(6).trim();
    data.strUnit = match.group(7).trim();
    data.strBox = match.group(8).trim();
    data.strMap = match.group(9).trim();
    data.strCallId = match.group(10);
    data.strDate = match.group(11);
    data.strTime = match.group(12);
    return true;
  }
//
//  @Override
//  public String adjustMapAddress(String sAddress) {
//    return RR_PTN.matcher(sAddress).replaceAll("RT");
//  }
//  private static final Pattern RR_PTN = Pattern.compile("\\bRR\\b");
}
