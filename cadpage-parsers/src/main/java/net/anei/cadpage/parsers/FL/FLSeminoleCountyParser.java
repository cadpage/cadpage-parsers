package net.anei.cadpage.parsers.FL;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.anei.cadpage.parsers.MsgInfo.Data;
import net.anei.cadpage.parsers.CodeSet;
import net.anei.cadpage.parsers.SmartAddressParser;

public class FLSeminoleCountyParser extends SmartAddressParser {

  public FLSeminoleCountyParser() {
    super("SEMINOLE COUNTY", "FL");
    setupCallList(CALL_LIST);
    setupMultiWordStreets(MW_STREET_LIST);
    setFieldList("CALL ADDR CITY X PLACE MAP APT CH UNIT INFO SRC");
  }

  private static Pattern LOC_CH_UNIT_INFO_SRC = Pattern.compile("(.*?) *TAC: *(.*?) *UNITS: *(.*?)(?: *- (.*?))?(?:\\. *(.*?))?");
  // Patterns to parse LOC(ation) field
  private static Pattern CALL_AMP_PLACE_X_APT_ADDR_MAP_CITY = Pattern.compile("(.*?) *& *(.*?)(?: *\\((?!MapBook:)(.*?/.*?)\\))?(?: *#(.*?))?(?: *\\(((?!MapBook:)[^/]*?)\\))?(?: *\\(MapBook:(.*?)\\))?(?:, *(.*?))?");
  private static Pattern CALLADDR_CITY_X_NEAR_MAP_APT = Pattern.compile("([^;]*?), *(.*?)(?: */(.*?)(?:, *(.*?))| *\\((?!MapBook)/?(.*?)/?\\))?(?: *Near:(.*?))?(?: *\\(MapBook:(.*?)\\))?(?: *#(.*?))?");
  private static Pattern CALLADDR_X_MAP_X = Pattern.compile("(.*?)(?: *\\((.*?)\\))?; *(.+)?");
  
  @Override
  protected boolean parseMsg(String subject, String body, Data data) {
    // General Alert if it doesn't fit the format
    Matcher mat = LOC_CH_UNIT_INFO_SRC.matcher(body);
    if (mat.matches()) {
      body = mat.group(1);
      data.strChannel = mat.group(2);
      data.strUnit = mat.group(3);
      data.strSupp = getOptGroup(mat.group(4));
      data.strSource = getOptGroup(mat.group(5));

      // Try specific format containing an &
      mat = CALL_AMP_PLACE_X_APT_ADDR_MAP_CITY.matcher(body);
      if (mat.matches()) {
        data.strCall = mat.group(1);
        data.strPlace = mat.group(2);
        data.strCross = getOptGroup(mat.group(3));
        data.strApt = getOptGroup(mat.group(4));
        parseAddress(getOptGroup(mat.group(5)), data);
        data.strMap = getOptGroup(mat.group(6));
        data.strCity = getOptGroup(mat.group(7));
        return true;
      }

      // Try second format
      mat = CALLADDR_CITY_X_NEAR_MAP_APT.matcher(body);
      if (mat.matches()) {
        parseAddress(StartType.START_CALL, FLAG_START_FLD_REQ | FLAG_ANCHOR_END, mat.group(1), data);
        data.strCity = mat.group(2);
        data.strCross = getOptGroup(mat.group(3)) + getOptGroup(mat.group(5));
        String crossCity = getOptGroup(mat.group(4));
        if (crossCity.length() > 0 && !crossCity.equals(data.strCity)) data.strCross += ", " + crossCity;
        data.strPlace = getOptGroup(mat.group(6));
        data.strMap = getOptGroup(mat.group(7));
        data.strApt = getOptGroup(mat.group(8));
        return true;
      }

      // Check if this is the rare Addr; format
      mat = CALLADDR_X_MAP_X.matcher(body);
      if (mat.matches()) {
        parseAddress(StartType.START_CALL, FLAG_START_FLD_REQ, mat.group(1), data);
        data.strCross = getOptGroup(mat.group(2));
        String g3 = mat.group(3);
        if (g3 != null) {
          Result mr = parseAddress(StartType.START_ADDR, FLAG_ONLY_CROSS, getOptGroup(g3));
          if (mr.isValid()) {
            mr.getData(data);
            data.strMap = getLeft();
          } else data.strMap = g3;
        }
        return true;
      }
    }
    
    return false;
  }
  
  @Override
  public String adjustMapAddress(String sAddress) {
    return SHADY_HOLW_PTN.matcher(sAddress).replaceAll("SHADY HOLLOW LN");
  }
  private static Pattern SHADY_HOLW_PTN = Pattern.compile("\\bSHADY +HOLW\\b", Pattern.CASE_INSENSITIVE);
  
  private static final CodeSet CALL_LIST = new CodeSet(
      "ALERT 1A",
      "ANIMAL BITES",
      "BREATHING PROBLEMS",
      "BRUSH FIRE",
      "CARDIAC ARREST",
      "COMMAND ESTABLISHED NATURAL / PROPANE GAS LEAK OUTSIDE",
      "COMMAND ESTABLISHED REF NATURAL / PROPANE GAS LEAK INSIDE",
      "COMMAND ESTABLISHED REF STRUCTURE FIRE RESIDENTIAL",
      "COMMAND ESTABLISHED REF TRAFFIC / TRANSPORTATION ACCIDENTS",
      "COMMAND ESTABLISHED REF TRAFFIC ACCIDENT (ENTRAPMENT)",
      "COMMAND ESTABLISHED SMOKE IN STRUCTURE COMMERICAL",
      "DIABETIC PROBLEMS",
      "EMDA",
      "EMDD",
      "GENERAL ALERT",
      "HAZARDOUS CONDITION",
      "HEADACHE",
      "ILLEGAL BURN",
      "MECHANICAL ALARM COMMERCIAL",
      "MECHANICAL ALARM RESIDENTIAL",
      "NATURAL / PROPANE GAS LEAK INSIDE",
      "NATURAL / PROPANE GAS LEAK OUTSIDE",
      "OVEN FIRE (CONTAINED)",
      "OVERDOSE / POISONING",
      "PREGNANCY / CHILDBIRTH",
      "PUBLIC ASSIST",
      "SICK PERSON",
      "SMOKE IN STRUCTURE COMMERICAL",
      "SMOKE IN STRUCTURE RESIDENTIAL",
      "SMOKE ODOR IN STRUCTURE",
      "STAB / GUNSHOT / PENETRATING TRAUMA",
      "STAB / GUNSHOT",
      "STRUCTURE FIRE COMMERCIAL",
      "STRUCTURE FIRE RESIDENTIAL",
      "TRAFFIC / TRANSPORTATION ACCIDENTS",
      "TRAFFIC ACCIDENT (ENTRAPMENT)",
      "TRAFFIC ACCIDENT (VEH VS STRUCTURE)",
      "TRAFFIC ACCIDENT",
      "TRAUMATIC INJURIES",
      "UNCONSCIOUS / FAINTING",
      "UNKNOWN PROBLEM",
      "UPDATE:  COMMAND TERMINATED - E16 ADVISED LOG IN FIREPLACE REF SMOKE IN STRUCTURE COMMERICAL",
      "WORKING STRUCTURE FIRE RESIDENTIAL"
   );
  
  private static final String[] MW_STREET_LIST = new String[] { 
    "BRANTLEY HALL",
    "COTTONWOOD CREEK",
    "CONTROL TOWER",
    "GOLDEN DAYS",
    "GRASSY POINT",
    "HIDDEN MEADOWS",
    "HITCHING POST",
    "HERITAGE PARK",
    "HOWELL BRANCH",
    "LAKE EMMA",
    "LAKE HOWELL",
    "LAKE MARY",
    "LONGWOOD LAKE MARY",
    "POINTE NEWPORT",
    "QUEENS MIRROR",
    "RED BUG LAKE",
    "REGAL POINTE",
    "ROSE MALLOW",
    "SABAL LAKE",
    "SHADY HOLW", //the weird spelling is consistent even between departments
    "SPANISH TRACE",
    "ST JOHNS",
    "WILLA SPRINGS",
    "WINDSOR CRESCENT",
    "WINDING LAKE",
    "WINTER GREEN",
    "WINTER PARK",
    "VILLAGE OAK"
  };
}
