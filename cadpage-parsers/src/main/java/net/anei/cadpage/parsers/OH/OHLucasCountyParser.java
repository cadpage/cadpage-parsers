package net.anei.cadpage.parsers.OH;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.anei.cadpage.parsers.MsgInfo.Data;
import net.anei.cadpage.parsers.MsgParser;
import net.anei.cadpage.parsers.SplitMsgOptions;
import net.anei.cadpage.parsers.SplitMsgOptionsCustom;

public class OHLucasCountyParser extends MsgParser {
  
  public OHLucasCountyParser() {
    super("LUCAS COUNTY", "OH");
    setFieldList("ID ADDR APT CODE CALL GPS X INFO");
  }
  
  @Override
  public SplitMsgOptions getActive911SplitMsgOptions() {
    return new SplitMsgOptionsCustom() {
      @Override public boolean splitBlankIns() { return false; }
      @Override public int splitMinMsg() { return 3; }
      @Override public int splitBreakLength() { return 300; }
      @Override public int splitBreakPad() { return 1; }
    };
  }
  
  @Override
  public int getMapFlags() {
    return MAP_FLG_PREFER_GPS;
  }
  
  private static final Pattern SPLITTER = Pattern.compile("\\[\\d\\]");
  private static final Pattern MASTER = Pattern.compile("([A-Z]{3}\\d{7,8}|\\d{8}-\\d{7}) (.*) ([A-Z]+)-((?:F-)?(?:[A-Z ]+)?[A-Z][a-z].*?)(\\d{8}|0) (\\d{8}|0)(?: (.*))?");
  
  @Override
  public boolean parseMsg(String body, Data data) {
    
    if (body.length() == 299 || body.length() == 300) data.expectMore = true;
    
    String[] parts = SPLITTER.split(body);
    Matcher match = MASTER.matcher(parts[0]);
    if (!match.matches()) return false;
    data.strCallId = match.group(1);
    parseAddress(match.group(2).trim(), data);
    data.strCode = match.group(3);
    data.strCall = match.group(4).trim();
    setGPSLoc(fixGPS(match.group(5)) + ',' + fixGPS(match.group(6)), data);
    data.strCross = getOptGroup(match.group(7));
    
    for (int j = 1; j<parts.length; j++) {
      data.strSupp = append(data.strSupp, "\n", parts[j].trim());
    }
   return true; 
  }
  
  private static String fixGPS(String field) {
    if (field.length() < 8) return field;
    return field.substring(0,2) + '.' + field.substring(2);
  }
}
