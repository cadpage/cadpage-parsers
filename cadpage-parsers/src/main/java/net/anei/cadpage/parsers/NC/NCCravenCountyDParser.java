package net.anei.cadpage.parsers.NC;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.anei.cadpage.parsers.SmartAddressParser;
import net.anei.cadpage.parsers.MsgInfo.Data;

/**
 * Craven County, NC
 */
public class NCCravenCountyDParser extends SmartAddressParser {
  
  public NCCravenCountyDParser() {
    super("CRAVEN COUNTY", "NC");
    setFieldList("SRC PLACE ADDR APT GPS CITY CALL ID INFO");
    setupMultiWordStreets(MWORD_STREET_LIST);
    removeWords("COUNTY");
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
    parseOurAddress(match.group(2).trim(), data);
    setGPSLoc(match.group(3).trim(), data);
    data.strCity = match.group(4);
    data.strCall = match.group(5);
    data.strCallId = getOptGroup(match.group(6));
    data.strSupp = BREAK_PTN.matcher(match.group(7).trim()).replaceAll("\n");
    return true;
  }
  
  private static final Pattern DIR_SECTOR_PTN = Pattern.compile(" *\\b[NSEW] SECTOR\\b *");
  private static final Pattern STREET_APT_PTN = Pattern.compile("(.*\\b\\d+ )(\\d{1,3}[A-Z]?|[A-K]\\d*) (.*)");
  
  private void parseOurAddress(String addr, Data data) {
    
    addr =  DIR_SECTOR_PTN.matcher(addr).replaceAll(" ").trim();
    
    String apt = "";
    Matcher match = STREET_APT_PTN.matcher(addr);
    if (match.matches()) {
      addr = match.group(1) + match.group(3);
      apt = match.group(2);
    }
    parseAddress(StartType.START_PLACE, FLAG_IMPLIED_INTERSECT | FLAG_ANCHOR_END, addr, data);
    data.strApt = append(apt, "-", data.strApt);
    
    // A numeric place name was probably a street number followed by an apt number that was mistaken
    // for the street number
    if (NUMERIC.matcher(data.strPlace).matches()) {
      data.strAddress = append(data.strPlace, " ", data.strAddress);
      data.strPlace = "";
    }
  }
  
  private static final String[] MWORD_STREET_LIST = new String[]{
      
      "ASHLEY PARK",
      "BEND COLLEGE",
      "BETTYE GRESHAM",
      "CAMDEN SQUARE",
      "CLUB HOUSE",
      "COUNTRY CLUB",
      "COUNTY LINE",
      "EVANS MILL",
      "FAIRWAYS W",
      "MEDICAL PARK",
      "M L KING JR",
      "RED ROBIN",
      "WALT BELLAMY",
      "WEATHERSTONE PARK"

  };
}
