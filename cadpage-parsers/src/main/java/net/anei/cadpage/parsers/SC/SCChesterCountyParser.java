package net.anei.cadpage.parsers.SC;

import java.util.regex.Pattern;

import net.anei.cadpage.parsers.CodeSet;
import net.anei.cadpage.parsers.MsgInfo.Data;
import net.anei.cadpage.parsers.dispatch.DispatchB2Parser;



public class SCChesterCountyParser extends DispatchB2Parser {
 
  public SCChesterCountyParser() {
    super("CHESTER_911:", CITY_CODES, "CHESTER COUNTY", "SC");
    setupCallList(CALL_LIST);
    setupMultiWordStreets(MWORD_STREET_LIST);
  }
  
  @Override
  public String getFilter() {
    return "CHESTER_911@Truvista.net";
  }
  
  private static final Pattern MM_PTN = Pattern.compile("MM(?: .*)?");
  
  @Override
  protected boolean parseAddrField(String field, Data data) {
    field = field.replace('@', '&');
    if (!super.parseAddrField(field, data)) return false;
    if (data.strApt.length() > 0 && MM_PTN.matcher(data.strName).matches()) {
      data.strAddress = data.strAddress + ' ' + data.strApt + " MM";
      data.strApt = "";
      data.strName = data.strName.substring(2).trim();
    }
    return true;
  }

  private static final Pattern CALL_ADDR_PTN = Pattern.compile("(.*);(.*)");
  
  @Override
  protected Pattern getCallPattern() {
    return CALL_ADDR_PTN;
  }
  
  private static final Pattern MTN_GAP_PTN = Pattern.compile("\\bMTN? GAP\\b", Pattern.CASE_INSENSITIVE);
  @Override
  public String adjustMapAddress(String addr) {
    addr = MTN_GAP_PTN.matcher(addr).replaceAll("MOUNTAIN GAP");
    return addr;
  }
  
  @Override
  public String adjustMapCity(String city) {
    if (city.equalsIgnoreCase("CATAWBA")) city = "CHESTER COUNTY";
    return city;
  }
  
  private static final String[] MWORD_STREET_LIST = new String[]{
      "ARROW WOOD",
      "B C MOORE",
      "BATON ROUGE",
      "BEAVER DAM",
      "BLADE RUN",
      "BRIAN CHRISTOPHER",
      "BUSTERS LANDING",
      "CABAL RD OFF LOCKHART",
      "CATAWBA RIVER",
      "CENTER RD",
      "CHESTER SCH",
      "DEER BRANCH",
      "EAST CREEK",
      "EAST LACEY",
      "F MELTON",
      "FIRST CLASS",
      "FISHING CREEK CH",
      "FISHING CREEK CHURCH",
      "GASTON CALDWELL",
      "GEORGE ARGUS",
      "GEORGES BRIDGE",
      "GILL JORDAN",
      "GOLF COURSE",
      "GREAT FALLS",
      "GROVE PARK",
      "HARMONY CHURCH",
      "HILL TOP",
      "HOPE CHURCH",
      "J A COCHRAN",
      "J S GASTON",
      "KEE MORE",
      "KING AIR",
      "KNOX STATION",
      "LAKE VIEW",
      "LAZY H",
      "LEWISVILLE HIGH SCH",
      "LIZZIE MELTON",
      "M STEVENSON",
      "MEL NUNNERY",
      "MINERAL SPRINGS",
      "MT GAP",
      "MT PLEASANT CH",
      "MT VERNON",
      "MTN GAP",
      "NITRO LEE",
      "NORTH MAIN",
      "OAKLEY HALL SCH",
      "PEDEN BRIDGE",
      "POOR BOY",
      "PRESTON BROOKE",
      "ROSS DYE",
      "SAINT MICHAEL",
      "SAYE PLACE",
      "SCHOOL HOUSE",
      "SIDNEY CORNWELL",
      "SILVER BROOKE",
      "SLICK ROCK",
      "SOUTH MAIN",
      "SPENCER BOYD",
      "SUZY BOLE",
      "SWEAT MCCULLOUGH",
      "SWEATT MCCULLOUGH",
      "THRAILKILL SOUTH",
      "TICKLE HILL",
      "WESTON ACRES",
      "WILLIAM MARTIN",
      "WYLIES MILL"
      
  };

  private static final CodeSet CALL_LIST = new CodeSet(
      "911 HANG UP",
      "911 OPEN LINE",
      "ABDOMINAL PAIN/PROBLEMS",
      "ALARM FIRE",
      "ALLERGIES/ENVENOMATIONS",
      "BACK PAIN",
      "BREATHINGPROBLEMS",
      "BRUSH/GRASS/TREE/WOODS FIRE",
      "CHECK AREA",
      "CHEST PAIN",
      "CHEST PAIN/DISCOMFORT",
      "CHOKING",
      "CONVULSIONS/SEIZURES",
      "DIABETIC PROBLEMS",
      "DOMESTIC DISTURBANCE",
      "EYE PROBLEM/INJURIES",
      "FALLS",
      "FUEL SPILL",
      "HAVE INFORMATION",
      "HEADACHE",
      "HEART PROBLEMS/AICD",
      "HEAT/COLD EXPOSURE",
      "HEMORRHAGE/LACERATIONS",
      "HIT AND RUN",
      "INTOXICATED PEDESTRIAN",
      "MEDICAL ALARM NO INFORMATION:",
      "MEET AT / STAND-BY",
      "MISSING PERSON",
      "MOTOR VEHICLE ACCIDENT",
      "OTHER FIRE RESPONSE",
      "OVERDOSE/POISONING",
      "PREGNANCY/CHILDBIRTH/MISCARRI",
      "PSYCHIATRIC/ABNORMAL BEHAVIOR",
      "ROAD BLOCKED",
      "SICK PERSON",
      "SMOKE",
      "SMOKE/GAS SMELL OR LEAK",
      "STROKE/CVA",
      "STROKE/CVA/TIA",
      "STRUCTURE FIRE",
      "SUICIDE",
      "SUSPICIOUS VEHICLE STOP",
      "TRANFERED TO",
      "TRANFERED TO HP",
      "TRANFER TO ANOTHER PSAP",
      "TRANSFER/INTERFACILITY/PALLIA",
      "TRAUMATIC INJURIES",
      "UNCONSCIOUS/FAINTING",
      "UNKNOWN PROBLEM",
      "VEHICLE/CAR/TRUCK FIRE",
      "WELFARE CHECK"
  );
  
  private static final String[] CITY_CODES = new String[]{
    
    // Cities
    "CHESTER",

    // Towns
    "FORT LAWN",
    "GREAT FALLS",
    "LOWRYS",
    "RICHBURG",

    // Other populated places
    "BLACKSTOCK",
    "EDGEMOOR",
    "EUREKA MILL",
    "GAYLE MILL",
    "LANDO",
    "LEEDS",
    "WILKSBURG",
    
    // York County
    "CATAWBA",
    "SHARON"
  };
}
