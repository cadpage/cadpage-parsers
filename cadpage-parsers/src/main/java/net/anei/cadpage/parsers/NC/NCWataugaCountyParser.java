
package net.anei.cadpage.parsers.NC;

import java.util.regex.Pattern;

import net.anei.cadpage.parsers.MsgInfo.Data;
import net.anei.cadpage.parsers.dispatch.DispatchSouthernParser;


public class NCWataugaCountyParser extends DispatchSouthernParser {

  public NCWataugaCountyParser() {
    super(CITY_LIST, "WATAUGA COUNTY", "NC",
          DSFLG_OPT_DISP_ID|DSFLG_ADDR|DSFLG_OPT_X|DSFLG_ID|DSFLG_TIME);
    setupMultiWordStreets(MWORD_STREET_LIST);
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
    int pt = sExtra.indexOf(' ');
    if (pt < 0) {
      data.strCall =  sExtra;
      return;
    }
    int pt2 = sExtra.indexOf('-', pt+1);
    if (pt2 >= 0) pt = sExtra.lastIndexOf(' ', pt2);
    data.strCall = sExtra.substring(0,pt).trim();
    data.strSupp = sExtra.substring(pt+1).trim();
  }
  
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
}
