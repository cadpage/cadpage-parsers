package net.anei.cadpage.parsers.GA;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.anei.cadpage.parsers.MsgInfo.Data;
import net.anei.cadpage.parsers.SmartAddressParser;


public class GAWalkerCountyParser extends SmartAddressParser {
  
  private static final Pattern UNIT_SRC_PTN = Pattern.compile("^((?:(?:[A-Z]+\\d+|[A-Z]{1,2}FD) +)*)([A-Z]{1,2}FD|\\d-[A-Z]) +");
  private static final Pattern TRAIL_ID_PTN = Pattern.compile(" +(\\d{4}-\\d{8})");
  
  private static final Pattern DATE_TIME_PTN = Pattern.compile(" +(\\d\\d/\\d\\d/\\d\\d) (\\d\\d:\\d\\d)$");
  private static final Pattern PART_DATE_TIME_PTN = Pattern.compile(" +\\d\\d/[\\d/]*(?: [\\d:]*)?$");
  private static final Pattern DISPATCHED_PTN = Pattern.compile("^Dispatch received by unit ([A-Z0-9]+)\\b *");
  
  public GAWalkerCountyParser() {
    super(CITY_LIST, "WALKER COUNTY", "GA");
  }
  
  @Override
  public String getFilter() {
    return "dispatch@walkerga.org";
  }
  
  @Override
  protected boolean parseMsg(String subject, String body, Data data) {
    
    if (!subject.equals("!")) return false;
    
    // There are two possible page formats, try the newest one first
    if (parseFormat2(body, data)) return true;
    if (parseFormat1(body, data)) return true;
    return false;
  }

  // Process older page format
  private boolean parseFormat1(String body, Data data) {
    setFieldList("CALL ADDR APT X CITY UNIT INFO DATE TIME");

    int pt = body.indexOf("   ");
    if (pt < 0) return false;
    String addr = body.substring(0,pt);
    String desc = body.substring(pt+3).trim();
    
    parseAddress(StartType.START_CALL, FLAG_START_FLD_REQ | FLAG_PAD_FIELD | FLAG_ANCHOR_END, addr, data);
    data.strCross = getPadField();
    
    // Strip date/time from end of description area
    Matcher match = DATE_TIME_PTN.matcher(desc);
    if (match.find()) {
      data.strDate = match.group(1);
      data.strTime = match.group(2);
      desc = desc.substring(0, match.start());
    }
    
    // No go, see if we should strip off a partial date/time
    else {
      match = PART_DATE_TIME_PTN.matcher(desc);
      if (match.find()) desc = desc.substring(0,match.start());
    }
    
    // Strip off leading unit specs
    while (true) {
      match = DISPATCHED_PTN.matcher(desc);
      if (!match.find()) break;
      data.strUnit = append(data.strUnit, " ", match.group(1));
      desc = desc.substring(match.end());
    }
    
    data.strSupp = desc;
    return true;
  }
  
  // Process new page format
  private boolean parseFormat2(String body, Data data) {
    
    // Strip off units and department codes from beginning of body
    Matcher match = UNIT_SRC_PTN.matcher(body);
    if (!match.find()) return false;
    data.strUnit = match.group(1).trim();
    data.strSource = match.group(2);
    body = body.substring(match.end());
    
    // Check for trailing ID field
    match = TRAIL_ID_PTN.matcher(body);
    if (match.find()) {
      data.strCallId = match.group(1);
      body = body.substring(0,match.start());
    }
    
    // SOmetimes there is no blank before the city :(
    body = MISSED_BLANK_PTN.matcher(body).replaceFirst(" ");

    // Parser address and other info
    parseAddress(StartType.START_ADDR, FLAG_IMPLIED_INTERSECT | FLAG_PAD_FIELD, body, data);
    data.strPlace = getPadField();
    data.strCall = getLeft();

    setFieldList("UNIT SRC ADDR APT PLACE CITY CALL ID");
    return true;
  }

  
  private static final String[] CITY_LIST = new String[]{
    "CHICKAMAUGA",
    "LAFAYETTE",
    "LOOKOUT MOUNTAIN",
    "ROSSVILLE",
    "CHATTANOOGA VALLEY",
    "NOBLE",
    "FAIRVIEW",
    "FLINTSTONE",
    "KENSINGTON",
    "NAOMI",
    "DRY CREEK",
    "ROCK SPRING",
    "VILLANOW",
    "HIGH POINT"
  };
  
  private static final Pattern MISSED_BLANK_PTN = 
      Pattern.compile("(?<! )(?=(?:CHICKAMAUGA|LAFAYETTE|LOOKOUT MOUNTAIN|ROSSVILLE|CHATTANOOGA VALLEY|NOBLE|FAIRVIEW|FLINTSTONE|KENSINGTON|NAOMI|DRY CREEK|ROCK SPRING|VILLANOW|HIGH POINT)\\b)");
}
