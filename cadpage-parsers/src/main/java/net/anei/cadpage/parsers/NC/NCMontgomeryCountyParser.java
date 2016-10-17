package net.anei.cadpage.parsers.NC;

import java.util.regex.Pattern;

import net.anei.cadpage.parsers.MsgInfo.Data;
import net.anei.cadpage.parsers.dispatch.DispatchSouthernParser;



public class NCMontgomeryCountyParser extends DispatchSouthernParser {
  
  
  public NCMontgomeryCountyParser() {
    super(CITY_LIST, "MONTGOMERY COUNTY", "NC", DSFLAG_ID_OPTIONAL | DSFLAG_LEAD_PLACE);
    allowBadChars("()");
    removeWords("LA");
    setupMultiWordStreets(MWORD_STREET_LIST);
  }
  
  @Override
  public String getFilter() {
    return "@montgomerycountync.com";
  }
  
  private static final Pattern BAD_MSG_PTN = Pattern.compile(".*;.*;.*;.*");
  
  @Override
  public boolean parseMsg(String body, Data data) {
    
    // Reject anything that looks like a Davidson County call
    if (BAD_MSG_PTN.matcher(body).matches()) return false;
    
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
    return NC24_27_PTN.matcher(addr).replaceAll("24");
  }
  private static Pattern NC24_27_PTN = Pattern.compile("\\b24[-/]27(?: [EW])?\\b");

  private static final String[] MWORD_STREET_LIST = new String[]{
    "APPLE ORCHARD",
    "BADIN LAKE",
    "BADIN SHORES",
    "BADIN SHORES RESORT",
    "BADIN VIEW",
    "BIRCH LANE",
    "BRUTONVILLE CHURCH",
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
    "FOX DEN",
    "GRAND VIEW",
    "HARRIS CEMETERY",
    "HAYWOOD COUNTRY",
    "HOGAN FARM",
    "HOLLY HARBOR",
    "HOLLY HILLS",
    "HOMANIT USA",
    "INDIAN HARBOR",
    "INDUSTRIAL PARK",
    "JACK CURRIE",
    "JUBAL REEVES",
    "JULIUS CHAMBERS",
    "LAKE FOREST",
    "LAKE LAND",
    "LASSITER MILL",
    "LIBERTY HILL CHURCH",
    "LILLYS BRIDGE",
    "LITTLE RIVER GOLF",
    "LOVE JOY",
    "MCBRIDE LUMBER",
    "MCLEANS CREEK",
    "MOCCASIN CREEK",
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
    "PLEASANT VALLEY",
    "POINT OVAL",
    "POST OFFICE",
    "POWELL STORE",
    "SANDY VALLEY",
    "SHADY OAK",
    "SHOE FACTORY",
    "SOUTH LIBERTY",
    "SOUTH MAIN",
    "SOUTH RUSSELL",
    "SOUTH SCHOOL",
    "SOUTH TOMLINSON",
    "SOUTH WADESBORO",
    "SUGAR LOAF",
    "THICKETY CREEK",
    "THOMASVILLE CHURCH",
    "UWHARRIE POINT",
    "WADEVILLE FIRE STAT",
    "WARNER FARM",
    "WASHINGTON PARK",
    "WEBB LOOP",
    "WEST ALLENTON",
    "WEST CHESTNUT",
    "WEST CLAIRMONT",
    "WEST INGRAM",
    "WEST MAIN",
    "WEST SECOND",
    "WHIP O WILL",
    "WHISPER LAKE",
    "WHITE OAK",
    "WINDY HILL",
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
