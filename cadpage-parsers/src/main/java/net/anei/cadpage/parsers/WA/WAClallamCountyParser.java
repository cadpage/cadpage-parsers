package net.anei.cadpage.parsers.WA;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.anei.cadpage.parsers.MsgInfo.Data;
import net.anei.cadpage.parsers.SmartAddressParser;

public class WAClallamCountyParser extends SmartAddressParser {
  
  public WAClallamCountyParser() {
    super(CITY_LIST, "CLALLAM COUNTY", "WA");
  }
  
  @Override
  public String getFilter() {
    return "Dispatch@co.clallam.wa.us";
  }
  
  private static final Pattern MASTER1 = Pattern.compile("(?:([A-Z0-9 ]+)  )?(?:([A-Z0-9]+) - (Stn \\S+|[A-Z][a-z]+) )?(.*?) (\\d{4}-\\d{8})");
  private static final Pattern MASTER2 = Pattern.compile("(\\d\\d/\\d\\d/\\d\\d) (\\d\\d:\\d\\d) (\\S+)((?: \\S+)*)  (?:(.*?) )?(\\d{4}-\\d{8}) *(.*)");
  private static final Pattern INFO_JUNK_PTN = Pattern.compile("\\bDispatch received by unit \\S+\\b|\\bCall Number \\d+ was created from Call Number \\d+(?:\\([A-za-z0-9 :]*\\))?|(?:  |^)E911 Info.*");
  
  @Override
  protected boolean parseMsg(String subject, String body, Data data) {
    
    if (!subject.equals("Incident")) return false;
    
    Matcher match = MASTER2.matcher(body);
    if (match.matches()) {
      setFieldList("DATE TIME CALL UNIT ADDR APT ID INFO");
      data.strDate = match.group(1);
      data.strTime = match.group(2);
      data.strCall = match.group(3);
      data.strUnit = match.group(4).trim();
      String addr = getOptGroup(match.group(5)).replaceAll("  +", " "); 
      parseAddress(addr, data);
      data.strCallId = match.group(6);
      String info = match.group(7);
      info = INFO_JUNK_PTN.matcher(info).replaceAll("");
      info = info.replaceAll("  +", "").trim();
      data.strSupp = info;
      return true;
    }
    
    match = MASTER1.matcher(body);
    if (match.matches()) {
      setFieldList("UNIT SRC MAP ADDR APT PLACE CITY CALL ID");
      data.strUnit = getOptGroup(match.group(1));
      data.strSource = getOptGroup(match.group(2));
      data.strMap = getOptGroup(match.group(3));
      String addr = match.group(4).trim();
      data.strCallId = match.group(5);
      
      parseAddress(StartType.START_ADDR, FLAG_PAD_FIELD | FLAG_IMPLIED_INTERSECT, addr, data);
      data.strPlace = getPadField();
      if (data.strPlace.equals("BLK")) {
        data.strAddress = append(data.strAddress, " ", data.strPlace);
        data.strPlace = "";
      }
      data.strCall = getLeft();
      return data.strCall.length() > 0;
    }
    
    return false;
  }
  
  private static final String[] CITY_LIST = new String[]{

    // Cities
    "FORKS",
    "PORT ANGELES",
    "SEQUIM",

    // Census-designated places
    "BELL HILL",
    "BLYN",
    "CARLSBORG",
    "CLALLAM BAY",
    "JAMESTOWN",
    "NEAH BAY",
    "PORT ANGELES EAST",
    "RIVER ROAD",
    "SEKIU",

    // Other communities
    "AGATE BEACH",
    "AGNEW",
    "BEAVER",
    "BOGACHIEL",
    "CRANE",
    "DIAMOND POINT",
    "DUNGENESS",
    "ELWHA",
    "FAIRHOLM",
    "HOKO",
    "JOYCE",
    "LA PUSH",
    "MAPLE GROVE",
    "MORA",
    "OZETTE",
    "PYSHT",
    "PIEDMONT",
    "SAPPHO",
    "SCHOOLHOUSE POINT",
    "SEKIU",
    "UPPER HOH"
  };

}
