package net.anei.cadpage.parsers.NC;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.anei.cadpage.parsers.MsgInfo.Data;
import net.anei.cadpage.parsers.dispatch.DispatchSouthernParser;



public class NCMontgomeryCountyParser extends DispatchSouthernParser {


  public NCMontgomeryCountyParser() {
    super(CITY_LIST, "MONTGOMERY COUNTY", "NC", DSFLG_PROC_EMPTY_FLDS | DSFLG_OPT_DISP_ID | DSFLG_ADDR | DSFLG_ADDR_LEAD_PLACE | DSFLG_ADDR_TRAIL_PLACE | DSFLG_OPT_CODE | DSFLG_OPT_ID | DSFLG_TIME);
    allowBadChars("()");
    removeWords("LA");
    setupMultiWordStreets(MWORD_STREET_LIST);
  }

  @Override
  public String getFilter() {
    return "@montgomerycountync.com";
  }

  @Override
  public int getMapFlags() {
    return MAP_FLG_PREFER_GPS;
  }

  private static final Pattern BAD_MSG_PTN = Pattern.compile("CAD:.*;.*;.*;.*");

  @Override
  public boolean parseMsg(String body, Data data) {

    // Reject anything that looks like a Davidson County call
    if (BAD_MSG_PTN.matcher(body).matches()) return false;

    body = body.replace('\\', '/');

    body = stripFieldStart(body, "CAD:");
    if (!super.parseMsg(body, data)) return false;

    // Fixup address construct
    data.strAddress = data.strAddress.replace(" 24 & 27", " 24/27");

    // Fixups for name field
    if (data.strName.length() > 0) {
      data.strName = stripFieldStart(data.strName, "/");
      if (data.strCity.length() == 0 && isCity(data.strName)) {
        data.strCity = data.strName;
        data.strName = "";
      } else if (data.strPlace.length() == 0) {
        data.strPlace = data.strName;
        data.strName = "";
      }
    }

    if (data.strCity.equalsIgnoreCase("MONT CO")) {
      data.strCity = "MONTGOMERY COUNTY";
    } else if (data.strCity.endsWith(" CO")) {
      data.strCity += "UNTY";
    } else if (data.strCity.endsWith(" Co") || data.strCity.endsWith(" co")) {
      data.strCity += "unty";
    }
    return true;
  }

  @Override
  protected void parseExtra(String sExtra, Data data) {
    Parser p = new Parser(sExtra);
    data.strCall = p.get(" - ");
    data.strSupp = p.get();
  }

  @Override
  protected boolean isNotExtraApt(String apt) {
    if (apt.startsWith("(")) return true;
    return super.isNotExtraApt(apt);
  }

  @Override
  public String adjustMapAddress(String addr) {
    addr = NC24_27_PTN.matcher(addr).replaceAll("24");

    // Eliminated double interstctions
    Matcher match = MULT_X_PTN.matcher(addr);
    if (match.matches()) addr = match.group(1);
    return addr;
  }

  private static Pattern NC24_27_PTN = Pattern.compile("\\b24[-/]27(?: [EW])?\\b");
  private static Pattern MULT_X_PTN = Pattern.compile("(.*?&.*?) *&.*");

  private static final String[] MWORD_STREET_LIST = new String[]{
    "ALVIN HARRIS",
    "APPLE ORCHARD",
    "ASBURY CHURCH",
    "ATKINS DAIRY",
    "BADIN LAKE",
    "BADIN SHORES RESORT",
    "BADIN SHORES",
    "BADIN VIEW",
    "BALL PARK",
    "BELFORD CHURCH",
    "BIRCH LANE",
    "BLACK ANKLE",
    "BRUTON CARPENTER",
    "BRUTONVILLE CHURCH",
    "BSR BALLFIELD",
    "CABIN CREEK",
    "CALVARY CHURCH",
    "CC CAMP",
    "CEDAR CREEK",
    "CHICKEN FARM",
    "CLEARVIEW POINT",
    "COGGINS MINE",
    "COLE VALLEY",
    "COTTON CREEK",
    "COTTON PLACE",
    "CRITTERS CORNER",
    "DARK SPRINGS MOUNTAIN",
    "DEATON CEMETERY",
    "DEEP WATER",
    "DEER PARK",
    "DRY CREEK",
    "DUTCH JOHN",
    "EAST ALLENTON",
    "EAST INGRAM",
    "EAST MAIN",
    "EAST SECOND",
    "EAST SPRING",
    "EMERALD SHORES",
    "FARMERS MARKET",
    "FIDDLERS GHOST",
    "FLINT HILL",
    "FLINT ROCK",
    "FLOYD FARM",
    "FOREST LAKE",
    "FOX DEN",
    "GRAND VIEW",
    "H R HOLT",
    "HARLEY RIDGE",
    "HARRIS CEMETERY",
    "HAVEN COVE",
    "HAYWOOD COUNTRY",
    "HEARNE FARM",
    "HOGAN FARM",
    "HOLLY HARBOR",
    "HOLLY HILLS",
    "HOMANIT USA",
    "INDIAN HARBOR",
    "INDUSTRIAL PARK",
    "JACK CURRIE",
    "JUBAL REEVES",
    "JULIUS CHAMBERS",
    "KU SU MEE",
    "LAKE FOREST",
    "LAKE LAND",
    "LAKE SHORE",
    "LANDS END",
    "LASSITER MILL",
    "LEMONDS DRYWALL",
    "LIBERTY HILL CHURCH",
    "LIGHTHOUSE CHURCH",
    "LILLYS BRIDGE",
    "LITTLE RIVER GOLF",
    "LOVE JOY",
    "MCAULEY FARM",
    "MCBRIDE LUMBER",
    "MCKAY HILL",
    "MCLEANS CREEK",
    "MOCCASIN CREEK",
    "MONTGOMERY SHORES",
    "MT CARMEL CHURCH",
    "NELSON STORE",
    "NORTH MAIN",
    "NORTH RUSSELL",
    "NORTH SANDY RIDGE",
    "NORTH SHORELINE",
    "NORTH STATE",
    "NORTHVIEW RDNC",
    "PEE DEE",
    "PINE BARK",
    "PINE TREE",
    "PLEASANT HILL",
    "PLEASANT VALLEY",
    "POINT OVAL",
    "POST OFFICE",
    "POWELL STORE",
    "ROSENWALD SCHOOL",
    "SANDY VALLEY",
    "SCENIC VIEW",
    "SEVEN OAKS",
    "SHADY OAK",
    "SHILOH CHURCH",
    "SHOE FACTORY",
    "SLEEPY HOLLOW",
    "SOUTH LIBERTY",
    "SOUTH MAIN",
    "SOUTH RUSSELL",
    "SOUTH SCHOOL",
    "SOUTH TOMLINSON",
    "SOUTH WADESBORO",
    "SUGAR LOAF",
    "THICKETY CREEK",
    "THOMASVILLE CHURCH",
    "TILLERY PARK",
    "UWHARRIE POINT",
    "WADEVILLE FIRE STAT",
    "WARNER FARM",
    "WASHINGTON PARK",
    "WEBB LOOP",
    "WEST ALLENTON",
    "WEST CHESTNUT",
    "WEST CLAIRMONT",
    "WEST FAIRGROUND",
    "WEST INGRAM",
    "WEST MAIN",
    "WEST SECOND",
    "WHIP O WILL",
    "WHISPER LAKE",
    "WHITE OAK",
    "WHITLEY MILL",
    "WINDEMERE POINTE",
    "WINDY HILL",
    "WOOD LAND",
    "ZION CHURCH"
  };

  private static final String[] CITY_LIST = new String[] {
    "MONT CO",

    "BISCOE",
    "BLACK ANKLE",
    "CANDOR",
    "CHEEK CREEK",
    "ELDORADO",
    "ETHER",
    "JACKSON SPRINGS",
    "LITTLE RIVER",
    "MOUNT GILEAD",
    "MT GILEAD",
    "NEW LONDON",
    "OKEEWEMEE",
    "OPHIR",
    "PEE DEE",
    "ROCKY SPRINGS",
    "SEAGROVE",
    "STAR",
    "STEEDS",
    "TROY",
    "UWHARRIE",
    "WINDBLOW",

    // Davidson County
    "DAVIDSON CO",
    "DENTON",

    // Mecklenburg County
    "MECKLENBURG CO",
    "DAVIDSON",

    // Randolph County
    "RANDOLPH CO",
    "ASHEBORO",

    // Richmond County
    "RICHMOND CO",
    "RICHMOND",

    // Stanly County
    "STANLY CO",
    "NORWOOD"


  };
}
