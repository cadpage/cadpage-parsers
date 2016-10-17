package net.anei.cadpage.parsers.KY;

import net.anei.cadpage.parsers.CodeSet;
import net.anei.cadpage.parsers.MsgInfo.Data;
import net.anei.cadpage.parsers.SmartAddressParser;

public class KYScottCountyBParser extends SmartAddressParser {
  
  public KYScottCountyBParser() {
    super(CITY_LIST, "SCOTT COUNTY", "KY");
    setFieldList("CALL ADDR APT CITY INFO");
    setupCallList(CALL_LIST);
    setupMultiWordStreets(
        "CHERRY BLOSSOM",
        "CORINTH HINTON",
        "DIVIDING RIDGE",
        "FOX RUN",
        "HARBOR VILLAGE",
        "HESS BRANCH",
        "LEMONS MILL",
        "LOCUST FORK",
        "LONG LICK",
        "MAIN EXT",
        "MINORS BRANCH",
        "NORTHERN HEIGHTS",
        "SPINDLETOP VILLAGE",
        "STAMPING GROUND",
        "WHITE OAK",
        "WILDERNESS COVE",
        "WOODLAKE STAMPING GROUND"
    );
    removeWords("X");
  }
  
  @Override
  public String getFilter() {
    return "nwsmail@gsc911.org";
  }
  
  @Override
  public boolean parseMsg(String subject, String body, Data data) {
    if (!subject.equals("nws email")) return false;
    parseAddress(StartType.START_CALL, FLAG_START_FLD_REQ | FLAG_IGNORE_AT | FLAG_EMPTY_ADDR_OK | FLAG_RECHECK_APT, body, data);
    if (data.strCity.length() == 0) return false;
    data.strSupp = getLeft();
    return true;
  }
  
  private static final CodeSet CALL_LIST = new CodeSet(
      "ACCIDENT - INJURY",
      "ACCIDENT - NON INJURY",
      "ACCIDENT - PEDESTRIAN",
      "ALARM - SMOKE DETECTOR",
      "BREATHING PROBLEMS",
      "CHEST PAIN",
      "CHILD LOCK IN",
      "CODE 500",
      "DIABETIC SHOCK",
      "EXPLOSION",
      "FALL",
      "FIRE ALARM",
      "FIRE - GRASS",
      "FIRE - STRUCTURE",
      "FIRE - UNKNOWN",
      "FIRE - VEHICLE",
      "GAS LEAK - INSIDE",
      "INJURED PERSON",
      "INSPECTION - FIRE",
      "MEDICAL",
      "MUTUAL AID",
      "STROKE",
      "UNCONSCIOUS",
      "UNKNOWN PROBLEM",
      "UNRESPONSIVE"
  );
  
  private static final String[] CITY_LIST = new String[]{
    "CORINTH",
    "GEORGETOWN",
    "SADIEVILLE",
    "STAMPING GROUND",
    
    // Fayette County
    "LEXINGTON"
  };
}
