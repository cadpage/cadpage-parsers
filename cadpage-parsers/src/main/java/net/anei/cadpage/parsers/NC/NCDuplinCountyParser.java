package net.anei.cadpage.parsers.NC;

import java.util.Properties;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.anei.cadpage.parsers.CodeSet;
import net.anei.cadpage.parsers.MsgInfo.Data;
import net.anei.cadpage.parsers.MsgInfo.MsgType;
import net.anei.cadpage.parsers.dispatch.DispatchSouthernParser;


public class NCDuplinCountyParser extends DispatchSouthernParser {
  
  
  public NCDuplinCountyParser() {
    super(CITY_LIST, "DUPLIN COUNTY", "NC",
          DSFLG_OPT_DISP_ID|DSFLG_ADDR|DSFLG_OPT_X|DSFLG_TIME);
    setupCallList(CALL_LIST);
    removeWords("DRIVE");
  }
  
  @Override
  public String getFilter() {
    return "@duplincountync.com";
  }
  
  @Override
  public int getMapFlags() {
    return MAP_FLG_REMOVE_EXT;
  }
  
  private static final Pattern RUN_REPORT_PTN = Pattern.compile("[A-Z.]*:CFS: *(\\d+) +Unit: *(\\S+) +(.*)");
  private static final Pattern FIX_ADDR_DIR_PTN = Pattern.compile("\\b([NSEW])- *");
  private static final Pattern FIX_ADDR_PTN = Pattern.compile("[ &]*(?: - *|- +)(.*?[^ ])(?= *[&/]|$)");
  
  @Override
  protected boolean parseMsg(String body, Data data) {
    
    Matcher match = RUN_REPORT_PTN.matcher(body);
    if (match.matches()) {
      data.msgType = MsgType.RUN_REPORT;
      setFieldList("ID UNIT CALL");
      data.strCallId = match.group(1);
      data.strUnit = match.group(2);
      data.strCall = match.group(3);
      return true;
    }
    
    if (!super.parseMsg(body, data)) return false;
    data.strCity = convertCodes(data.strCity.toUpperCase(), FIX_CITY_TABLE);
    data.strAddress = FIX_ADDR_DIR_PTN.matcher(data.strAddress).replaceAll("$1 ");
    data.strCross = FIX_ADDR_DIR_PTN.matcher(data.strCross).replaceAll("$1 ");
    data.strAddress = FIX_ADDR_PTN.matcher(data.strAddress).replaceAll(" ($1)");
    data.strCross = FIX_ADDR_PTN.matcher(data.strCross).replaceAll(" ($1)");
    return true;
  }
  
  private static final CodeSet CALL_LIST = new CodeSet(
      "10-50",
      "911-HANG-UP",
      "ABDOMINAL-PAINS",
      "ACCIDENT-FATALITY",
      "ACCIDENT-INJURY",
      "ACCIDENT-PROPERTY",
      "ACCIDENT-PROPERTY-DAMAGE-ONLY",
      "ALERT",
      "ALLERGIC-REACTION",
      "ANIMAL-BITE",
      "ASTHMA-ATTACK",
      "BI-PRODUCT-SPILL",
      "BRUSH/-WOODS/-WILD-LAND--FIRE",
      "BURN-VICTIM",
      "CARBON-MONOXIDE-LEAK/ALARM",
      "CHEST-PAINS-/-HEART-ATTACK",
      "COMBATIVE-PATIENT",
      "CONTROL-BURN",
      "C&R-CARELESS-AND-RECKLESS-DRIVER",
      "DAMAGE-TO-PROPERTY",
      "DEBRIS-IN-ROADWAY",
      "DIABETIC-EMERGENCY",
      "DISTURBANCE",
      "D-O-A",
      "DROWNING---NEAR-DROWNING",
      "ELECTRICAL FIRE",
      "ELECTRICAL-FIRE-OTHER",
      "ELECTRICAL-FIRE-STRUCTURE",
      "EQUIPMENT FIRE",
      "EQUIPMENT-FIRE",
      "EXPLOSION",
      "FALL",
      "FALL WITH INJURIES",
      "FALL-WITH-INJURIES",
      "FIELD FIRE / GRASS FIRE",
      "FIELD-FIRE-/-GRASS-FIRE",
      "FIRE ALARM COMMERCIAL",
      "FIRE ALARM RESIDENTIAL",
      "FIRE-ALARM-COMMERCIAL",
      "FIRE-ALARM-RESIDENTIAL",
      "FIRE-ALARM-SCHOOL",
      "FURTHER-INVESTIGATION",
      "HAZARDOUS-MATERIALS-(-FUEL-CHEMICAL-LEAK-SPILL)",
      "HEART-PATIENT-CARDIAC",
      "HEMORRHAGE-BLEEDING-PROFUSELY",
      "HIGH-BLOOD-PRESSURE",
      "ILLEGAL-BURNING",
      "ILLNESS",
      "INFORMATION-NEEDED-OR-GIVEN",
      "INJURY---HIP/-HEAD/-ETC",
      "MEDICAL-ALARM",
      "MISSING-PERSON",
      "OD---OVERDOSE",
      "OTHER FIRE",
      "OTHER-FIRE",
      "OUT OF SERVICE",
      "POA---ZONE",
      "SEARCH-AND-RESCUE-TEAM",
      "SEIZURE",
      "SERVICE-CALL-FIRE-DEPARTMENT",
      "SHORTNESS-OF-BREATH/DIFFICULTY-BREATHING",
      "STORM DAMAGE",
      "STORM-DAMAGE",
      "STRANDED MOTORIST 10-82",
      "STRANDED-MOTORIST-10-82",
      "STROKE-PATIENT",
      "STRUCTURE FIRE CHURCH",
      "STRUCTURE FIRE COMMERCIAL",
      "STRUCTURE FIRE RESIDENTIAL",
      "STRUCTURE-FIRE-CHURCH",
      "STRUCTURE-FIRE-COMMERCIAL",
      "STRUCTURE-FIRE-RESIDENTIAL",
      "SUICIDE-THREAT",
      "SUSPICIOUS-SUBJECT",
      "TRAINING-EXERCISE/DRILL",
      "TRANSFER",
      "TRANSFER-OF-911-CALL-TO-OTHER-PSAP",
      "TRAPPED/PINNED",
      "UNCONSCIOUS PASSED OUT",
      "UNCONSCIOUS-PASSED-OUT",
      "UNRESPONSIVE BREATHING",
      "UNRESPONSIVE NOT BREATHING",
      "UNRESPONSIVE-BREATHING",
      "UNRESPONSIVE-NOT-BREATHING",
      "VEHICLE FIRE",
      "VEHICLE-FIRE",
      "WEAKNESS",
      "WELFARE-CHECK-PERSON"
  );

  private static final String[] CITY_LIST = new String[]{
    
    "DUPLIN",
    
    // Cities
    "BEULAVILLE",
    "CALYPSO",
    "CHINQUAPIN",
    "FAISON",
    "FOUNTAINTOWN",
    "GREENEVERS",
    "HARRELLS",
    "KENANSVILLE",
    "MAGNOLIA",
    "MT OLIVE",
    "MOUNT OLIVE",
    "ROSE HILL",
    "SARECTA",
    "TEACHEY",
    "WALLACE",
    "WARSAW",
    
    // Townships
    "ALBERTSON",
    "CYPRESS CREEK",
    "FAISON",
    "GLISSON",
    "ISLAND CREEK",
    "KENANSVILLE",
    "KENASVILLE",    // misspelled
    "KENANSVL",      // misspelled
    "LIMESTONE",
    "MAGNOLIA",
    "ROCKFISH",
    "ROSE HILL",
    "SMITH",
    "WALLACE",
    "WARSAW",
    "WOLFSCRAPE",
    
    // Onslow County
    "ONSLOW",
    "RICHLANDS",
    
    // Lenoir County
    "DEEP RUN",
    "PINK HILL",
    
    // Pender County
    "PENDER",
    "WATHA",
    "WILLARD",
    
    // Sampson County
    "SAMPSON",
    "TURKEY",
    
    // Wayne County
    "SEVEN SPRINGS"
  };
  
  private static final Properties FIX_CITY_TABLE = buildCodeTable(new String[]{
      "KENASVILLE",     "KENANSVILLE",
      "KENANSVL",       "KENANSVILLE"
  });

}
