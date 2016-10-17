package net.anei.cadpage.parsers.VA;

import net.anei.cadpage.parsers.MsgInfo.Data;
import net.anei.cadpage.parsers.dispatch.DispatchSouthernPlusParser;

/**
 * Botetourt County, VA
 */
public class VABotetourtCountyParser extends DispatchSouthernPlusParser {
  
  public VABotetourtCountyParser() {
    super(CITY_LIST, "BOTETOURT COUNTY", "VA", DSFLAG_OPT_DISPATCH_ID | DSFLAG_BOTH_PLACE | DSFLAG_FOLLOW_CROSS);
    setupMultiWordStreets(MWORD_STREET_LIST);
    setupSpecialStreets("AVERY ROW", "LITTLE TIMBER RDG");
    removeWords("COURT", "PARKWAY");
  }

  @Override
  public String getFilter() {
    return "@botetourtva.us";
  }
  
  @Override
  protected boolean parseMsg(String subject, String body, Data data) {
    if (!super.parseMsg(subject, body, data)) return false;
    data.strCall = stripFieldEnd(data.strCall, "-");
    if (data.strCity.endsWith(" CO")) data.strCity += "UNTY";
    return true;
  }
  
  private static final String[] MWORD_STREET_LIST = new String[]{
    "ALLEN BRANCH",
    "ARCH MILL",
    "BALL PARK",
    "BEAVER DAM",
    "BLUE BIRD",
    "BLUE RIDGE",
    "BLUE RIDGE SPRINGS",
    "BORDEN RUN",
    "BORE AUGER",
    "BRECKINRIDGE MILL",
    "BREEDENS BOTTOM",
    "BRUGHS MILL",
    "BRUNSWICK FORGE",
    "CABIN HALLOW",
    "CAMP JAYCEE",
    "CARTMILLS GAP",
    "CATAWBA CREEK",
    "CEDAR BLUFF",
    "CHESTNUT HILL",
    "CHURCH HILL",
    "COLEN HOLLOW",
    "COLING HOLLOW",
    "COPPS HILL",
    "COUNTRY CLUB",
    "COUNTRY VIEW",
    "COYNER SPRINGS",
    "CRAIG CREEK",
    "CROSS CREEK",
    "CURRY CREEK",
    "DEER HAVEN",
    "EAGLES NEST",
    "EAST CLEO",
    "EDWARDS CYCLE",
    "ELLIS RUN",
    "EMERALD LAKE",
    "FARM VIEW",
    "FIRE TOWER",
    "FOREST OAKS",
    "FORK FARM",
    "FORK VIEW",
    "FOUR SEASONS",
    "GALA LOOP",
    "GARDEN MOUNTAIN",
    "GILMERS MILL",
    "GLEN WILTON",
    "GOOSE CREEK VALLEY",
    "GRACE HOLLOWER",
    "GRAVEL HILL",
    "GRAVELLY RIDGE",
    "GREENWAY HOLLOW",
    "GREY FOX",
    "GROVE HILL",
    "HAWTHORNE HALL",
    "HEALING SPRINGS",
    "IDLE ACRES",
    "INDIAN ROCK",
    "IVORY CREEK",
    "IVY MOUNTAIN",
    "JAMES RIVER",
    "JAY RIDGE",
    "JENNINGS CREEK",
    "KYLES MILL",
    "LAKE CATHERINE",
    "LAPSLEY RUN",
    "LAUREL RIDGE",
    "LEES GAP",
    "LEONARD FARM",
    "LITTLE CATAWBA CREEK",
    "LITTLE MOUNTAIN",
    "LITTLE VALLEY",
    "LONG RIDGE",
    "LONG RUN",
    "LONG VIEW",
    "MAGNOLIA SPRINGS",
    "MAJOR WADE",
    "MAPLE LEAF",
    "MARKET RIDGE",
    "MCKINNEY HOLLOW",
    "MISTY HILLS",
    "MORNING DOVE",
    "MOUNT JOY",
    "MOUNTAIN PASS",
    "MT BEULAH",
    "MT MORIAH",
    "MT PLEASANT CHURCH",
    "MURRAY FARM",
    "NARROW PASSAGE",
    "NORTH COMMERCE",
    "NORTH ROANOKE",
    "NORTH ROME",
    "OAK RIDGE",
    "PARK VISTA",
    "PEACHTREE VALLEY",
    "PENN HOLLOW",
    "PLEASANT VIEW",
    "POOR FARM",
    "PRICES BLUFF",
    "RAINBOW FOREST",
    "READ MOUNTAIN",
    "RED HORSE",
    "ROARING RUN",
    "ROCK SPRING",
    "ROLLING MEADOW",
    "SALT PETRE CAVE",
    "SANTANA ESTATES",
    "SESSLER MILL",
    "SHADE HOLLOW",
    "SHILOH CHURCH",
    "SHORT HILL",
    "SINKING CREEK",
    "SOLDIERS RETREAT",
    "SOUTH CENTER",
    "SPREADING SPRING",
    "SPRING HOLLOW",
    "ST CLAIR",
    "SUGAR TREE HOLLOW",
    "SWITZER MOUNTAIN",
    "TIMBER CREEK",
    "TINKER MOUNTAIN",
    "VAN NESS HOLLOW",
    "WALKER ORCHARD",
    "WALNUT MANOR",
    "WEBSTER HEIGHTS",
    "WEEPING WILLOW",
    "WELCHES RUN",
    "WEST LYNCHBURG SALEM",
    "WEST MAIN",
    "WEST WIND",
    "WHITE CHURCH",
    "WHITE TAIL",
    "WINCE HOLLOW",
    "WINDY HILL"
  };

  private static final String[] CITY_LIST = new String[]{
    
    // Towns
    "BUCHANAN",
    "FINCASTLE",
    "TROUTVILLE",

    // Unincorporated Communities
    "ARCADIA",
    "BLUE RIDGE",
    "CLOVERDALE",
    "DALEVILLE",
    "EAGLE ROCK",
    "GLEN WILTON",
    "HOLLINS",
    "LITHIA",
    "NACE",
    "ORISKANY",
    "ROANOKE",
    "SPRINGWOOD",
    
    // Alleghany County
    "ALLEGHANY",
    "ALLEGHANY CO",
    "CLIFTON FORGE",
    
    // Bedford County
    "BEDFORD",
    "BEDFORD CO",
    "MONTVALE",
    "THAXTON",
    
    // Craig County,
    "CRAIG",
    "CRAIG CO",
    
    // Roanoke County
    "ROANOKE CO",
    
    // Rockbridge County
    "ROCKBRIDGE",
    "ROCKBRIDGE CO",
    "NATURAL BRIDGE STATION",
    
    // Independent cities
    "ROANOKE CITY"
  }; 
}
