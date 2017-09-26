package net.anei.cadpage.parsers.PA;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Properties;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.anei.cadpage.parsers.CodeSet;
import net.anei.cadpage.parsers.MsgInfo.Data;
import net.anei.cadpage.parsers.dispatch.DispatchB2Parser;


public class PANorthamptonCountyParser extends DispatchB2Parser {

  public PANorthamptonCountyParser() {
    super(CITY_LIST, "NORTHAMPTON COUNTY", "PA", B2_OPT_CALL_CODE | B2_CROSS_FOLLOWS);
    setupCallList(CALL_LIST);
    removeWords("RCH");
    setupSaintNames("FRANCIS");
    setupSpecialStreets("EDDYSIDE POOL", "NORTH BROADWAY");
    setupMultiWordStreets(MWORD_STREET_LIST);
  }
  
  @Override
  public String getFilter() {
    return "@notifync.org,14100,12101,777,411912,4702193914,no-reply@ecnalert.com,no-reply@onsolve.com";
  }
  
  private static final Pattern MARKER = Pattern.compile(">.* Cad: ");
 
  @Override
  protected boolean isPageMsg(String body) {
    if (MARKER.matcher(body).find()) return true;
    return CALL_LIST.getCode(body) != null;
  }
  
  private static final Pattern UNIT_MARKER = Pattern.compile("([A-Za-z]+\\d+) > ");
  @Override
  public boolean parseMsg(String subject, String body, Data data) {
    if (subject.equals("Alert Message")) {
      Matcher match = UNIT_MARKER.matcher(body);
      if (!match.lookingAt()) return false;
      data.strUnit = match.group(1);
      body = body.substring(match.end()).trim();
    } else {
      data.strUnit = new Parser(subject).getLast('|');
    }
    int pt = body.indexOf('\n');
    if (pt >= 0) body = body.substring(0,pt).trim();
    if (!super.parseMsg(body, data)) return false;
    data.strCity = convertCodes(data.strCity, CITY_ABRV_TABLE);
    if (data.strName.endsWith(" COUNTY")) {
      if (data.strCity.length() == 0) data.strCity = data.strName;
      data.strName = "";
    }
    if (NJ_CITY_TABLE.contains(data.strCity.toUpperCase())) data.strState = "NJ";
    return true;
  }
  
  @Override
  public String getProgram() {
    return "UNIT " + super.getProgram().replace("CITY", "CITY ST");
  }
  
  @Override
  protected boolean parseAddrField(String field, Data data) {
    field = BLC_PTN.matcher(field).replaceAll("BLK");
    field = BLK_OF_PTN.matcher(field).replaceAll("BLK");
    return super.parseAddrField(field, data);
  }
  private static final Pattern BLC_PTN = Pattern.compile("\\bBLC\\b");
  private static final Pattern BLK_OF_PTN = Pattern.compile("\\bBLK +OF\\b");

  @Override
  protected void parseAddress(StartType sType, int flags, String address, Data data) {
    if (address.endsWith("@ FIRE SCHOOL")) flags |= FLAG_IGNORE_AT;
    super.parseAddress(sType, flags, address, data);
  }
  
  private static final String[] MWORD_STREET_LIST = new String[]{
      "ALUTA MILL",
      "BABBLING BROOK",
      "BANGOR JUNCTION",
      "BANGOR VEIN",
      "BETH BATH",
      "BETHLEHEM TWP",
      "BILL SCOTT",
      "BLACK RIVER",
      "BLOSSOM HILL",
      "BLUE MOUNTAIN",
      "BOCCE CLUB",
      "BRIDLE PATH",
      "BROTHER THOMAS BRIGHT",
      "BUNKER HILL",
      "BUSHKILL CENTER",
      "BUSHKILL PLAZA",
      "CEDAR PARK",
      "CHASE HOLLOW",
      "CHERRY HILL",
      "CHRISTIAN SPRING",
      "CINDER DUMP",
      "COAL YARD",
      "COMMERCE PARK",
      "COUNTRY CHASE",
      "COUNTRY CLUB",
      "COUNTRY SIDE",
      "COUNTY LINE",
      "COVERED BRIDGE",
      "CREEK VIEW",
      "DE MARIA",
      "DEER PATH",
      "DEL HAVEN",
      "DELABOLE JUNCTION",
      "DEPUES FERRY",
      "EAST LAWN",
      "ECHO LAKE",
      "ECHO RIDGE",
      "FAWN MEADOW",
      "FIRE CO",
      "FIVE POINTS RICHMOND",
      "FLINT HILL",
      "FOUL RIFT",
      "FOX RIDGE",
      "FRANKLIN HILL",
      "FRIARS VIEW",
      "GLENDON HILL",
      "GREEN MEADOW",
      "GREEN PINE",
      "GREEN POND",
      "HICKORY HILL",
      "HICKORY HILLS",
      "HIGH POINT",
      "HONEY SUCKLE",
      "HOPE RIDGE",
      "LAKE MINSI",
      "LARRY HOLMES",
      "LITTLE CREEK",
      "LOCKE HEIGHTS",
      "LONG LANE",
      "LORD BYRON",
      "MARK TWAIN",
      "MARTINS CREEK BELVIDERE",
      "MARTINS CREEK-BELVIDERE",
      "MARY ANN",
      "MAUCH CHUNK",
      "MOCKINGBIRD HILL",
      "MORAVIAN HALL",
      "MORGAN HILL",
      "MOUNTAIN VIEW",
      "MT BETHEL",
      "MT PLEASANT",
      "MT VERNON",
      "MUD RUN",
      "NEWLINS MILL",
      "OAK RIDGE",
      "PARK RIDGE",
      "PARK WEST",
      "PEACH TREE",
      "PEN ARGYL",
      "PENN ALLEN",
      "PENN DIXIE",
      "PINE HURST",
      "PLEASANT VIEW",
      "POLK VALLEY",
      "QUARTER MILE",
      "RASLEY HILL",
      "RED OAK",
      "RITZ CRAFT",
      "ROCKY MOUNTAIN",
      "ROSE INN",
      "SAUCON CREEK",
      "SCENIC VIEW",
      "SHERRY HILL",
      "SILVER CREEK",
      "SILVER CREST",
      "SLATE BELT",
      "SMITH GAP",
      "SNOW HILL",
      "SNYDERS CHURCH",
      "SOUTH MAIN",
      "SPRING BROOK",
      "SPRING GARDEN",
      "SPRING VALLEY",
      "SPRINGTOWN HILL",
      "ST JOHN",
      "STATE PARK",
      "STEPHEN CRANE",
      "STOCKER MILL",
      "STOKE PARK",
      "STONE CHURCH",
      "STONES CROSSING",
      "SUGAR MAPLE",
      "TOWNSHIP LINE",
      "VALLEY CENTER",
      "VALLEY VIEW",
      "VAN BUREN",
      "VERA CRUZ",
      "WAGON WHEEL",
      "WALKING PURCHASE",
      "WEYHILL FARM",
      "WHISPERING ACRES",
      "WHITE OAK",
      "WILKES BARRE",
      "WILLIAM C",
      "WILLIAM PENN",
      "WILLIAMS CHURCH",
      "WILLOW PARK",
      "WOODS EDGE"
  };

  private static final CodeSet CALL_LIST = new CodeSet(
      "911 INCIDENT (TYPE)",
      "ADVANCED LIFE SUPPORT CALL",
      "AIRCRAFT DOWN",
      "ALERT 2",
      "ANIMAL COMPLAINT",
      "ASSISTANCE CALL",
      "BASIC LIFE SUPPORT CALL",
      "BOMB THREAT \\ ATTEMPT \\ FOUND",
      "BRUSH FIRE",
      "CARBON MONOXIDE - NO SYMPTOMS",
      "CARBON MONOXIDE (NO SYMPTOMS)",
      "CARBON MONOXIDE - SYMPTOMS",
      "COMMERCIAL STRUCTURE FIRE",
      "CONTROL BURN / BURN COMPLAINT",
      "DISABLED VEHICLE",
      "DWELLING FIRE",
      "ELEVATOR RESCUE",
      "FIRE ALARM",
      "FIRE GENERIC (TYPE)",
      "FIRE TRANSFORMER / WIRES",
      "FOLLOW UP",
      "FOOT / VEHICLE PURSUIT",
      "GAS OTHER LEAK\\ALARM INDOOR",
      "GAS OTHER LEAK\\ALARM OUTDOOR",
      "GAS STRIKE",
      "GENERAL INFORMATION",
      "HIGH OCCUPANCY FACILITY FIRE",
      "INDOOR ODOR",
      "INDUSTRIAL STRUCTURE FIRE",
      "KNOX BOX RELEASE",
      "LOCK OUT VEHICLE / BUILDING",
      "LOST PROPERTY REPORT",
      "MISSING PERSON / RUNAWAY",
      "MOVE UP ASSIGNMENT",
      "MVA EMS REQUEST",
      "MVA NON INJURY",
      "MVA NONE INJURY",
      "MVA UNKNOWN INJURIES",
      "MVA WITH ENTRAPMENT",
      "MVA WITH INJURIES",
      "MVA WITH UNKNOW INJUIRIES",
      "MVA WITH UNKNOWN INJURIES",
      "MVA WITH UNKNOWN INJUIRIES",
      "NG\\LPG LEAK\\ALARM INDOOR",
      "NG\\LPG LEAK\\ALARM OUTDOOR",
      "NOISE COMPLAINT",
      "ODOR / OTHER THAN SMOKE",
      "ODOR/OTHER THAN SMOKE INDOOR",
      "ODOR/OTHER THAN SMOKE OUTDOOR",
      "OUTDOOR SMOKE INVESTIGATION",
      "PHONE CALL",
      "PUMP DETAIL",
      "RESCUE (TYPE)",
      "ROAD HAZARD",
      "RUBBISH / TRASH FIRE",
      "SETUP AIRCRAFT LANDING ZONE",
      "SPECIAL ASSIGNMENT",
      "SPILL (TYPE)",
      "STATION IN SERVICE",
      "STATION OUT OF SERVICE",
      "STRUCTURE FIRE",
      "SUSPICIOUS ACTIVITY",
      "TERRAIN / SEARCH RESCUE",
      "TERRAIN / SEARCH RESCUE SMITH",
      "TEST (DO NOT DISPATCH)",
      "TEST CALL (DO NOT DISPATCH)",
      "TONE(S) TEST",
      "TRAFFIC CONTROL",
      "TRAFFIC STOP",
      "TREE DOWN",
      "TREE DOWN PLACE",
      "UNIT IN SERVICE",
      "UNIT OUT OF SERVICE",
      "VEHICLE FIRE",
      "WARRANT SERVICE",
      "WATER / DIVE RESCUE",
      "WATER MAIN\\LINE BREAK",
      "WELFARE CHECK",
      "WIRE (GENERAL PROBLEM)",
      "WIRES DOWN"
  );
  
  private static final String[] CITY_LIST = new String[]{
    "BETH CITY",
    "BETHLEHEM",
    "BETHELEHM",  // Misspelled
    "EASTON",

    "BANGOR",
    "BATH",
    "CHAPMAN",
    "EAST ALLEN",
    "EAST BANGOR",
    "FREEMANSBURG",
    "GLENDON",
    "HELLERTOWN",
    "NAZARETH",
    "NORTH CATASAUQUA",
    "NORTHAMPTON",
    "PEN ARGYL",
    "PORTLAND",
    "ROSETO",
    "STOCKERTOWN",
    "TATAMY",
    "WALNUTPORT",
    "WEST EASTON",
    "WILSON",
    "WIN GAP",
    "WIND GAP",

    "ALLEN TWP",
    "BETHLEHEM TWP",
    "BUSHKILL TWP",
    "EAST ALLEN TWP",
    "FORKS TWP",
    "HANOVER TWP",
    "LEHIGH TWP",
    "LOWER MOUNT BETHEL TWP",
    "LOWER MNT BETHEL",
    "LOWER MT BETHEL",
    "L MOUNT BETHEL",
    "LOWER NAZARETH TWP",
    "LOWER SAUCON TWP",
    "MOORE TWP",
    "PALMER TWP",
    "PLAINFIELD TWP",
    "UPPER MOUNT BETHEL TWP",
    "UPPER MNT BETHEL",
    "UPPER MT BETHEL",
    "U MOUNT BETHEL",
    "UPPER NAZARETH TWP",
    "WASHINGTON TWP",
    "WILLIAMS TWP",

    "ALLEN",
    "BETHLEHEM",
    "BUSHKILL",
    "EAST ALLEN",
    "FORKS",
    "HANOVER",
    "LEHIGH",
    "LOWER MOUNT BETHEL",
    "LOWER NAZARETH",
    "LOWER SAUCON",
    "MOORE",
    "PALMER",
    "PLAINFIELD",
    "UPPER MOUNT BETHEL",
    "UPPER NAZARETH",
    "WASHINGTON",
    "WILLIAMS",
    
    // Bucks County
    "BUCKS COUNTY",
    "BUCKS CO",
    "BUCKS",
    "NOCKAMIXON",
    
    // Carbon County
    "CARBON COUNTY",
    "CARBON CO",
    "CARBON",
    "LEHIGH CARBON COUNTY",
    
    // Lehigh County
    "LEHIGH COUNTY",
    "LEHIGH CO",
    "FOUNTAIN HILL",
    "NORTH WHITEHALL",
    "SALISBURY",
    "SAILSBURY",
    "SALSBURY",
    "SALISB URY",
    "HAN LE CO",
    "HAN-LE-CO",
    "UPPER SAUCON",
    "UPPER SAUCON TWP",
    "UPPER SAUCON LEHIGH",
    
    // Monroe County
    "MONROE COUNTY",
    "MONROE CO",
    "MONROE",
    "HAMILTON TWP",
    "HAMILTON",
    "ROSS TWP",
    "ROSS",
    
    // Warren County
    "WARREN COUNTY",
    "WARREN CO",
    "WARREN",
    "POHATCONG TWP"
  };
  
  private static final Set<String> NJ_CITY_TABLE = new HashSet<String>(Arrays.asList(
      "WARREN COUNTY",
      "WARREN CO",
      "WARREN",
      "POHATCONG TWP"
  ));
  
  private static final Properties CITY_ABRV_TABLE = buildCodeTable(new String[]{
      "HAN LE CO",            "HANOVER TWP, LEHIGH COUNTY",
      "HAN-LE-CO",            "HANOVER TWP, LEHIGH COUNTY",
      "LEHIGH CARBON COUNTY", "LEHIGH, CARBON COUNTY",
      "BETH CITY",            "BETHLEHEM",
      "BETHELEHM",            "BETHLEHEM",
      "BUCKS CO",             "BUCKS COUNTY",
      "BUCKS",                "BUCKS COUNTY",
      "CARBON CO",            "CARBON COUNTY",
      "CARBON",               "CARBON COUNTY",
      "LEHIGH CO",            "LEHIGH COUNTY",
      "MONROE CO",            "MONROE COUNTY",
      "MONROE",               "MONROE COUNTY",
      "SAILSBURY",            "SALISBURY",
      "SALISB URY",           "SALISBURY",
      "SALSBURY",             "SALISBURY",
      "UPPER SAUCON LEHIGH",  "UPPER SAUCON",
      "WARREN CO",            "WARREN COUNTY",
      "WARREN",               "WARREN COUNTY",
      "WIN GAP",              "WIND GAP"
  });
}
