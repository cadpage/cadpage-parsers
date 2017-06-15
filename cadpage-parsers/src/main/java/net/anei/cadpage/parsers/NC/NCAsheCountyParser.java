
package net.anei.cadpage.parsers.NC;

import java.util.Properties;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.anei.cadpage.parsers.MsgInfo.Data;
import net.anei.cadpage.parsers.dispatch.DispatchSouthernParser;

public class NCAsheCountyParser extends DispatchSouthernParser {

  public NCAsheCountyParser() {
    super(CITY_LIST, "ASHE COUNTY", "NC", 
          DSFLG_OPT_DISP_ID|DSFLG_ADDR|DSFLG_ADDR_TRAIL_PLACE|DSFLG_ADDR_NO_IMPLIED_APT|DSFLG_OPT_CODE|DSFLG_ID|DSFLG_TIME);
    setupMultiWordStreets(MWORD_STREET_LIST);
    setupSpecialStreets("VIRGINIA ST LINE");
    setupSaintNames("MARYS");
    addExtendedDirections();
    removeWords("PLACE", "SQUARE");
  }
  
  @Override
  public String getFilter() {
    return "@washconc.org";
  }
  
  @Override
  public int getMapFlags() {
    return MAP_FLG_PREFER_GPS | MAP_FLG_SUPPR_LA;
  }
  
  private static final Pattern CODE_CALL_PTN = Pattern.compile("(?:(\\S+) - +)?(.*?)[-/ ]*");
  
  @Override
  protected boolean parseMsg(String body, Data data) {
    if (!super.parseMsg(body, data)) return false;
    
    // Clean up call
    Matcher match = CODE_CALL_PTN.matcher(data.strCall);
    if (match.matches()) { // Always matches
      data.strCode = getOptGroup(match.group(1));
      data.strCall = match.group(2);
    }
    
    // Fix mispelled city names
    data.strCity = convertCodes(data.strCity.toUpperCase(), FIX_CITY_TABLE);
    
    // Check for out of state locations
    String state = CITY_ST_TABLE.getProperty(data.strCity);
    if (state != null) data.strState = state;
    
    // See if they put a cross street in front of the address
    if (data.strCross.length() == 0 && data.strPlace.endsWith("/")) {
      String cross = data.strPlace.substring(0,data.strPlace.length()-1).trim();
      data.strPlace = "";
      parseAddress(StartType.START_PLACE, FLAG_ONLY_CROSS | FLAG_NO_CITY | FLAG_ANCHOR_END, cross, data);
    }
    return true;
  }
  
  @Override
  public String getProgram() {
    return "X " + super.getProgram().replace("CITY", "CITY ST");
  }
  
  @Override
  protected boolean isNotExtraApt(String apt) {
    if (apt.startsWith("(") && apt.endsWith(")")) return true;
    return super.isNotExtraApt(apt);
  }

  private static final String[] MWORD_STREET_LIST = new String[]{
    "ABSHER FARM",
    "ANDERSON HILL",
    "ANDY FOWLER",
    "ASHE CENTRAL SCHOOL",
    "ASHE COUNTY HIGH SCHOOL",
    "ASHE PARK",
    "AUBREY TURNER",
    "B C HUNTER",
    "BALD MOUNTAIN",
    "BARE CREEK",
    "BARE HOLLOW",
    "BART HURLEY",
    "BEAR RIDGE",
    "BEAU RIVAGE",
    "BEAVER CREEK SCHOOL",
    "BEAVER CRK SCHOOL",
    "BEN BOLEN",
    "BIG FLATTS CHURCH",
    "BIG HELTON",
    "BIG HORSE CREEK",
    "BIG LAUREL",
    "BIG PEAK CREEK",
    "BIG PINEY CREEK",
    "BIG SPRINGS",
    "BIG WINDFALL",
    "BILL BLEDSOE",
    "BILL COOPER",
    "BLACK BEAR INN",
    "BLUE RIDGE",
    "BOBCAT CROSSING",
    "BOWER RIDGE",
    "BROWN HOLLOW",
    "BRUNT HILL",
    "BRUSHY FORK",
    "BRYAN DAVIS",
    "BUCK MT",
    "BUFFALO MEADOWS",
    "BURNT HILL",
    "CABBAGE CREEK",
    "CALL CREEK",
    "CALLOWAY GAP",
    "CAMPBELL GLEN",
    "CANOE GAP",
    "CARL EASTRIDGE",
    "CARSON WOODS",
    "CEDAR HILL",
    "CEDAR RIDGE",
    "CENTRAL BUFFALO",
    "CHEEK MOUNTAIN",
    "CHERRY HILL",
    "CHESTNUT HILL BAPTIST",
    "CHESTNUT HILL",
    "CLAUDE MASH",
    "CONLEY CHEEK",
    "COY HAM",
    "CRANBERRY CREEK",
    "CRANBERRY SPRINGS",
    "CROSS CREEK",
    "DALE BLEVINS",
    "DANIEL DAUGHTERS",
    "DAVIS ACRES",
    "DEEP FORD",
    "DICK PHILLIPS",
    "DISHMAN SEVERT",
    "DOG CREEK",
    "DON ADAMS",
    "DON WALTERS",
    "EARL RAY",
    "EARL SHEETS",
    "EARNEST HOWELL",
    "EAST BIG SPRINGS",
    "EAST BUCK MOUNTAIN",
    "EAST BUFFALO MEADOWS",
    "EAST DOUBLE SPRINGS CH",
    "EAST LANDING",
    "EAST LITTLE HORSE CREEK",
    "EAST MAIN",
    "EAST MILL CREEK",
    "EAST MT VIEW LODGE",
    "EAST SECOND",
    "EAST STAGGS CREEK",
    "ED LITTLE",
    "ELK CREEK CHURCH",
    "ELK KNOB",
    "FAIRVIEW CH",
    "FEES BRANCH",
    "FIELD CREEK",
    "FLAT ROCK",
    "FLATWOOD SCHOOL",
    "FLATWOODS SCHOOL",
    "FLINT HILL",
    "FOX RIDGE",
    "FRANK DILLARD",
    "FRANK EDWARDS",
    "FRANK MCMILLAN",
    "FRANK ROLAND",
    "FRED PUGH",
    "FRIENDSHIP BAPT CH",
    "FULTON REEVES",
    "GAITHER POE",
    "GARVEY BRIDGE",
    "GENTLE RIDGE",
    "GEORGE MCMILLAN",
    "GEORGE SHEPHERD",
    "GEORGE SHEPHERDS",
    "GINGER MTN",
    "GLENDALE SCHOOL",
    "GLENN KING",
    "GLENN MILLER",
    "GOLDEN POND",
    "GOLF COURSE",
    "GRASSY CREEK",
    "GRAY STONE",
    "GREEN HILL",
    "GREEN VALLEY",
    "GREER TOWN",
    "HAMPTON PLACE",
    "HARDIN GILLEY",
    "HARTZOG FORD",
    "HELEN BLEVINS",
    "HELTON CREEK",
    "HELTON SCHOOL",
    "HIDDEN CREEK",
    "HIDDEN SPRING",
    "HIDDEN VALLEY RIDGE",
    "HILL TOP ACRES",
    "HILLBILLY RIDGE",
    "HORSE CREEK",
    "HOWARD COLVARD",
    "HOWARD COVINGTON",
    "HUBERT TAYLOR",
    "HUCKLEBERRY RIDGE",
    "INDIAN LAKE",
    "IRA JORDAN",
    "IRWIN GROCERY",
    "J E GENTRY",
    "J TAYLOR",
    "JACK JONES",
    "JAKE BLACKBURN",
    "JAKE ROLAND",
    "JAMES F WATSON",
    "JEB HULL RIDGE",
    "JEFFERSON VIEW",
    "JERD BRANCH",
    "JOE BADGER",
    "JOE HAMPTON",
    "JOE JONES",
    "JOE LITTLE",
    "JOE WEAVER",
    "JOHN GRIFFITH",
    "JOHN HALSEY",
    "JOHN MILLER",
    "JONES WHITE",
    "JONT MOUNTAIN",
    "KEMP LEWIS",
    "LANE L MASSEY",
    "LAUREL FORK",
    "LAUREL KNOB",
    "LEE OSBORNE",
    "LEMLY HILL",
    "LIBERTY GROVE CH",
    "LIBERTY GROVE CHURCH",
    "LITTLE GAP",
    "LITTLE LAUREL",
    "LITTLE LYALL",
    "LITTLE PEAK CREEK",
    "LITTLE WINDFALL",
    "LONG BRANCH",
    "LOW GAP",
    "LUTHER TESTERMAN",
    "LYALLS ACRES",
    "MABE DAIRY",
    "MCNEILL HILL",
    "MILLER MTN",
    "MISTY MOUNTAIN",
    "MISTY RIDGE",
    "MONT SHEPHERD",
    "MOUNT JEFFERSON",
    "MOUNTAIN VALLEY",
    "MT JEFF",
    "MT JEFFERSON STATE PARK",
    "MT JEFFERSON",
    "MUDDY BRANCH",
    "MULATTO MT",
    "NAKED CREEK",
    "NATHANS CREEK SCHOOL",
    "NETTLE KNOB",
    "NOR FIELDS",
    "NORTH ACORN",
    "NORTH FLATWOODS",
    "NORTH FORK NEW RIVER",
    "NORTH FULTON REEVES",
    "NORTH LANDING",
    "NORTH OLD",
    "NORTHWEST SCHOOL",
    "OAK GROVE",
    "OBIDS BAPTIST CHURCH",
    "ORE KNOB",
    "ORE VALLEY",
    "OSBORNE MEMORIAL",
    "PAINTED LAUREL",
    "PARK VISTA",
    "PARKER ELLER",
    "PARKWAY FARM",
    "PARKWAY VISTA",
    "PARSONS HILL",
    "PAUL GOODMAN",
    "PEA RIDGE",
    "PEACH BOTTOM",
    "PERDY GROGAN",
    "PHILLIPS GAP",
    "PHOENIX COLVARD",
    "PINE KNOLL",
    "PINE MOUNTAIN",
    "PINEY CREEK SCHOOL",
    "PINEY CREEK",
    "PINNACLE TREE",
    "PISGAH HEIGHTS",
    "PLUMB NEARLY",
    "PONY FARM",
    "QUAIL HOLLOW",
    "RACCOON HOLLOW",
    "RAILROAD GRADE",
    "RASH SCHOOL",
    "RAY HILL",
    "RAYFIELD ACRES",
    "RED HILL",
    "RED OAK",
    "RICH HILL",
    "RIP SHIN",
    "RIVER BLUFF",
    "RIVER BREEZE",
    "RIVER HILLS",
    "RIVER RIDGE",
    "ROARING FORK",
    "ROBY POE",
    "ROCK CREEK",
    "ROCK FENCE",
    "ROCKY GAP",
    "ROCKY RIDGE",
    "ROE HUNT",
    "ROUND KNOB CHURCH",
    "ROY GOODMAN",
    "RUSSELL COMBS",
    "SCRAPE BOTTOM",
    "SHARON SWEENEY",
    "SHATLEY ROARK",
    "SHATLEY SPRINGS",
    "SHELTER BAPTIST CHURCH",
    "SILAS CREEK",
    "SKIN CAMP CREEK",
    "SMOKEY HOLLOW",
    "SMOKY HOLLOW",
    "SOUP BEAN BRANCH",
    "SOUR WOOD",
    "SOUTH BALD FORK",
    "SOUTH BIG HORSE CREEK",
    "SOUTH BLUE RIDGE",
    "SOUTH FLATWOODS",
    "SOUTH FORK",
    "SOUTH J E GENTRY",
    "SOUTH LAUREL FORK",
    "SOUTH MAIN",
    "SPENCER BRANCH",
    "SPRING CREEK",
    "ST DAVID",
    "STAGGS CREEK",
    "STATE PARK",
    "SUGAR TREE",
    "SWEET HOLLOW",
    "TEMPLE BAPTIST CH",
    "THIRTY FT CANAL",
    "THREE TOP",
    "TITUS RIDGE",
    "TOM FOWLER",
    "TOM MCNEILL",
    "TOM PEPPER",
    "TONY MILLER",
    "TRADING POST",
    "TRIVETTE HOLLOW",
    "VILLAGE PARK",
    "VIRGIL GREER",
    "W A REED",
    "WAGONER ACCESS",
    "WALNUT COVE",
    "WALTER BLEVINS",
    "WALTONS MT",
    "WARRENSVILLE SCHOOL",
    "WATER TANK",
    "WATERS EDGE",
    "WEAVER FORD",
    "WEST BRUSHY FORK",
    "WEST BUFFALO",
    "WEST DEEP FORD",
    "WEST EVERGREEN",
    "WEST LAUREL KNOB",
    "WEST MILL CREEK",
    "WEST PEAK",
    "WEST SUGAR TREE",
    "WHENLIN RIDGE",
    "WILDCAT HOLLOW",
    "WILEY F GAMBILL",
    "WILLIE BROWN",
    "WILLIE WALKER",
    "WINDY HILL",
    "WOLF KNOB",
    "WOODLEY STATION",
    "WOODROW BARE",
    "WORTH MCNEIL",
    "YELLOW BEAR",
    "YELLOWKNIFE RANCH",
    "Z SEXTON"
  };
  
  private static final Properties CITY_ST_TABLE = buildCodeTable(new String[]{
      "GRAYSON COUNTY",    "VA",
      "GRAYSON",           "VA",
      "JOHNSON COUNTY",    "TN",
      "JOHNSON",           "TN"
  });

  private static final Properties FIX_CITY_TABLE = buildCodeTable(new String[]{
      "CRUMLPER",   "CRUMPLER"
  });

  private static final String[] CITY_LIST = new String[]{
    
      "ASHE COUNTY",
      "ASHE",
      
      //Towns
      "JEFFERSON",
      "LANSING",
      "WEST JEFFERSON",

      //Townships
      "CHESTNUT HILL",
      "CLIFTON",
      "CRESTON",
      "ELK",
      "GRASSY CREEK",
      "HELTON",
      "HORSE CREEK",
      "HURRICANE",
      "JEFFERSON",
      "LAUREL",
      "LAUREL SPRINGS",
      "NORTH FORK",
      "OBIDS",
      "OLD FIELDS",
      "PEAK CREEK",
      "PINE SWAMP",
      "PINEY CREEK",
      "POND MOUNTAIN",
      "WALNUT HILL",
      "WEST JEFFERSON",

      //Unincorporated Communities
      "BEAVER CREEK",
      "BINA",
      "CHESTNUT HILL",
      "CLIFTON",
      "COMET",
      "CRESTON",
      "CRUMPLER",
      "CRUMLPER",  // Misspelled
      "FIG",
      "FLEETWOOD",
      "GLENDALE SPRINGS",
      "GRASSY CREEK",
      "GRAYSON",
      "HELTON",
      "PARKER",
      "SCOTTVILLE",
      "STURGILLS",
      "TODD",
      "WARRENSVILLE",
      
      // Alleghany County
      "ALLEGHANY COUNTY",
      "ALLEGHANY",
      "CRANBERRY",
      "ENNICE",
      "PINEY CREEK",
      "PRATHERS CREEK",
      "SPARTA",
      
      // Wataugua County
      "WATAUGUA COUNTY",
      "WATAUGUA",
      "BALD MOUNTAIN",
      "BOONE",
      "NORTH FORK",
      "STONY FORK",
      "ZIONVILLE",
      
      // Wilkes County
      "WILKES COUNTY",
      "WILKES",
      "ELK",
      "JOBS CABIN",
      "UNION",
      
      // Other counties
      
      "GRAYSON COUNTY",
      "GRAYSON",
      "JOHNSON COUNTY",
      "JOHNSON",
      
      // Washington County is on the other side of the state
      // But someone they have one department there (Creswell VFD)
      "WASHINGTON COUNTY",
      "WASHINGTON",

      // Cities
      "CRESWELL",
      "PLYMOUTH",
      "ROPER",

      // Unincorporated communities
      "LAKE PHELPS",
      "MACKEYS",
      "PEA RIDGE",
      "PLEASANT GROVE",

      // Townships
      "PLYMOUTH",
      "LEES MILL",
      "SCUPPERNONG",
      "SKINNERSVILLE"
  };
}
