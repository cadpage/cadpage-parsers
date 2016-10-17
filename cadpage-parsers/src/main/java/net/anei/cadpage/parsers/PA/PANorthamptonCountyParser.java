package net.anei.cadpage.parsers.PA;

import java.util.Properties;
import java.util.regex.Pattern;

import net.anei.cadpage.parsers.CodeSet;
import net.anei.cadpage.parsers.MsgInfo.Data;
import net.anei.cadpage.parsers.dispatch.DispatchB2Parser;


public class PANorthamptonCountyParser extends DispatchB2Parser {
  
  private static final Pattern MARKER = Pattern.compile(">.* Cad: ");

  public PANorthamptonCountyParser() {
    super(CITY_LIST, "NORTHAMPTON COUNTY", "PA", B2_CROSS_FOLLOWS);
    setupCallList(CALL_LIST);
    removeWords("RCH");
    setupSaintNames("FRANCIS");
    setupSpecialStreets("EDDYSIDE POOL", "NORTH BROADWAY");
    setupMultiWordStreets(MWORD_STREET_LIST);
  }
  
  @Override
  public String getFilter() {
    return "@notifync.org,14100,12101,777,411912";
  }
 
  @Override
  protected boolean isPageMsg(String body) {
    if (MARKER.matcher(body).find()) return true;
    return CALL_LIST.getCode(body) != null;
  }
  
  @Override
  public boolean parseMsg(String subject, String body, Data data) {
    data.strUnit = new Parser(subject).getLast('|');
    int pt = body.indexOf('\n');
    if (pt >= 0) body = body.substring(0,pt).trim();
    if (!super.parseMsg(body, data)) return false;
    data.strCity = convertCodes(data.strCity, CITY_ABRV_TABLE);
    if (data.strName.endsWith(" COUNTY")) {
      if (data.strCity.length() == 0) data.strCity = data.strName;
      data.strName = "";
    }
    if (data.strCity.equals("WARREN COUNTY")) data.strState = "NJ";
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
    "BLACK RIVER",
    "BLUE MOUNTAIN",
    "BRIDLE PATH",
    "BROTHER THOMAS BRIGHT",
    "BUNKER HILL",
    "BUSHKILL CENTER",
    "BUSHKILL PLAZA",
    "CEDAR PARK",
    "CHERRY HILL",
    "CINDER DUMP",
    "COAL YARD",
    "COMMERCE PARK",
    "COUNTRY CHASE",
    "COUNTRY CLUB",
    "COUNTRY SIDE",
    "COVERED BRIDGE",
    "CREEK VIEW",
    "DE MARIA",
    "DEER PATH",
    "DEPUES FERRY",
    "ECHO LAKE",
    "FAWN MEADOW",
    "FIRE CO",
    "FIVE POINTS RICHMOND",
    "FLINT HILL",
    "FOX RIDGE",
    "FRANKLIN HILL",
    "FRIARS VIEW",
    "GREEN MEADOW",
    "GREEN PINE",
    "GREEN POND",
    "HICKORY HILLS",
    "HIGH POINT",
    "HONEY SUCKLE",
    "HOPE RIDGE",
    "LARRY HOLMES",
    "LITTLE CREEK",
    "LONG LANE",
    "MARK TWAIN",
    "MARTINS CREEK-BELVIDERE",
    "MARY ANN",
    "MAUCH CHUNK",
    "MOUNTAIN VIEW",
    "MT PLEASANT",
    "MUD RUN",
    "NEWLINS MILL",
    "OAK RIDGE",
    "PARK WEST",
    "PEN ARGYL",
    "PENN DIXIE",
    "PLEASANT VIEW",
    "POLK VALLEY",
    "RASLEY HILL",
    "RED OAK",
    "ROCKY MOUNTAIN",
    "SAUCON CREEK",
    "SCENIC VIEW",
    "SILVER CREEK",
    "SILVER CREST",
    "SLATE BELT",
    "SNOW HILL",
    "SNYDERS CHURCH",
    "SOUTH MAIN",
    "SPRING BROOK",
    "STATE PARK",
    "STOKE PARK",
    "STONES CROSSING",
    "SUGAR MAPLE",
    "TOWNSHIP LINE",
    "VALLEY CENTER",
    "VALLEY VIEW",
    "VAN BUREN",
    "WAGON WHEEL",
    "WILKES BARRE",
    "WILLIAM PENN",
    "WILLIAMS CHURCH",
    "WILLOW PARK",
    "WOODS EDGE"

  };

  private static final CodeSet CALL_LIST = new CodeSet(
      "ADVANCED LIFE SUPPORT CALL",
      "ALERT 2",
      "ASSISTANCE CALL",
      "BASIC LIFE SUPPORT CALL",
      "BRUSH FIRE",
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
      "GENERAL INFORMATION",
      "HIGH OCCUPANCY FACILITY FIRE",
      "INDOOR ODOR",
      "INDUSTRIAL STRUCTURE FIRE",
      "KNOX BOX RELEASE",
      "LOCK OUT VEHICLE / BUILDING",
      "LOST PROPERTY REPORT",
      "MOVE UP ASSIGNMENT",
      "MVA EMS REQUEST",
      "MVA NONE INJURY",
      "MVA WITH ENTRAPMENT",
      "MVA WITH INJURIES",
      "MVA WITH UNKNOW INJUIRIES",
      "MVA WITH UNKNOWN INJUIRIES",
      "NOISE COMPLAINT",
      "ODOR / OTHER THAN SMOKE",
      "ODOR/OTHER THAN SMOKE",
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
      "TERRAIN / SEARCH RESCUE",
      "TERRAIN / SEARCH RESCUE SMITH",
      "TEST CALL (DO NOT DISPATCH)",
      "TRAFFIC CONTROL",
      "TREE DOWN",
      "TREE DOWN PLACE",
      "UNIT IN SERVICE",
      "UNIT OUT OF SERVICE",
      "VEHICLE FIRE",
      "WATER / DIVE RESCUE",
      "WELFARE CHECK",
      "WIRE (GENERAL PROBLEM)",
      "WIRES DOWN"
  );
  
  private static final String[] CITY_LIST = new String[]{
    "BETHLEHEM",
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
    "SALISBURY",
    "HAN LE CO",
    "HAN-LE-CO",
    
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
    "WARREN"
  };
  
  private static final Properties CITY_ABRV_TABLE = buildCodeTable(new String[]{
      "HAN LE CO",            "HANOVER TWP, LEHIGH COUNTY",
      "HAN-LE-CO",            "HANOVER TWP, LEHIGH COUNTY",
      "LEHIGH CARBON COUNTY", "LEHIGH, CARBON COUNTY",
      "BUCKS CO",             "BUCKS COUNTY",
      "BUCKS",                "BUCKS COUNTY",
      "CARBON CO",            "CARBON COUNTY",
      "CARBON",               "CARBON COUNTY",
      "LEHIGH CO",            "LEHIGH COUNTY",
      "MONROE CO",            "MONROE COUNTY",
      "MONROE",               "MONROE COUNTY",
      "WARREN CO",            "WARREN COUNTY",
      "WARREN",               "WARREN COUNTY"
  });
}
