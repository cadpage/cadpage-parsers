
package net.anei.cadpage.parsers.NC;

import java.util.Properties;
import java.util.regex.Pattern;

import net.anei.cadpage.parsers.CodeSet;
import net.anei.cadpage.parsers.MsgInfo.Data;
import net.anei.cadpage.parsers.dispatch.DispatchSouthernParser;


public class NCWataugaCountyAParser extends DispatchSouthernParser {

  public NCWataugaCountyAParser() {
    super(CITY_LIST, "WATAUGA COUNTY", "NC",
          DSFLG_OPT_DISP_ID|DSFLG_ADDR|DSFLG_OPT_X|DSFLG_ID|DSFLG_TIME);
    setupMultiWordStreets(MWORD_STREET_LIST);
    setupGpsLookupTable(GPS_LOOKUP_TABLE);
  }
  
  @Override
  public String getFilter() {
    return "wcc911@wataugacounty.org";
  }
  
  private String extra;

  @Override
  protected boolean parseMsg(String body, Data data) {
    extra = null;
    if (!super.parseMsg(body, data)) return false;
    if (extra != null) data.strSupp = append(extra, "\n", data.strSupp);
    return true;
  }

  private static final Pattern MEDIC_PREFIX_PTN = Pattern.compile("MEDICS |MANPOWER ");
  @Override
  protected void parseMain(String sAddr, Data data) {
    
    if (MEDIC_PREFIX_PTN.matcher(sAddr).lookingAt()) {
      Data tmpData = new Data(this);
      parseAddress(StartType.START_OTHER, FLAG_START_FLD_REQ, sAddr, tmpData);
      extra = getStart();
      sAddr = sAddr.substring(extra.length()).trim();
    }
    
    super.parseMain(sAddr, data);
    data.strCross = stripFieldStart(data.strCross, "FROM ");
    
    if (data.strCross.length() > 0 && data.strCity.length() > 0 ) {
      String line = "";
      String temp = data.strCross;
      if (temp.startsWith("LINE ")) {
        line = "LINE";
        temp = temp.substring(5).trim();
      }
      if (isCity(temp)) {
        String extra = append(data.strCity, " ", line);
        if (data.strPlace.startsWith("NEAR ") || data.strPlace.startsWith("BESIDE ")) {
          data.strPlace = append(data.strPlace, " ", extra);
        } else {
          data.strAddress = append(data.strAddress, " ", data.strCity);
        }
        data.strCity = temp;
        data.strCross = "";
      }
    }
  }

  @Override
  protected void parseExtra(String sExtra, Data data) {
    
    String call = MWORD_CALL_LIST.getCode(sExtra.toUpperCase());
    if (call != null) {
      data.strCall = sExtra.substring(0,call.length());
      data.strSupp = sExtra.substring(call.length()).trim();
      return;
    }

    int pt = sExtra.indexOf(' ');
    if (pt >= 0) {
      data.strCall = sExtra.substring(0,pt);
      data.strSupp = sExtra.substring(pt+1).trim();
      return;
    }
    data.strCall =  sExtra;
    return;
  }
  
  @Override
  protected String adjustGpsLookupAddress(String address, String apt) {
    if (apt.length() > 0) address = append(address, " ", "UNIT " + apt);
    return address;
  }
  
  private static CodeSet MWORD_CALL_LIST = new CodeSet(new String[]{
      "10-50 PI",
      "10-65/ ARMED ROBBERY",
      "911 HANG UP",
      "911 OPEN LINE",
      "ABDOMINAL PAIN",
      "ELEVATOR-ESCALATOR TEST",
      "FUEL-SPILL DIESEL",
      "SICK PERSON",
      "STRUCTURE FIRE",
      "UNKNOWN PROBLEM (MAN DOWN)"
  });

  private static final String[] MWORD_STREET_LIST = new String[]{
    "ANDY HICKS",
    "APPLE ORCHARD",
    "ARCHIE CARROLL",
    "ARVIL GREER",
    "ASTOR COOK",
    "B H DUNCAN",
    "BAIRDS CREEK",
    "BALL BRANCH",
    "BASS LAKE",
    "BEAVER HORN RANCH",
    "BEECH TREE",
    "BEN MILLER",
    "BIG HILL",
    "BIG VALLEY",
    "BLOWING ROCK",
    "BLUE BUCK",
    "BOONE COFFEY",
    "BOONE HEIGHTS",
    "BOULDER GARDENS",
    "BOULDER RIDGE",
    "BROOK HOLLOW",
    "BROWN FARM",
    "BROWNS CHAPEL",
    "BRYAN HOLLOW",
    "BURL BROWN",
    "CABBAGE CREEK",
    "CAMP JOY",
    "CASTLE FORD",
    "CHARLIE DANCY",
    "CHESTNUT GROVE",
    "CHESTNUT RIDGE",
    "CHETOLA LAKE",
    "CHRIST CHURCH",
    "CIRCLE OAK",
    "CLAY HODGES",
    "CLAY WILSON",
    "CLINT NORRIS",
    "COBBS CREEK",
    "COFFEY KNOB",
    "COUNTRY CLUB",
    "CRANBERRY SPRINGS",
    "CREEK VIEW",
    "CURLEY MAPLE",
    "DAISY RIDGE",
    "DAVID MCLEAN",
    "DECK HILL",
    "DEEP GAP",
    "DEEP WOOD",
    "DELL COFFEY",
    "DEVILS LAKE",
    "DEWITT BARNETT",
    "DIAMOND RANCH",
    "DICK WATSON",
    "DOC BYRD",
    "DOLLS EYE",
    "DON HAYES",
    "EAST COVE",
    "ECHO POINT",
    "ED HARDY",
    "ED WILLIAMS",
    "ELK CREEK",
    "EVERGREEN SPRINGS",
    "FAIRWAY 11",
    "FALL CREEK",
    "FARRIS VALLEY",
    "FARTHING HAYES",
    "FLAT TOP",
    "FLETCHER BRANCH",
    "FORD HOLLARS",
    "FOREST HILL",
    "FOX COVE",
    "FRANK HODGES",
    "FRED HATLEY",
    "GEORGE COOK",
    "GEORGES GAP",
    "GIDEON RIDGE",
    "GOAT RUN",
    "GOLDEN RIDGE",
    "GRADY WINKLER",
    "GRANDE OAKS",
    "GRASSY KNOLL",
    "GREEN HEIGHTS",
    "GREEN HILL",
    "GREENE FARM",
    "GUY FORD",
    "H O ALDRIDGE",
    "HARLEY PERRY",
    "HATTIE HILL",
    "HERB THOMAS",
    "HIDDEN VALLEY",
    "HIDDEN WATER",
    "HIGH COUNTRY",
    "HIGH KNOLLS",
    "HIGH RIDGE",
    "HITE WILLIAMS",
    "HODGES GAP",
    "HOLLOWAY MOUNTAIN",
    "HOMESPUN HILL",
    "HONEY BEAR CAMPGROUND",
    "HOPEWELL CHURCH",
    "HORN IN WEST",
    "HOT ROD",
    "HOWARD WOODRING FAMILY",
    "HOWARDS CREEK",
    "HOWARDS KNOB",
    "HUNTING HILLS",
    "INDIAN PAINTBRUSH",
    "ISAACS BRANCH",
    "IVY RIDGE",
    "IVY TERRACE",
    "J C GREENE",
    "J H MCLEAN",
    "JANS MOUNTAIN",
    "JIMMY BILLINGS",
    "JORDAN V COOK",
    "JORDON V COOK",
    "JOYCE LAWRENCE",
    "KELLER FARM",
    "KNOB HILL",
    "LARRY WINKLER",
    "LAUREL BRANCH",
    "LAUREL CREEK",
    "LAUREL FORK",
    "LAUREL PARK",
    "LAZY LAKE",
    "LINVILLE CREEK",
    "LITTLE JAKE",
    "LITTLE WOODS",
    "LOCUST GAP",
    "MABEL SCHOOL",
    "MAIN HAVEN",
    "MARKET HEIGHTS",
    "MAST GAP",
    "MCGUIRE MOUNTAIN",
    "MEADOW HILL",
    "MEADOW LARK",
    "MEAT CAMP",
    "MIDDLE FORK",
    "MILTON BROWN HEIRS",
    "MILTON MORETZ",
    "MOUNTAIN BIKE",
    "MOUNTAIN DALE",
    "MOUNTAIN VIEW BAPT CHURCH",
    "MT PARON",
    "MYSTERY HILL",
    "NETTLES RIDGE",
    "NORTH FORK",
    "NORTH RIDGE",
    "NORTH WOODS",
    "PARKWAY FOREST",
    "PARKWAY VILLAGE",
    "PAYNE BRANCH",
    "PEACEFUL HAVEN",
    "PHILLIPS BRANCH",
    "PINE BRANCH",
    "PINE HILL",
    "PINE RUN",
    "PINE TUNNEL",
    "POPLAR CREEK ESTATES",
    "POPLAR HILL",
    "POSSUM HOLLOW",
    "R J ALDRIDGE",
    "R LAYNG",
    "RALPH WILSON",
    "RAVEN ROCKS FARM",
    "RAY BROWN",
    "RED MAPLE",
    "RED TAILED HAWK",
    "RICH MOUNTAIN",
    "RIDGE HAVEN",
    "RIDGE POINT",
    "RIVER LAKE",
    "RIVER POINTE",
    "RIVER RIDGE",
    "ROARING FORK",
    "ROBY EGGERS",
    "ROBY GREENE",
    "ROCK CLIFF",
    "ROCKY KNOB",
    "ROYAL OAKS",
    "RUSH BRANCH",
    "SADDLE HILLS",
    "SAIGER MOUNTAIN",
    "SEVEN DEVILS",
    "SHOPPES ON THE PARKWAY",
    "SHULLS MILL",
    "SNOWY OAK",
    "SNYDER BRANCH",
    "SOC HOUCK",
    "SORRENTO KNOLLS",
    "SOUTH DEPOT",
    "SPICE CREEK",
    "STADIUM HEIGHTS",
    "STATE FARM",
    "STONE MOUNTAIN",
    "STONEY BROOK",
    "STONEY FORK",
    "STONY FORK",
    "STRAWBERRY FIELDS",
    "SYCAMORE PINES",
    "T E CANNON",
    "TATER HILL ESTATES",
    "TATER HILL",
    "TIMBER LAKES",
    "TRUE JOY",
    "TURTLE CREEK",
    "TWEETSIE RAILROAD",
    "TWIN RIVERS",
    "V L MORETZ",
    "VALLE CAY",
    "VALLEY VIEW",
    "VAUGHN POTTER",
    "VIRGIL DAY",
    "WADE MORETZ",
    "WATAUGA RIVER",
    "WEST KING",
    "WHISPERING HILLS",
    "WILDCAT HOLLOW",
    "WILSON RIDGE",
    "WINKLERS CREEK",
    "WONDERLAND WOODS"
  };
  
  private static final String[] CITY_LIST = new String[]{
    "BEECH MOUNTAIN",
    "BLOWING ROCK",
    "BOONE",
    "SEVEN DEVILS",
    
    "DEEP GAP",
    "SUGAR GROVE",
    "VALLE CRUCIS",
    "ZIONVILLE",
    "FOSCOE",
    
    "TODD",
    "VILAS",
    
    // Ashe County
    "ASHE",
    "CRESTON",
    "ELK",
    "FLEETWOOD",
    "NORTH FORK",
    "OLD FIELDS",
    "PINE SWAMP",
    
    // Avery County
    "AVERY",
    "BANNER ELK",
    "LINDVILLE",
    "SUGAR MOUNTAIN",
    
    // Caldwell County
    "CALDWELL",
    "GLOBE",
    "LENOIR",
    "MULBERRY",
    "PATTERSON",
    "YADKIN VALLEY",
    
    // Wilkes County
    "WILKES",
    "JOBS CABIN"
  };
  
  private static final Properties GPS_LOOKUP_TABLE = buildCodeTable(new String[]{
      "142 EVENING VIEW LN UNIT CA4",         "+36.172670,-81.760410",
      "164 EVENING VIEW LN UNIT CB1",         "+36.172530,-81.760080",
      "164 EVENING VIEW LN UNIT CB2",         "+36.172530,-81.760080",
      "164 EVENING VIEW LN UNIT CB3",         "+36.172530,-81.760080",
      "164 EVENING VIEW LN UNIT CB4",         "+36.172530,-81.760080",
      "164 EVENING VIEW LN UNIT CB5",         "+36.172530,-81.760080",
      "164 EVENING VIEW LN UNIT CB6",         "+36.172530,-81.760080",
      "164 EVENING VIEW LN UNIT CB7",         "+36.172530,-81.760080",
      "164 EVENING VIEW LN UNIT CB8",         "+36.172530,-81.760080",
      "119 FALCON TRC UNIT C111",             "+36.173320,-81.761770",
      "119 FALCON TRC UNIT C112",             "+36.173320,-81.761770",
      "119 FALCON TRC UNIT C121",             "+36.173320,-81.761770",
      "119 FALCON TRC UNIT C122",             "+36.173320,-81.761770",
      "119 FALCON TRC UNIT C131",             "+36.173320,-81.761770",
      "119 FALCON TRC UNIT C132",             "+36.173320,-81.761770",
      "135 FALCON TRC UNIT C211",             "+36.173110,-81.762090",
      "135 FALCON TRC UNIT C212",             "+36.173110,-81.762090",
      "135 FALCON TRC UNIT C221",             "+36.173110,-81.762090",
      "135 FALCON TRC UNIT C222",             "+36.173110,-81.762090",
      "135 FALCON TRC UNIT C231",             "+36.173110,-81.762090",
      "135 FALCON TRC UNIT C232",             "+36.173110,-81.762090",
      "155 FALCON TRC UNIT C311",             "+36.172990,-81.762380",
      "155 FALCON TRC UNIT C312",             "+36.172990,-81.762380",
      "155 FALCON TRC UNIT C321",             "+36.172990,-81.762380",
      "155 FALCON TRC UNIT C322",             "+36.172990,-81.762380",
      "155 FALCON TRC UNIT C331",             "+36.172990,-81.762380",
      "155 FALCON TRC UNIT C332",             "+36.172990,-81.762380",
      "171 FALCON TRC UNIT C411",             "+36.172930,-81.762700",
      "171 FALCON TRC UNIT C412",             "+36.172930,-81.762700",
      "171 FALCON TRC UNIT C421",             "+36.172930,-81.762700",
      "171 FALCON TRC UNIT C422",             "+36.172930,-81.762700",
      "171 FALCON TRC UNIT C431",             "+36.172930,-81.762700",
      "171 FALCON TRC UNIT C432",             "+36.172930,-81.762700",
      "193 HIDDEN VISTA",                     "+36.170390,-81.763960",
      "231 INDIGO OVERLOOK UNIT 22A",         "+36.169730,-81.765860",
      "231 INDIGO OVERLOOK UNIT 22B",         "+36.169690,-81.765970"
     
  });
}
