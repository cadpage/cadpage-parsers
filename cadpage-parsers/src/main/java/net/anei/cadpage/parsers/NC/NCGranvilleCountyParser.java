package net.anei.cadpage.parsers.NC;


import java.util.Properties;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.anei.cadpage.parsers.CodeSet;
import net.anei.cadpage.parsers.MsgInfo.Data;
import net.anei.cadpage.parsers.dispatch.DispatchSouthernParser;



public class NCGranvilleCountyParser extends DispatchSouthernParser {
  
  public NCGranvilleCountyParser() {
    super(CITY_LIST, "GRANVILLE COUNTY", "NC", 
           DSFLAG_OPT_DISPATCH_ID | DSFLAG_CROSS_NAME_PHONE | DSFLAG_PLACE_FOLLOWS | DSFLAG_NO_ID);
    setupCallList(CALL_LIST);
    setupMultiWordStreets(MWORD_STREET_LIST);
    setupSpecialStreets("STEM CITY LIMITS");
  }

  @Override
  public String getFilter() {
    return "@granvillecounty.org";
  }
  
  private static final Pattern NAME_COUNTY_PTN = Pattern.compile("(.*?)[ /]*\\b([A-Z]+ COUNTY)", Pattern.CASE_INSENSITIVE);
  
  @Override
  protected boolean parseMsg(String body, Data data) {
    if (!super.parseMsg(body, data)) return false;
    if (NUMERIC.matcher(data.strCall).matches()) {
      data.strSupp = append(data.strCall, " ", data.strSupp);
      data.strCall = "";
    }
    
    data.strCity = data.strCity.replace('-', ' ');
    data.strCity = convertCodes(data.strCity, MISSPELLED_CITY_TABLE);

    data.strName = data.strName.replace('-', ' ');
    Matcher match = NAME_COUNTY_PTN.matcher(data.strName);
    if (match.matches()) {
      data.strName = match.group(1);
      if (data.strCity.length() == 0) data.strCity = match.group(2);
    }
    
    return true;
  }
  
  private static final String[] MWORD_STREET_LIST = new String[]{
      "ADAMS MOUNTAIN",
      "ALLEN CREEK",
      "ANTLER WAY",
      "B CLARK",
      "BATTLE CAVINESS",
      "BAXTER HUFF",
      "BEAVER DAM",
      "BEAVER POND",
      "BEN THORP",
      "BLUE BELL",
      "BLUE CREEK",
      "BOB DANIEL",
      "BODIE CURRIN",
      "BRASGG VALLEY",
      "BRUCE GARNER",
      "BULLOCK CHURCH",
      "CAMP KANATA",
      "CASTLE ROCK",
      "CEDAR CREEK",
      "DENNY FARM",
      "EAST BAY",
      "ELAM CURRIN",
      "EMERALD CREST",
      "ESTES CROSSING",
      "FERN HOLLOW",
      "FITCH OAKLEY",
      "FLAT ROCK",
      "GARNER TERRACE",
      "GENE HOBGOOD",
      "GLEN HAVEN",
      "GOLDEN FOREST",
      "GOOCHS MILL",
      "GORDON MOORE",
      "GRAHAM HOBGOOD",
      "GRAHAM SHERRON",
      "GROVE HILL",
      "HAWLEY SCHOOL",
      "HAYES FARM",
      "HOLLY CREEK",
      "HUGH DAVIS",
      "HUNTERS RIDGE",
      "JOE PEED",
      "JOE PRUITT",
      "JOHN SANDLING",
      "JOHNSON CREEK FARM",
      "JONAH DAVIS",
      "KNOTTY PINE",
      "LESTER MCFARLAND",
      "LITTLE MOUNTAIN",
      "LITTLE SATTERWHITE",
      "LONESOME DOVE",
      "LYON SERVICE STATION",
      "LYON STATION",
      "MARY LEE",
      "MAYS STORE",
      "MOLLIE MOONEY",
      "MORNING STAR",
      "MOSS LEDFORD",
      "OXFORD RIDGE",
      "PARROTT HOLLOW",
      "PHILO WHITE",
      "PINE RIDGE",
      "PINE TOWN",
      "PINE VALLEY",
      "PIXLEY PRITCHARD",
      "PLEASANTS RIDGE",
      "QUEEN ANNE",
      "RAVEN WOOD",
      "RED BUD",
      "RED PINE",
      "REEDY BRANCH",
      "REEDY CREEK",
      "ROBIN HOOD",
      "ROCK BOTTOM",
      "ROCK SPRING CHURCH",
      "ROCKY RIDGE",
      "ROGERS FARM",
      "ROGERS POINTE",
      "SAM MOSS HAYES",
      "SAM MOSS-HAYES",
      "SER J",
      "SHEP ROYSTER",
      "SIR WALTER",
      "SLEEPY HOLLOW",
      "SMITH CREEK",
      "ST LUCY",
      "STERLING CREEK",
      "STOOL TREE",
      "SUGAR MAPLE",
      "SUITTS STORE",
      "SUMMER SPRINGS",
      "SUMMIT RIDGE",
      "SUNRISE RIDGE",
      "TALLY HO",
      "TAR RIVER",
      "THOLLIE GREEN",
      "THOMAS STORE",
      "TOMMIE DANIEL",
      "TREE TOP",
      "VIRGINIA PINE",
      "WALNUT CREEK",
      "WAYSIDE FARM",
      "WES SANDLING",
      "WHEELER POND",
      "WHITE PINE",
      "WILD GOOSE",
      "WILLOW CREEK",
      "WINDING ACRES",
      "WINDING CREEK",
      "WINGATE CREEK",
      "WOODLAND CHURCH"
  };

  private static final CodeSet CALL_LIST = new CodeSet(
      "ACCIDENT-FATALITY",
      "ACCIDENT-PERSONAL INJURY",
      "ACCIDENT-PERSONAL-INJURY",
      "ACCIDENT-PROPERTY DAMAGE",
      "ACCIDENT-PROPERTY-DAMAGE",
      "ACCIDENT UNKNOWN",
      "ACCIDENT-UNKNOWN",
      "ALERT",
      "ALLERGIC REACTION",
      "ALLERGIC-REACTION",
      "ASSIST-OUTSIDE-AGENCY",
      "ASSIST-PERSONS",
      "BACK-PAINS",
      "BURN",
      "CHEST PAINS",
      "CHEST-PAINS",
      "CO2 MONITOR ACTIVATION",
      "CO2-MONITOR-ACTIVATION",
      "DECEASED PERSON",
      "DECEASED-PERSON",
      "DIABETIC CALL",
      "DIABETIC-CALL",
      "DOMESTIC-PROBLEM",
      "FALL (PERSON HAS FALLEN)",
      "FALL-(PERSON-HAS-FALLEN)",
      "FIRE ALARM",
      "FIRE-ALARM",
      "FIRE ALARM KEYPAD FIRE",
      "FIRE-ALARM-KEYPAD-FIRE",
      "FIRE ALARM PULL STATION ALARM",
      "FIRE-ALARM-PULL-STATION-ALARM",
      "FIRE (GRASS-WOODS)",
      "FIRE-(GRASS-WOODS)",
      "FIRE (OTHER/UNKNOWN)",
      "FIRE-(OTHER/UNKNOWN)",
      "FIRE (REKINDLE)",
      "FIRE-(REKINDLE)",
      "FIRE (STRUCTURE",
      "FIRE (STRUCTURE)",
      "FIRE-(STRUCTURE)",
      "FIRE (STRUCTURE) MUTUAL AID.",
      "FIRE-(STRUCTURE)-MUTUAL-AID.",
      "FIRE (VEHICLE) RV ON FIRE",
      "FIRE-(VEHICLE)-RV-ON-FIRE",
      "FIRE-ALARM",
      "FIRE-(GRASS-WOODS)",
      "FIRE-(OTHER/UNKNOWN)",
      "FIRE-(REKINDLE)",
      "FIRE-(STRUCTURE)",
      "FIRE-(STRUCTURE) STOVE FIRE",
      "FIRE-(VEHICLE)",
      "FUEL-SPILL",
      "GAS-SMELL/LEAK",
      "HEART ATTACK",
      "HEART-ATTACK",
      "HEART-PROBLEMS",
      "HEMORRHAGING-CALL",
      "INJURED-PERSON",
      "INVESTIGATION",
      "LIFTING-ASSISTANCE",
      "MISSING-PERSON",
      "OB CALL",
      "OB-CALL",
      "PAIN",
      "PAIN-CALL",
      "POWER-LINES-DOWN",
      "RESPIRATORY DISTRESS",
      "RESPIRATORY-DISTRESS",
      "SEIZURE",
      "SHORTNESS OF BREATH",
      "SHORTNESS-OF-BREATH",
      "SICK CALL",
      "SICK-CALL",
      "SMOKE-INVESTIGATION",
      "STROKE",
      "TEST-CALL",
      "SUICIDE-ATTEMPT",
      "UNCONSCIOUS PERSON",
      "UNCONSCIOUS-PERSON",
      "UNKNOWN EMS CALL",
      "UNKNOWN-EMS-CALL",
      "ACCIDENT-UNKNOWN",
      "UNRESPONSIVE PERSON",
      "UNRESPONSIVE-PERSON",
      
      // One time events
      "GARAGE",
      "SMOKE IN THE AREA",
      "WHITE TRUCK RED JEEP"
  );
  
  private static final Properties MISSPELLED_CITY_TABLE = buildCodeTable(new String[]{
      "CREEMDOOR",    "CREEDMOOR"
  });
  
  private static final String[] CITY_LIST = new String[]{
    
    // Cities and towns
    "BUTNER",
    "CREEDMOOR",
    "CREEMDOOR",  // Misspelled
    "OXFORD",
    "STEM",
    "STOVALL",
    
    // Unincorporated communities
    "BEREA",
    "BRASSFIELD",
    "BRAGGTOWN",
    "BULLOCK",
    "CULBRETH",
    "GRISSOM",
    "LEWIS",
    "SHAKE RAG",
    "SHOOFLY",
    "TALLY HO",
    "WILTON",
    
    // Durham County
    "DURHAM",
    "DURHAM CO",
    "DURHAM-CO",
    "DURHAM COUNTY",
    "CARR",
    "FALLS LAKE",
    "GORMAN",
    "MANGUM",
    "OAK GROVE",
    "ROUGEMONT",
    
    // Franklin County
    "FRANKLIN",
    "FRANKLIN CO",
    "FRANKLIN-CO",
    "FRANKLIN COUNTY",
    "FRANKLINTON",
    "YOUNGSVILLE",
    
    // Person County
    "PERSON",
    "PERSON CO",
    "PERSON-CO",
    "PERSON COUNTY",
    "ALLENSVILLE",
    "HOLLOWAY",
    "MOUNT TIRZAH",
    "MT TIRZAH",
    "ROXBORO",
    "TIMBERLAKE",
    
    // Vance County
    "VANCE",
    "VANCE CO",
    "VANCE-CO",
    "VANCE COUNTY",
    "TOWNSVILLE",
    "WILLIAMSBORO",
    "DABNEY",
    "WATKINS",
    "KITTRELL",
    
    // Wake County
    "WAKE",
    "WAKE CO",
    "WAKE-CO",
    "WAKE COUNTY",
    "FALLS LAKE",
    "NEW LIGHT",
    "RALEIGH",
    "ROLESVILLE",
    "WAKE FOREST"
    
  };
}