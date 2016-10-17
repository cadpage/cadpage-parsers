package net.anei.cadpage.parsers.KY;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.anei.cadpage.parsers.CodeSet;
import net.anei.cadpage.parsers.MsgInfo.Data;
import net.anei.cadpage.parsers.dispatch.DispatchB3Parser;

public class KYLaRueCountyParser extends DispatchB3Parser {
  
  private static final Pattern CALL_ID_PTN = Pattern.compile("(\\d+):(.*)");
  private static final Pattern MISSING_GT_PTN = Pattern.compile("(FIRE(?:-[A-Z])?) (FIRE)");
  private static final Pattern AUTOMATED_MSG_PTN = Pattern.compile("/?(.*\\b(?:FIRE ALARM|FIRE) IN (?:YOUR AREA\\.? )?(?:THIS IS AN )?AUTOMATED (?:MSG|MESSAGE)\\.?)");
  
  public KYLaRueCountyParser() {
    super("LARUECO911:",CITY_LIST, "LARUE COUNTY", "KY", B2_CROSS_FOLLOWS);
    setupCallList(CALL_LIST);
    setupProtectedNames("L AND N", "LG AND E");
    setupSpecialStreets("FERRAL HL");
    setupMultiWordStreets(
        "AC UNDERWOOD",
        "AETNA FURNACE",
        "ALVIN BROOKS",
        "B F BROWN",
        "BALL HOLLOW",
        "BARREN RUN",
        "BF BROWN",
        "BOUNDARY OAKS",
        "BUDS LAKE",
        "CARTER BRO",
        "CHARLIE RAGLAND",
        "CIRCLE CREST",
        "CISSAL HILL",
        "COLLEGE (B)",
        "DAN DUNN",
        "DIE SHIBOLI CHURCH",
        "EARL JONES",
        "FERRILL HILL",
        "GAULT TERRY",
        "HAMMONDSVILLE SCHOOL HOUSE",
        "HAMMONDSVILLE SCHOOLHOUSE",
        "HERBERT HOWELL",
        "JESSE ABELL",
        "KNOB SCHOOL HOUSE",
        "KNOX CK",
        "LCPL HANSON USMC",
        "LEE OAK",
        "LINCOLN FARM",
        "LOCUST GROVE",
        "LOGAN SKAGGS",
        "LYONS STATION",
        "MAGNOLIA GAS STORAGE",
        "MARTIN MEADOW",
        "MARTIN MEADOW",
        "MCDOWELL SPUR",
        "MT SHERMAN WARD",
        "MT SHERMAN",
        "MT TABOR",
        "OAK HILL",
        "OTTER CREEK",
        "PARKER GROVE SPUR",
        "PARKER GROVE",
        "PLEASANT CHURCH",
        "POWER MILL",
        "SALEM CHURCH",
        "SALEM LAKE",
        "SAND RIDGE",
        "SPENCER SCHOOL",
        "SPRING LOOP",
        "SPRING PARK",
        "STILES FORD",
        "TALLEY OAK HILL",
        "THOMPSON HILL",
        "UNION CHUCH",
        "UNION CHURCH",
        "UPTON TALLEY",
        "WARDS RIDGE",
        "WAYNE ENNIS",
        "WHITE CITY",
        "YOUNGERS CREEK"
    );
    removeWords("TURNPIKE");
  }
  
  @Override
  public String getFilter() {
    return "LARUECO911@otelco.net,315@otelco.net";
  }
  
  boolean hasSubject;
  
  @Override
  protected boolean parseMsg(String subject, String body, Data data) {
    Matcher match = CALL_ID_PTN.matcher(body);
    if (match.matches()) {
      data.strCallId = match.group(1);
      body = match.group(2);
    }
    body = MISSING_GT_PTN.matcher(body).replaceFirst("$1>$2");
    hasSubject = subject.length() > 0;
    if (!super.parseMsg(subject, body, data)) return false;
    
    // Cross street may have ended in with name, in which case it was
    // misinterpreted as the actual city name and a duplicate city name
    // ended up in the name field
    if (data.strCross.length() > 0 && 
          (data.strCross.endsWith(" IN") || data.strCross.endsWith(" OF")) &&
        data.strCity.length() > 0 && 
        data.strName.startsWith(data.strCity)) {
      data.strCross = data.strCross + ' ' + data.strCity;
      data.strName = data.strName.substring(data.strCity.length()).trim();
    }

    // And sometimes what pops up in the name field is really a cross street
    // and automated alert message
    match = AUTOMATED_MSG_PTN.matcher(data.strName);
    if (match.matches()) {
      data.strSupp = append(match.group(1).trim(), " / ", data.strSupp);
      data.strName = "";
    }
    
    return true;
  }
  
  @Override
  public String getProgram() {
    return "ID " + super.getProgram();
  }
  
  private static final Pattern L_AND_N_PTN = Pattern.compile("\\bL *& *N\\b");
  private static final Pattern LG_AND_E_PTN = Pattern.compile("\\bLG *& *E\\b");

  @Override
  protected boolean parseAddrField(String field, Data data) {
    field = L_AND_N_PTN.matcher(field).replaceAll("L AND N");
    field = LG_AND_E_PTN.matcher(field).replaceAll("LG AND E");
    if (!hasSubject) field = field.replace('@', '&');
    return super.parseAddrField(field, data);
  }

  private static final String[] CITY_LIST = new String[]{
      "ATHERTONVILLE",
      "BUFFALO",
      "HODGENVILLE",
      "LYONS",
      "MAGNOLIA",
      "MT SHERMAN",
      "TONIEVILLE",
      "UPTON",
      
      // Green County
      "GREEN CO",
      
      // Hardin County
      "HARDIN CO",
      "SONORA",
      
      // Hart County
      "HART CO",
      
      // Marion County
      "MARION CO",
      
      // Nelson County
      "NELSON CO",
      "NEW HAVEN",
      
      // Taylor County
      "TAYLOR CO",
      "CAMPBELLSVILLE"
      
    };

  private static final CodeSet CALL_LIST = new CodeSet(
      "2ND CALL ON ANY EVENT",
      "AGENCY ASSIST-OUT OF COUNTY",
      "ANIMAL COMPLT",
      "AUTOMATED BURGLAR ALARM",
      "ATTEMPT TO LOCATE",
      "BREATHING DIFFICULTIES",
      "CARBON MONOXIDE",
      "CARDIAC/CHEST PAINS",
      "DEATH INVESTIGATION",
      "DOMESTIC",
      "FALL",
      "FIRE - ALARM",
      "FIRE - FIELD / BRUSH",
      "FIRE - UNDEFINED",
      "FIRE - STRUCTURE",
      "FIRE - VEHICLE",
      "FIRE REKINDLE",
      "GAS LEAK ANY",
      "GENERAL ALERT",
      "ILLNESS",
      "INCOMPLETE 911 CALL",
      "INJURY ANY TYPE",
      "LOGIN ONLY",
      "MISSING JUVENILE",
      "MISSING PERSON",
      "MOTORIST ASSIST",
      "MVA NON INJURY",
      "MVA WITH INJURIES",
      "OFFICER REQUEST BACKUP",
      "OVERDOSE",
      "PATIENT CHOKING",
      "RESCUE CALL",
      "ROAD OBSTRUCTION",
      "ROUTINE TRANSFER",
      "RUNAWAY",
      "SEIZURE",
      "SPECIAL DETAIL",
      "STROKE",
      "SUICIDE OR ATTEMPTED SUICIDE",
      "UNKNOWN",
      "UNRESPONSIVE PERSON",
      "WEATHER COMPLT ANY",
      "WELFARE CHECK"
  );
}
