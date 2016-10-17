package net.anei.cadpage.parsers.IL;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.anei.cadpage.parsers.MsgInfo.Data;
import net.anei.cadpage.parsers.SmartAddressParser;

public class ILKaneCountyAParser extends SmartAddressParser {
  
  private static final Pattern MASTER = Pattern.compile("([- A-Z0-9]+?)  (?:((?:[A-Z]{2}(?:\\d{4}[A-Z]?|-[A-Z0-9]+)|ALGN|ELGIN|PNGR)) )?([^ ].*?) ([A-Z]{3,4}) - (.*?)(?: (\\d{4}-\\d{8}))?(?: ([A-Z]-\\d\\d))?");
  private static final Pattern APT_PTN = Pattern.compile("([0-9][^ ]*|[A-Z]\\d*)\\b *(.*)", Pattern.CASE_INSENSITIVE);
  
  public ILKaneCountyAParser() {
    super(ILKaneCountyParser.CITY_LIST, "KANE COUNTY", "IL");
    setFieldList("UNIT BOX ADDR APT PLACE CITY CODE CALL ID MAP");
  }
  
  @Override
  public String getFilter() {
    return "Quad Com,Dispatch@quadcom911.org";
  }
  @Override
  public boolean parseMsg(String subject, String body, Data data) {
    
    if (!subject.equals("Dispatch Incident")) return false;
    
    Matcher match = MASTER.matcher(body);
    if (!match.matches()) return false;
    
    data.strUnit = match.group(1).replace("  ", " ");
    data.strBox =  getOptGroup(match.group(2));
    parseAddress(StartType.START_ADDR, FLAG_IMPLIED_INTERSECT | FLAG_ALLOW_DUAL_DIRECTIONS | FLAG_PAD_FIELD | FLAG_ANCHOR_END, match.group(3).trim(), data);
    String pad = getPadField();
    data.strCode = match.group(4);
    data.strCall = match.group(5).trim();
    data.strCallId = getOptGroup(match.group(6));
    data.strMap = getOptGroup(match.group(7));
    
    match = APT_PTN.matcher(pad);
    if (match.matches()) {
      data.strApt = append(data.strApt, "-", match.group(1));
      pad = match.group(2);
    }
    data.strPlace = pad;

    return true;
  }
}
