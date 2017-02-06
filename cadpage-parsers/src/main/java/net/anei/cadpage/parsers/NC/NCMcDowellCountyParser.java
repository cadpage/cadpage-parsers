
package net.anei.cadpage.parsers.NC;

import java.util.Properties;
import java.util.regex.Pattern;

import net.anei.cadpage.parsers.MsgInfo.Data;
import net.anei.cadpage.parsers.SplitMsgOptions;
import net.anei.cadpage.parsers.SplitMsgOptionsCustom;
import net.anei.cadpage.parsers.dispatch.DispatchSouthernParser;

public class NCMcDowellCountyParser extends DispatchSouthernParser {

  public NCMcDowellCountyParser() {
    super(CITY_LIST, "MCDOWELL COUNTY", "NC", 
          DSFLG_OPT_DISP_ID | DSFLG_ADDR | DSFLG_ADDR_LEAD_PLACE | DSFLG_ADDR_TRAIL_PLACE | DSFLG_OPT_X | DSFLG_OPT_CODE | DSFLG_TIME);
    setupMultiWordStreets(MWORD_STREET_LIST);
    setupSaintNames("JOSEPH");
  }
  
  @Override
  public SplitMsgOptions getActive911SplitMsgOptions() {
    return new SplitMsgOptionsCustom();
  }

  @Override
  public boolean parseMsg(String body, Data data) {
    if (! super.parseMsg(body, data)) return false;
    data.strCall = stripFieldEnd(data.strCall, "-");
    data.strCity = stripFieldEnd(data.strCity, " CITY");
    data.strCity = stripFieldEnd(data.strCity, " AREA");
    return true;
  }
  
  @Override
  public String adjustMapAddress(String addr) {
    addr = PK_PTN.matcher(addr).replaceAll("PARK");
    return super.adjustMapAddress(addr);
  }
  private static final Pattern PK_PTN = Pattern.compile("\\bPK\\b", Pattern.CASE_INSENSITIVE);
  
  @Override
  public String adjustMapCity(String city) {
    city = city.toUpperCase();
    city = stripFieldEnd(city, " AREA");
    return convertCodes(city, MAP_CITY_TABLE);
  }
  
  private static final String[] MWORD_STREET_LIST = new String[]{
    "ALLISON HOLLOW",
    "AMERICAN THREAD",
    "AMERICAN THREAD VILLAGE",
    "APPALACHIAN HILL",
    "AUTUMN VIEW",
    "BAT CAVE",
    "BEAR CLIFF",
    "BETHEL CHURCH",
    "BIG SKY",
    "BLACK FOREST",
    "BLUE RIDGE",
    "BOULDER ROCK",
    "BRACKETT TOWN",
    "BRADLEY PARK",
    "BRANSON CREEK",
    "BUCK CREEK",
    "BUMPY HILL",
    "BYRDS MOBILE HOME",
    "CANNON WOODS",
    "CARRIAGE STATION",
    "CATAWBA RIVER",
    "CHESTNUT HILL",
    "CHESTNUT OAK FOREST",
    "CLEAR CREEK",
    "CLEAR WATER",
    "CLUB HOUSE",
    "COACHMAN HILLS",
    "COLUMBIA CAROLINA",
    "COOL SPRING",
    "COTTAGE GROVE",
    "COUNTRY ACRES",
    "CROWN RIDGE",
    "CURTIS CREEK",
    "DEER PARK",
    "DEER PARK RESORT",
    "DEER PATH",
    "DOBSONS VIEW",
    "DRUCILLA CHURCH",
    "DRUMS INLET",
    "DUSTY HOLLOW",
    "DWIGHT BERRY",
    "EASTFIELD SCHOOL",
    "EBENEZER CHURCH",
    "EMMA GRACE",
    "FALLING CREEK",
    "FARM HILL",
    "FIFTH EM",
    "FOREST HILL",
    "FOREST PARK",
    "FOREST RIDGE",
    "FORK RIVER",
    "FORT SUGAR HILL",
    "FOURTH EM",
    "GARDEN CREEK",
    "GRASSY HOLLOW",
    "GREAT OAKS",
    "GREEN MEADOW",
    "HALF TIMBER",
    "HALL ESTATES",
    "HARMONY GROVE",
    "HAW BRANCH",
    "HENRY MCCALL",
    "HIGH TRAIL",
    "HOLLY HILL",
    "HOLLY HOCK",
    "HOPPY TOM HOLLOW",
    "HUSKINS BRANCH",
    "JACK CORPENING",
    "JAKES BRANCH",
    "JAY DAN",
    "JOHN ROACH",
    "JOHNSON HOLLOW",
    "KEN TOM",
    "LAKE CLUB",
    "LAKE JAMES",
    "LAKE JAMES LANDING",
    "LAKE TAHOMA",
    "LAUREL CROSSING",
    "LEVEL PLAINS",
    "LIMEKIL VIEW",
    "LINVILLE HOLLOW",
    "LITTLE BUCK CREEK",
    "LOCUST COVE",
    "LOST COVE",
    "LYTLE MOUNTAIN",
    "MAJOR CONLEY",
    "MAPLE HILL",
    "MARTIN BRANCH",
    "MASHBURN BRANCH",
    "MCDOWELL HIGH",
    "MCDOWELL JUNIOR HIGH",
    "MEMORIAL PARK",
    "MISTY MOUNTAIN",
    "MOFFITT BRANCH",
    "MOFFITT HILL CHURCH",
    "MONTFORD COVE",
    "MOUNTAIN IVY",
    "MOUNTAIN VIEW",
    "MUD CUT",
    "MUDDY CREEK",
    "NANCY TOLLEY",
    "NEBO SCHOOL",
    "NED MCGIMSEY",
    "NEWBERRY CREEK",
    "NIX CREEK",
    "NIX CREEK CHURCH",
    "NORTH WAYCASTER",
    "OAK HILL",
    "OLDE DUFFER",
    "OVERLOOK PARK",
    "PARKER PADGETT",
    "PERSIMMON BRANCH",
    "PG BAPTIST CHURCH",
    "PINE COVE",
    "PINNACLE CHURCH",
    "PINNACLE HEIGHTS",
    "PLEASANT HILL",
    "PLEASANT VIEW",
    "QUARTER MOUNTAIN",
    "RAINBOW TROUT",
    "RED OAK",
    "REVIS CEMETERY",
    "RIVER VIEW",
    "ROBERT MAYNOR",
    "ROBY CONLEY",
    "ROCKING ROBYN",
    "ROLANDS CHAPEL",
    "ROLLING HILLS",
    "ROSES CHAPEL",
    "ROY HOLLIFIELD",
    "ROY MOORE",
    "SADDLE WOOD",
    "SHADY BROOK",
    "SHERWOOD FOREST",
    "SILVERS WELCH",
    "SIXTH EM",
    "SLIPPERY ROCK",
    "SOUTH CREEK",
    "STACY FARM",
    "STACY HILL",
    "STACY HOLLOW",
    "SUGAR HILL",
    "THOUSAND OAKS",
    "TRIPLE R",
    "TWIN LAKES",
    "US 221",
    "VEIN MOUNTAIN",
    "WALNUT GROVE",
    "WARREN MOUNTAIN",
    "WEST CAMP",
    "WEST HENDERSON",
    "WESTWOOD CHATEAU",
    "WHISPERING RIDGE",
    "WHITE PINES",
    "WILDCAT BLUES",
    "WILDLIFE CLUB",
    "WILLOW CREEK",
    "WILSON FARM",
    "WINSOME FOREST",
    "ZION HILL"
  };
  
  private static final Properties MAP_CITY_TABLE = buildCodeTable(new String[]{
      "HANKINS NORTH FORK", "MARION",
      "PG",                 "MARION",
      "PLEASANT GARDEN",    "MARION",
      "WOODLAWN",           "MARION"
  });
  
  private static final String[] CITY_LIST = new String[]{
      
    //cities
    "MARION",
    "MARION AREA",
    "MARION CITY",
    "HANKINS NORTH FORK",
    "WOODLAWN",
    
    //towns
    "OLD FORT",
    "OLD FORT AREA",
    "OLD FORT TOWN",
    
    //Census-designated place
    "WEST MARION",
    "WEST MARION CITY",
    
    //Unincorporated communities
    "GLENWOOD",
    "LINVILLE FALLS",
    "LITTLE SWITZERLAND",
    "NEBO",
    "NORTH COVE",
    "PG",
    "PLEASANT GARDENS",
    "PROVIDENCE",

    //townships
    "CROOKED CREEK",
    "DYSARTSVILLE",
    "MONTFORD COVE",
    "PLEASANT GARDENS",
    "WOODLAWN-SEVIER",
    "SUGAR HILL",
    
    // Buncombe County
    "ASHEVILLE",
    "BLACK MOUNTAIN"
  };
}
