package net.anei.cadpage.parsers.NC;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.anei.cadpage.parsers.MsgInfo.Data;
import net.anei.cadpage.parsers.MsgInfo.MsgType;
import net.anei.cadpage.parsers.dispatch.DispatchSouthernPlusParser;
/**
 * Brunswick County, NC
 */
public class NCBrunswickCountyParser extends DispatchSouthernPlusParser {
  
  public NCBrunswickCountyParser() {
    super(CITY_LIST, "BRUNSWICK COUNTY", "NC", 
          DSFLG_OPT_DISP_ID|DSFLG_ADDR|DSFLG_ADDR_TRAIL_PLACE|DSFLG_OPT_BAD_PLACE|DSFLG_OPT_CODE|DSFLG_OPT_UNIT1|DSFLG_ID|DSFLG_TIME,
          "[A-Z]{1,3}\\d+|\\d{1,4}[A-Z]{0,4}\\d?|bcso_a911_command|pio_bcso_only|pio"); 
    setupMultiWordStreets(MWORD_STREET_LIST);
    addRoadSuffixTerms("WYND");
    removeWords("CIRCLE", "COURT", "COVE", "PLACE", "STREET", "SQUARE", "TERRACE", "TRAIL");
    setupSaintNames("BRENDAN", "JAMES", "LUKE");
    setupProtectedNames("J AND E");
  }
  
  @Override
  public String getFilter() {
    return "pagegate@brunswickes.com,pagegate@brunswick911.com,vtext.com@returns.groups.yahoo.com";
  }
  
  @Override
  public int getMapFlags() {
    return MAP_FLG_PREFER_GPS;
  }
  
  private static final Pattern RUN_REPORT_PTN = Pattern.compile("(?:\\d+:)?(\\d\\d-\\d{6})[,;] *(.*)");
  private static final Pattern STACKED_PTN = Pattern.compile("(\\S+ (?:stacked|removed from stack) on) +");
  private static final Pattern PAREN_PREFIX_PTN = Pattern.compile("(?:\\d+ +)?(?:\\([^\\)]*?\\) *)+");
  
  
  @Override
  public boolean parseMsg(String subject, String body, Data data) {
    subject = subject.trim();
    if (subject.startsWith("[") && subject.endsWith("]")) {
      data.strSource = subject.substring(1,subject.length()-1).trim();
      subject = "";
    }
    
    Matcher match = RUN_REPORT_PTN.matcher(body);
    if (match.matches()) {
      setFieldList("ID INFO");
      data.msgType = MsgType.RUN_REPORT;
      data.strCallId = match.group(1);
      data.strSupp = match.group(2).replace('|', '\n');
      return true;
    }
    
    String stacked = "";
    match = STACKED_PTN.matcher(body);
    if (match.lookingAt()) {
      stacked = match.group(1);
      body = body.substring(match.end());
    }
    if (!super.parseMsg(subject, body,  data)) return false;
    data.strCall = append(stacked, " ", data.strCall);
    
    match = PAREN_PREFIX_PTN.matcher(data.strAddress);
    if (match.lookingAt()) {
      data.strPlace = append(data.strPlace, " ", match.group().trim());
      data.strAddress = data.strAddress.substring(match.end());
    }
    return true;
  }
  
  @Override
  public String getProgram() {
    return "SRC " + super.getProgram();
  }
  
  @Override
  protected int getExtraParseAddressFlags() {
    return FLAG_AT_PLACE;
  }
  
  @Override
  public Field getField(String name) {
    if (name.equals("ADDR")) return new MyAddressField();
    return super.getField(name);
  }
  
  private class MyAddressField extends BaseAddressField {
    @Override
    public void parse(String field, Data data) {
      field = fixParenField(field, true);
      super.parse(field, data);
      data.strPlace = fixParenField(data.strPlace, false);
      data.strAddress = fixParenField(data.strAddress, false);
      data.strApt = fixParenField(data.strApt, false);
      if (data.strApt.startsWith("(")) {
        data.strAddress = append(data.strAddress, " ", data.strApt.replace('/', '&'));
        data.strApt = "";
      }
    }
  }
  
  private static String fixParenField(String field, boolean encode) {
    Matcher match = (encode ? PAREN_FLD1 : PAREN_FLD2).matcher(field);
    if (!match.find()) return field;
    StringBuffer sb = new StringBuffer();
    do {
      String term = match.group();
      if (encode) {
        term = term.replace("(", "_<_").replace(")", "_>_").replace(' ', '_');
      } else {
        term = term.replace("_<_", "(").replace("_>_", ")").replace('_', ' ');
      }
      match.appendReplacement(sb, term);
    } while (match.find());
    match.appendTail(sb);
    return sb.toString();
  }
  private static final Pattern PAREN_FLD1 = Pattern.compile("(\\([^\\(\\)]*\\))");
  private static final Pattern PAREN_FLD2 = Pattern.compile("(_<_[^<>]*_>_)");
  
  @Override
  protected boolean isNotExtraApt(String apt) {
    if (apt.indexOf('/') >= 0) return true;
    if (apt.startsWith("TO ")) return true;
    if (apt.contains(" TO ")) return true;
    return super.isNotExtraApt(apt);
  }

  @Override
  public String adjustMapAddress(String addr) {
    addr = addr.replace("CUT-OFF", "CUTOFF");
    return super.adjustMapAddress(addr);
  }
  
  private static final String[] MWORD_STREET_LIST = new String[]{
    "A I CLEMMONS",
    "AL HAYNES",
    "ALTON LENNON",
    "AMBER PINES",
    "ANDREW JACKSON",
    "ANGELS GIFT",
    "ANNIE M BRYANT",
    "ARBOR BRANCH",
    "ARNOLD PALMER",
    "ASH-LITTLE RIVER",
    "BALD CYPRESS",
    "BANKS LOOP",
    "BAY POINTE",
    "BAY TREE",
    "BEAR TRAP",
    "BEAVER CREEK",
    "BEECH TREE",
    "BELL SWAMP",
    "BENT OAK",
    "BIG MACEDONIA",
    "BIG NECK",
    "BILL HOLDEN",
    "BIRCH POND",
    "BLACK CHESTNUT",
    "BLACK SKIMMER",
    "BLUE BANKS LOOP",
    "BLUFF HILL",
    "BOB WHITE",
    "BOILING SPRING",
    "BOONES NECK",
    "BROWN PELICAN",
    "BRUNSWICK FOREST",
    "CAPE FEAR NATIONAL",
    "CAPTAINS WHEEL",
    "CAROLINA SHORES",
    "CASWELL BEACH",
    "CEDAR CREST",
    "CEDAR HILL",
    "CEDAR LOOP",
    "CHAPPELL LOOP",
    "CHERRY LAUREL",
    "CLUB VILLAS",
    "COASTAL HORIZONS",
    "COL WILLIAM RHETT",
    "COLONIAL LANDING",
    "CORN PLANTERS",
    "COTTON MILL",
    "COTTON PATCH",
    "COUNTRY CLUB VILLA",
    "COUNTRY CLUB",
    "CROOKED GULLEY",
    "CROWN CREEK",
    "DALES HAVEN",
    "DAWS CREEK",
    "DREW BRANCH",
    "DUCK HAVEN",
    "DUTCH ELM",
    "EARL OF CRAVEN",
    "EAST BEACH",
    "EAST CHATMAN",
    "EAST COAST",
    "EGRET NEST",
    "EXECUTIVE PARK",
    "FARMERS CHAPEL",
    "FIELD PLANTERS",
    "FIELD VIEW",
    "FIFTY LAKES",
    "FISH FACTORY",
    "FLETCHER HEWETT",
    "FOREST HILL",
    "FORT HOLMES",
    "FOUR SEASONS",
    "FOX SQUIRREL",
    "FREEDOMS STAR",
    "FRINK LAKE",
    "GATE 10 COUNTRY CLUB",
    "GENOES POINT",
    "GEORGE DANIELS",
    "GEORGE II",
    "GEORGE III",
    "GOLEY HEWETT",
    "GOLF CREST",
    "GOOSE CREEK",
    "GOOSE NECK",
    "GORES LANDING",
    "GOVERNMENT CENTER",
    "GRAY BRIDGE",
    "GREAT EGRET",
    "GREAT OAK",
    "GREEN HILL",
    "GREEN LOOP",
    "GREEN SWAMP",
    "HALE SWAMP",
    "HARBOR BAY",
    "HAZELS BRANCH",
    "HEATHERS GLEN",
    "HERON RUN",
    "HICKORY HILL",
    "HIDDEN VALLEY",
    "HIGH HILL",
    "HIGH MARKET",
    "HIGH POINT",
    "HIGHLANDS GLEN",
    "HOLDEN BEACH",
    "HOLLY HILLS",
    "HUNTERS BRANCH",
    "HUNTERS RIDGE",
    "ISLAND VIEW",
    "J SWAIN",
    "JACKEYS CREEK",
    "JOE BROOKS",
    "JOHN DAVIS",
    "JOHNNE MAE",
    "KAY TODD",
    "KINGS LYNN",
    "LAKE BUTLER",
    "LAKE PARK",
    "LAKE PAULA",
    "LAKE PEGGY",
    "LAKE SHORE",
    "LAKE TREE",
    "LANDS END",
    "LAUGHING GULL",
    "LAUREL CHERRY",
    "LAUREL VALLEY",
    "LEE BUCK",
    "LELAND SCHOOL",
    "LITTLE PRONG",
    "LITTLE SHALLOTTE RIVER",
    "LIVE OAK",
    "LONE TREE",
    "LONG BEACH",
    "LONG LEAF",
    "LORD THOMAS",
    "LORDS BRANCH",
    "LOW COUNTRY",
    "LUCIAN I FULFORD",
    "MAIN SAIL",
    "MALLORY CREEK",
    "MALMO LOOP",
    "MAPLE BRANCHES",
    "MAPLE CREEK",
    "MAPLE HILL",
    "MARKER FIFTY FIVE",
    "MARSH GROVE",
    "MARSH HEN",
    "MARSH VIEW",
    "MARY FRANCES",
    "MEADOW SUMMIT",
    "MEMBERS CLUB",
    "MIDDLE DAM",
    "MIDDLE RIVER",
    "MILL CREEK",
    "MIRROR LAKE",
    "MORNING VIEW",
    "MT MISERY",
    "MT PISGAH",
    "MT ZION CHURCH",
    "NATURAL SPRINGS",
    "NIGHT HARBOR",
    "NORTH HAMPTON",
    "NORTH PALM",
    "NORTH SHORE",
    "OAK CREST",
    "OAK GLEN",
    "OAK ISLAND",
    "OCEAN GATE",
    "OCEAN HARBOUR GOLF CLUB",
    "OCEAN ISLE BEACH",
    "OCEAN ISLE WEST",
    "OCEAN PINE",
    "OCEAN RIDGE",
    "OCEAN SOUND",
    "OCEAN VIEW",
    "OLDE REGENT",
    "OLDE WATERFORD",
    "ORCHARD LOOP",
    "OYSTER BAY",
    "PALMERS BRANCH",
    "PEA LANDING",
    "PEPPER VINE",
    "PICKET FENCE",
    "PINE HARVEST",
    "PINE VALLEY",
    "PLANTERS ROW",
    "PLAYERS CLUB",
    "POINT WINDWARD",
    "POLLY GULLEY",
    "PORT LOOP",
    "PORT ROYAL",
    "PRICES CREEK",
    "QUAIL HAVEN",
    "QUEEN ANNE",
    "RABBIT BAY",
    "RED BUG",
    "RED FOX",
    "RICEFIELD BRANCH",
    "RIDGE CREST",
    "RISING MEADOWS",
    "RIVER BLUFF",
    "ROCK CRAB",
    "ROCK CREEK",
    "ROUNDING RUN",
    "ROYAL OAK",
    "RUBY BELL",
    "SABBATH HOME",
    "SALT MEADOW",
    "SALT PINE",
    "SALT WORKS",
    "SAND PEBBLE",
    "SANDPIPER BAY",
    "SANDY CREEK",
    "SANDY GROVE",
    "SANDY HILLS",
    "SANDY RIDGE",
    "SANDY RUN",
    "SAVANNA BRANCH",
    "SAW MILL",
    "SCOTCH BONNET",
    "SEA HOLLY",
    "SEA PINES",
    "SEA TRAIL",
    "SEASHORE HILLS",
    "SELLERS COVE",
    "SHADY FOREST",
    "SHADY LOOP",
    "SHALLOTTE CROSSING",
    "SHALLOTTE POINT LOOP",
    "SHELL POINT",
    "SKEETER HAWK",
    "SOUTH CROW CREEK",
    "SOUTH DICKINSON",
    "SOUTH SHORE",
    "SOUTH WILLIS",
    "ST GEORGE",
    "ST JAMES",
    "ST KITTS",
    "STAMP ACT",
    "STAR CROSS",
    "STATION HOUSE",
    "STONE BALLAST",
    "STONE CHIMNEY",
    "STONEY BROOK",
    "STONEY CREEK",
    "STONEY POINT",
    "STONY WOODS",
    "SUGAR MAPLE",
    "SUNNY POINT",
    "SUNSET LAKES",
    "SWEET BAY",
    "SWEET GUM",
    "TATE LAKE",
    "THADDEUS LOTT",
    "THOMAS GARST",
    "TIGER LILY",
    "TOWN CENTER",
    "TOWN CREEK",
    "TOWN HALL",
    "TOWNE LAKE",
    "TRAIL PINES",
    "TURKEY TRAP",
    "UNION SCHOOL",
    "VIA OLD SOUND",
    "VILLAGE POINT",
    "WACCAMAW SCHOOL",
    "WALDEN POND",
    "WANETS LANDING",
    "WEST GATE",
    "WEST HILL",
    "WEST TRACE",
    "WET ASH",
    "WHITE MILL",
    "WHITE OAK",
    "WOOD LILY",
    "YACHT BASIN",
    "ZION CHURCH",
    "ZION HILL"
  };

  private static final String[] CITY_LIST = new String[]{
    "LOCKWOODS FOLLY",
    "NORTHWEST",
    "SHALLOTTE",
    "SMITHVILLE",
    "TOWN CREEK",
    "WACCAMAW",

    "BALD HEAD ISLAND",
    "BELVILLE",
    "BOILING SPRING LAKES           ",
    "BOLIVIA",
    "CALABASH",
    "CAROLINA SHORES",
    "CASWELL BEACH",
    "HOLDEN BEACH",
    "LELAND",
    "NAVASSA",
    "NORTHWEST",
    "OAK ISLAND",
    "OCEAN ISLE BEACH",
    "SANDY CREEK",
    "SHALLOTTE",
    "SOUTHPORT",
    "ST JAMES",
    "SUNSET BEACH",
    "VARNAMTOWN",

    "ANTIOCH",
    "ASH",
    "BATARORA",
    "BELL SWAMP",
    "BISHOP",
    "BIVEN",
    "BONAPARTE LANDING",
    "BOONES NECK",
    "BOWENSVILLE",
    "BRUNSWICK STATION",
    "CAMP BRANCH",
    "CEDAR GROVE",
    "CEDAR HILL",
    "CIVIETOWN",
    "CLAIRMONT",
    "CLARENDON",
    "COOLVALE",
    "DOE CREEK",
    "EASTBROOK",
    "EASY HILL",
    "LONGWOOD",
    "PINEY GROVE",
    "SUPPLY",
    "SUNSET HARBOR",
    "WINNABOW",
    
    // Columbus County
    "RIEGELWOOD",
    "WHITEVILLE"
  };

}
