package net.anei.cadpage.parsers.PA;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.anei.cadpage.parsers.MsgInfo.Data;
import net.anei.cadpage.parsers.SmartAddressParser;

public class PAErieCountyDParser extends SmartAddressParser {
  
  public PAErieCountyDParser() {
    super(PAErieCountyParser.CITY_LIST, "ERIE COUNTY", "PA");
    setFieldList("SRC DATE TIME PRI CALL CODE ADDR CITY APT PLACE X INFO GPS");
    setupMultiWordStreets(MWORD_STREET_LIST);
    removeWords("RIDGE");
  }
  
  @Override
  public String getFilter() {
    return "snpp@eriecountypa.gov,messaging@iamresponding.com";
  }
  
  @Override
  public int getMapFlags() {
    return MAP_FLG_PREFER_GPS;
  }
  
  private static final Pattern SUBJECT_SRC_PTN = Pattern.compile("[A-Za-z ]+");
  private static final Pattern MASTER = Pattern.compile("snpp:(\\d\\d?/\\d\\d?/\\d{4}) (\\d\\d:\\d\\d:\\d\\d) (?:(High|Medium|Low) )?(.*?) -?(\\d{1,2}) (.*)");
  private static final Pattern APT_PTN = Pattern.compile("(?:LOT|APT|RM|ROOM) (\\S+) *(.*)");
  
  @Override
  protected boolean parseMsg(String subject, String body, Data data) {
    
    if (SUBJECT_SRC_PTN.matcher(subject).matches() && !subject.equals("Text Message")) data.strSource = subject;
    
    Matcher match = MASTER.matcher(body);
    if (!match.matches()) return false;
    data.strDate = match.group(1);
    data.strTime = match.group(2);
    data.strPriority = getOptGroup(match.group(3));
    data.strCall = match.group(4).trim();
    data.strCode = match.group(5);
    String addr = match.group(6);
    
    Parser p = new Parser(addr);
    addr = p.get(" Lat:");
    String gps1 = p.get(" Lon:");
    String gps2 = p.get();
    setGPSLoc(gps1+','+gps2, data);
    
    addr = stripFieldStart(addr, "UNKNOWN ");
    addr = addr.replace('@', '/');
    int pt = addr.indexOf(',');
    if (pt >= 0) {
      parseAddress(addr.substring(0,pt).trim(), data);
      parseAddress(StartType.START_ADDR, FLAG_ONLY_CITY, addr.substring(pt+1).trim(), data);
    } else {
      parseAddress(StartType.START_ADDR, FLAG_CROSS_FOLLOWS, addr, data);
    }
    addr = getLeft();
    addr = stripFieldStart(addr, "BORO");
    addr = stripFieldStart(addr, "CITY");
    if (!data.strAddress.contains("&")) {
      Result res = parseAddress(StartType.START_PLACE, FLAG_ONLY_CROSS, addr);
      if (res.isValid()) {
        res.getData(data);
        addr = res.getLeft();
        
        match = APT_PTN.matcher(data.strPlace);
        if (match.matches()) {
          data.strApt = append(data.strApt, "-", match.group(1));
          data.strPlace = match.group(2);
        }
      }
    }
    data.strSupp = addr;
    
    return true;
  }
  
  private static final String[] MWORD_STREET_LIST = new String[]{
      "BEAR CREEK",
      "BLUE SPRUCE",
      "BRIER HILL",
      "CAMBRIDGE SPRINGS",
      "CARRIAGE HILL",
      "CHERRY HILL",
      "CROSS STATION",
      "EDGE PARK",
      "ELK CREEK",
      "ELK PARK",
      "FAIR OAKS",
      "FIELD VALLEY",
      "FOREST GLEN",
      "FOX HOLLOW",
      "FRANKLIN CENTER",
      "GENEVA MARIE",
      "GIBSON HILL",
      "GLENWOOD PARK",
      "GOLF CLUB",
      "GREEN OAKS",
      "HALF MOON",
      "HANNA HALL",
      "HARLEY DAVIDSON",
      "HASKELL HILL",
      "HOPSON HILL",
      "IMPERIAL POINT",
      "JOHN WILLIAMS",
      "KAHKWA CLUB",
      "KIMBALL HILL",
      "KINTER HILL",
      "LAKE FRONT",
      "LAKE PLEASANT",
      "LAKE SHORE",
      "MCGAHEN HILL",
      "NICKLE PLATE",
      "OLD RIDGE",
      "OLD STATE",
      "OLD WATTSBURG",
      "OLD ZUCK",
      "PENELEC PARK",
      "PIN OAK",
      "PINE LEAF",
      "PINE TREE",
      "PINE VALLEY",
      "RILEY SIDING",
      "SHERROD HILL",
      "SPRING LAKE",
      "SPRING VALLEY",
      "SPRUCE TREE",
      "STONE QUARRY",
      "SUNRISE LAKES",
      "TAYLOR RIDGE",
      "UNION AMITY",
      "UNION LEBOEUF",
      "VALLEY VIEW",
      "VILLAGE COMMON",
      "WALNUT CREEK",
      "WASHINGTON TOWNE",
      "WOLF RUN VILLAGE",
      "WOLF RUN",
      "WOODLAND HILL"
  };

}
