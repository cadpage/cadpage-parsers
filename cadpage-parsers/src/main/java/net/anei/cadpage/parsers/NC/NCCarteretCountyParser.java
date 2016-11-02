package net.anei.cadpage.parsers.NC;

import java.util.Properties;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.anei.cadpage.parsers.CodeSet;
import net.anei.cadpage.parsers.MsgInfo.Data;
import net.anei.cadpage.parsers.dispatch.DispatchSouthernPlusParser;

/**
 * Carteret county, NC
 */
public class NCCarteretCountyParser extends DispatchSouthernPlusParser {
  
  public NCCarteretCountyParser() {
    super(CITY_LIST, "CARTERET COUNTY", "NC", 
          DSFLAG_OPT_DISPATCH_ID | DSFLAG_NO_NAME_PHONE | DSFLAG_ID_OPTIONAL);
    setupCallList(CALL_LIST);
  }
  
  @Override
  public String getFilter() {
    return "@carteretcountygov.org,@sealevelfire-rescue.org,vtext.com@gmail.com";
  }
  
  @Override
  public int getMapFlags() {
    return MAP_FLG_PREFER_GPS;
  }
  
  private static final Pattern CODE_CALL_PTN = Pattern.compile("(\\d{3}) +(.*)");
  
  @Override
  public boolean parseMsg(String subject, String body, Data data) {

    if (!super.parseMsg(subject, body, data)) return false;
    
    // Split code and call
    Matcher match = CODE_CALL_PTN.matcher(data.strCall);
    if (match.matches()) {
      data.strCode = match.group(1);
      data.strCall = match.group(2);
    }
    
    // Sometimes city name is duplicated in address
    // which ends up as the place name
    if (data.strCity.equals(data.strPlace)) data.strPlace = "";
    
    // Fix misspelled cities
    String fixCity = MISSPELLED_CITIES.getProperty(data.strCity.toUpperCase());
    if (fixCity != null) data.strCity = fixCity;
    return true;
  }
  
  @Override
  public String getProgram() {
    return super.getProgram().replace("CALL", "CODE CALL");
  }
  
  @Override
  protected int getExtraParseAddressFlags() {
    return FLAG_CROSS_FOLLOWS;
  }
  
  private static final Properties MISSPELLED_CITIES = buildCodeTable(new String[]{
      "MILLCREEK",      "MILL CREEK",
      "ONLSOW CO",      "ONSLOW CO",
      "PELLETIER",      "PELETIER"
  });
  
  private static final String[] MWORD_STREET_LIST = new String[]{
      "0 CHRISTINA",
      "ADAMS CREEK",
      "AMOS GILLIKIN",
      "ANITA FORTE",
      "AZALEA PLANTATION",
      "B MCLEAN",
      "BAY RIDGE",
      "BEACH HAVEN",
      "BEAUFORT MANOR",
      "BELL GRADE",
      "BLUE BIRD",
      "BLUE HERON",
      "BOGUE FOREST",
      "BOGUE INLET",
      "BOGUE LOOP",
      "BOGUE SOUND",
      "BRIDGES ST",
      "BROAD CREEK LOOP",
      "BUCKS CORNER",
      "CAPE LOOKOUT",
      "CEDAR CREEK",
      "CEDAR ISLAND",
      "CEDAR POINT",
      "CEDAR TREE",
      "CHANNEL ROCK",
      "CHIP SHOT",
      "CHRISSIE WRIGHT",
      "CLIFFORD OGLESBY",
      "CLUB COLONY",
      "COAST GUARD",
      "CORAL RIDGE",
      "COUNTRY CLUB",
      "CREEK LINE",
      "CRISSY WRIGHT",
      "CROSS CREEK",
      "CROW HILL",
      "CROWS NEST",
      "CRYSTAL OAKS",
      "CYRUS POLLARD",
      "DAVIS BAY",
      "DEEP BAY",
      "DEER CREEK",
      "DIAMOND CITY",
      "DOLPHIN RIDGE",
      "DOWN HOME",
      "DUKE MARINE LAB",
      "EAST CHATHAM",
      "EAST LADIES",
      "EAST RAILROAD",
      "EAST SEAVIEW",
      "ECHO RIDGE",
      "EGRET LAKE",
      "ELIS LANDING",
      "ELM GREEN LOOP",
      "EMERALD PLANTATION",
      "FERRY DOCK",
      "FISHER TOWN",
      "FLORIDA PARK",
      "FOREST DUNES",
      "FOREST LINE",
      "FORT MACON",
      "FULFORD WILLIS",
      "GALES CREEK",
      "GALES SHORE",
      "GEORGE TAYLOR",
      "GOLDEN FARM",
      "GOOSE BAY",
      "GOOSE CREEK",
      "GOOSE POND",
      "GREAT LAKE",
      "GREEN GLEN",
      "HARDESTY LOOP",
      "HARKERS ISLAND",
      "HARKERS POINT",
      "HIDDEN BAY",
      "HIDDEN HARBOR",
      "HOOP POLE CREEK",
      "HOWARD FARM",
      "HUNTER BROWN",
      "HUNTERS CREEK",
      "J BELL",
      "JAMES GRADY",
      "JO DOC",
      "JOHN PLATT",
      "KAROBI PARK",
      "LIGE PINER",
      "LITTLE DEEP CREEK",
      "LIVE OAK",
      "LOMA LINDA",
      "LORD BERKELEY",
      "LUCILLE LEWIS",
      "MARSH HARBOUR",
      "MARSH ISLAND",
      "MASON TOWN",
      "MAYBERRY LOOP",
      "MILL CREEK",
      "MORRIS HILL",
      "NEWPORT LOOP",
      "NINE FOOT",
      "NINE MILE",
      "NORRIS LANDING",
      "NORTH FORTY",
      "NORTH GATE",
      "NORTH GUTHRIE",
      "NORTH RIVER CLUB",
      "OAK FOREST",
      "OAK HILL",
      "OAK TREE",
      "OCEAN BLUFF",
      "OCEAN RIDGE",
      "OCEAN SHORE",
      "OLDE TOWNE YACHT CLUB",
      "OLE FIELD",
      "OSCAR HILL",
      "OTWAY FARM",
      "PAINTED DAISY",
      "PALMETTO PLACE",
      "PARK MEADOWS",
      "PELLETIER LOOP",
      "PIER POINTE",
      "PINE CREST",
      "PINE KNOLL",
      "PINE LAKE",
      "PIRATES LANDING",
      "POINTE WEST",
      "POLLY HILL",
      "POLLY WAY",
      "POOR MANS CONDO",
      "POSSUM TROT",
      "PROFESSIONAL PARK",
      "RAY DAVIS",
      "RED BARN",
      "RED DRUM",
      "RED OAK",
      "RED SORREL",
      "RIDGE WATER",
      "RIP TIDE",
      "RIVER OAKS",
      "RIVER SHORE",
      "RUSH POINT",
      "RUSSELLS CREEK",
      "SALTER HARRIS FARM",
      "SALTER PATH",
      "SALTY SHORES",
      "SAM GARNER",
      "SAM HATCHER",
      "SAND CASTLE",
      "SAND HILLS",
      "SAND RIDGE",
      "SEA DUNES",
      "SEA ISLE",
      "SEA SHELL",
      "SHADY LANDING",
      "SILVER LAKE",
      "SLEEPY POINT",
      "SNOW GOOSE",
      "SOUTH GUTHRIE",
      "SOUTH LOOP",
      "SOUTH WINDS",
      "SPARROW HAWK",
      "SPRUCE PINES",
      "STACY LOOP",
      "STAR CHURCH",
      "STEEL TANK",
      "STEEP HILL",
      "STELLA BRIDGEWAY",
      "STEPHEN WILLIS",
      "SWANSBORO LOOP",
      "T DAVIS",
      "TAYLOR FARM",
      "TAYLOR NOTION",
      "THOMAS BELL JAY",
      "TIMBER RIDGE",
      "TOM MANN",
      "TREATMENT PLANT",
      "TURTLE COVE",
      "TUTTLES GROVE",
      "TWO OAKS",
      "WALKING LEAF",
      "WAM SQUAM",
      "WATERS EDGE",
      "WEST BEAUFORT",
      "WEST FIRETOWER",
      "WEST SEAVIEW",
      "WETHERINGTON LANDING",
      "WHITE OAK",
      "WHITE OAK BLUFF",
      "WHITEHOUSE FORK",
      "WILLOW POND"

  };

  private static final CodeSet CALL_LIST = new CodeSet(
      "911 HANG UP",
      "ABDOMINAL PAIN - PROBLEMS",
      "ALARM BURGLARY",
      "ALARM FIRE",
      "ALARM HOLD UP - PANIC",
      "ALARM MEDICAL",
      "ALLERGIES - REACTION",
      "ASSAULT IP",
      "ASSIST OTH AGENCY",
      "BACK PAIN",
      "BREATHING PROBLEMS",
      "BURGLARY NIP",
      "CARDIAC ARREST",
      "CHECK WELFARE",
      "CHEST PAIN",
      "DECEASED PERSON",
      "DIABETIC PROB",
      "DIST - NUIS - FIGHT IP",
      "ELEVATOR - ESCALATOR RESCUE",
      "FALL",
      "FALLS",
      "FIRE GRASS - BRUSH - WOODS - OUTSIDE FIRE",
      "FOOT PATROL",
      "GAS LEAK - ODOR (LP or Natural Gas)",
      "HEART PROB -DEFIB",
      "HEART PROB - DEFIB PROB",
      "HEAT - COLD EXPOSURE",
      "HEMORRHAGE - LACERATIONS",
      "LOCK IN - OUT URGENT",
      "LOUD NOISE - MUSIC/BARKING/PARTY",
      "MARINE FIRE",
      "MENTAL DISORDER/BEHAVIORAL PROBLEMS",
      "MVC MINOR",
      "MVC PI OR ROLLOVER",
      "MVC UNK PI",
      "OUTSIDE FIRE",
      "OVERDOSE - POISONING",
      "PSYCHIATRIC - ABNORMAL BHAVIOR - SUICIDE ATTEMPT",
      "REQ FOR SERV - EMS",
      "SICK PERSON",
      "STAB - GUNSHOT - PENETRATING TRAUMA",
      "STROKE",
      "STRUCTURE FIRE",
      "SUICIDAL IP",
      "SUSP - WANT PERSON",
      "TRAFFIC HAZARD - ROAD RAGE",
      "TRAFFIC STOP",
      "TRANSFER (MEDICAL) INTERFACILITY",
      "TRAUMATIC INJURIES",
      "UNCONSCIOUS - FAINTING",
      "VEHICLE FIRE",
      "WATERCRAFT IN DISTRESS"
  );

  private static final String[] CITY_LIST = new String[]{
    "ATLANTIC BEACH",
    "BEAUFORT",
    "BOGUE",
    "CAPE CARTERET",
    "CEDAR POINT",
    "EMERALD ISLE",
    "INDIAN BEACH",
    "MOREHEAD CITY",
    "NEWPORT",
    "PELETIER",
    "PELLETIER",   // Misspelled
    "PINE KNOLL SHORES",
    
    "ATLANTIC",
    "BETTIE",
    "BROAD CREEK",
    "CEDAR ISLAND",
    "DAVIS",
    "GALES CREEK",
    "GLOUCESTER",
    "HARKERS ISLAND",
    "HARLOWE",
    "LOLA",
    "MARSHALLBERG",
    "MERRIMON",
    "MILL CREEK",
    "MILLCREEK",
    "NORTH RIVER",
    "OCEAN",
    "OTWAY",
    "SALTER PATH",
    "SEA GATE",
    "SEA LEVEL",
    "STACY",
    "STELLA",
    "STRAITS",
    "SMYRNA",
    "WILDWOOD",
    "WILLISTON",
    "WIREGRASS",
    
    // Craven County
    "CRAVEN CO",
    "CRAVEN",
    "HAVELOCK",
    
    // Jones County
    "JONES CO",
    "JONES",
    "MAYSVILLE",
    
    // Lenoir County
    "LENOIR CO",
    "LENOIR",
    "KINSTON",
    
    // Onslow County
    "ONSLOW CO",
    "ONLSOW CO",
    "ONSLOW COUNTY",
    "ONSLOW",
    "HUBERT",
    "SWANSBORO",
    "WHITE OAK"
  };

}
