package net.anei.cadpage.parsers.NC;

import java.util.Properties;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.anei.cadpage.parsers.CodeSet;
import net.anei.cadpage.parsers.CodeTable;
import net.anei.cadpage.parsers.MsgInfo.Data;
import net.anei.cadpage.parsers.dispatch.DispatchA3AParser;


public class NCNashCountyParser extends DispatchA3AParser {
  
  private static final Pattern CHANNEL_PTN = Pattern.compile("TAC.*", Pattern.CASE_INSENSITIVE);
  private static final Pattern HAZARD_PTN = Pattern.compile("Hazards: .*");
  private static final Pattern UNIT_PTN = Pattern.compile("(?!FIRES)(?:\\b(?:\\d*[A-Z]*\\d+[A-Z]?|\\d+-\\d+|[A-Z]*EMS|[A-Z]*FIR?E|[A-Z]*FD|[A-Z]*RES|[A-Z]*CEM|[A-Z]CSO|DOT|TOISN|SI FR|WAKEM|ST\\d+(?:-\\d+)?|(?:BRUSH|EMS|PPO|Nash Car|UNIT|RESCUE|TRUCK|ENG|SQD) ?\\d+)\\b[ ,]?)+");
  private static final Pattern PHONE_PTN = Pattern.compile("\\d{2}[ 0-9]-\\d{3}-\\d{4}");
  private static final Pattern COMMENT_LABEL_PTN = Pattern.compile("(?:Geo|Landmark|Place) Comment: *(.*)|NBH: *(.*)");
  
  public NCNashCountyParser() {
    super(CITY_LIST, "NASH COUNTY", "NC");
    setFieldList("ADDR APT CH CITY X PLACE CODE CALL NAME UNIT PHONE INFO " + getFieldList());
    setupGpsLookupTable(GPS_LOOKUP_TABLE);
    setupMultiWordStreets(
        "ADOLPHUS T BOONE",
        "ANNIE LEIGH",
        "AVENTON GIN",
        "BAINES LOOP",
        "BALDY HILL",
        "BARNES HILL CHURCH",
        "BARNHILL FARM",
        "BATTLE PARK",
        "BATTLEBORO LEEGETT",
        "BATTLEBORO LEGGETT",
        "BATTLEBORO LEGGIT",
        "BEAVER DAM",
        "BELLAMY MILL",
        "BEND OF THE RIVER",
        "BIG WOODS",
        "BLACK CLOUD",
        "BLUE HERON",
        "BODDIE MILL POND",
        "BONES ACRES",
        "BONES CUT OFF",
        "BRADLEY CUTCHIN",
        "BRANTLEY PARK",
        "BRIDGE TENDER",
        "BUCK DEANS",
        "BULL HEAD",
        "BULLOCK SCHOOL",
        "BULLUCK SCHOOL",
        "BURNT MILL",
        "CABIN PATH",
        "CAMP CHARLES",
        "CARRIAGE FARM",
        "CARRIAGE HOUSE",
        "CARTER GROVE",
        "CEDAR GROVE SCHOOL LOOP",
        "CEDAR LAKE",
        "CHILDRENS HOME",
        "COKER TOWN",
        "COLLIE TYSON",
        "COOL SPRING",
        "COOPERS SCHOOL",
        "COUNTRY CLUB",
        "COUNTY HOME",
        "COUNTY LINE",
        "CRESCENT MEADOWS",
        "CURTIS ELLIS",
        "DAVIS STORE",
        "DAVIS WORRELL",
        "DOUBLE WIDE",
        "EAGLE RIDGE",
        "EL SHADDAI",
        "FIRE TOWER FARM",
        "FIRE TOWER",
        "FISHING CREEK",
        "FLAG POND LOOP",
        "FLAT ROCK",
        "FLOOD STORE",
        "FLOODS STORE",
        "FLOWER HILL",
        "FOREST COVE",
        "FOREST HILL",
        "FOUNTAIN BRANCH",
        "FOX RUN",
        "GASKILL FARM",
        "GEORGE PACE",
        "GOLD ROCK",
        "GREEN HILLS",
        "GREEN PASTURE",
        "GREEN POND LOOP",
        "GREYS MILL",
        "GRIFFIN FARM",
        "HARRIS COLLIE",
        "HORNES CHURCH",
        "HUNTER HILL",
        "HUNTER RIDGE",
        "JOHN J SHARPE",
        "L A GREEN FARM",
        "LAKE ROYALE",
        "LAKE VIEW",
        "LANCASTER STORE",
        "LOCHMERE BAY",
        "LOGANS RUN",
        "LONESOME PINE",
        "LOYD PARK",
        "MAMAS RUN",
        "MARTIN LUTHER KING",
        "MEADOW PARK",
        "MICHAEL SCOTT",
        "MIDDLESEX CORPORATE",
        "MIGHTY CIRCLE",
        "MILL BRANCH",
        "MILLIE FIELD",
        "MISSING MILE",
        "MOORE FARM",
        "MORNING STAR CH",
        "MORNING STAR CHURCH",
        "MOUNT PLEASANT",
        "MT PLEASANT",
        "MT ZION CHURCH",
        "MURRAY LOOP",
        "NASHVILLE COMMONS",
        "NICODEMUS MILE",
        "NORTH OF WHITFIELD",
        "NORTHERN NASH",
        "OAK GROVE",
        "OAK LEVEL",
        "PEACHTREE HILLS",
        "PLANER MILL",
        "PLEASANT GROVE CHURCH",
        "PLEASANT GROVE",
        "POLE CAT",
        "PREACHER JOYNER",
        "QUIET WATERS",
        "RACE TRACK",
        "RALEIGH WILSON",
        "RED BUD",
        "RED OAK BATTLEBORO",
        "RED OAK",
        "REGES STORE",
        "RIVER BEND",
        "RIVER GLENN",
        "RIVER LAKE",
        "ROCK QUARRY",
        "ROCK RIDGE",
        "ROCKY CROSS",
        "ROLLING ROCK",
        "ROSE LOOP",
        "SALEM SCHOOL",
        "SAMARIA CHURCH",
        "SANCTIFIED CHURCH",
        "SANDY CROSS",
        "SANDY HILL CHURCH",
        "SCHOOL HOUSE",
        "SEVEN BRIDGES",
        "SEVEN PATHS",
        "SHADY LANE",
        "SHEEP PASTURE",
        "SHILOH CHURCH",
        "SHILOH SCHOOL",
        "SOUTHERN NASH HIGH",
        "SPEIGHT CHAPEL",
        "SPEIGHTS CHAPEL",
        "SPENCER COCKRELL",
        "SPRING HILL",
        "SPRING HOPE",
        "SQUIRREL DEN",
        "ST JOHN",
        "STANLEY PARK",
        "STONE HERITAGE",
        "STONE PARK",
        "STONE ROSE",
        "STONE WHITLEY",
        "STONEY CREEK",
        "STONEY HILL CHURCH",
        "STRAIGHT GATE",
        "SUGAR HILL",
        "SUTTERS CREEK",
        "SWIFT CREEK SCHOOL",
        "TABERNACLE CHURCH",
        "TAR RIVER CHURCH",
        "TAYLORS GIN",
        "TAYLORS STORE",
        "THOMAS A BETTS",
        "THOMAS BETTS",
        "THOMPSON CHAPEL",
        "THORNE FARM",
        "TOWN CREEK",
        "TRESSELL LOOP",
        "TYSON LOOP",
        "VALLEY CREEK",
        "VAUGHAN CHAPEL",
        "VILLAGE SQUARE",
        "WAGON WHEEL",
        "WATER FRONT",
        "WATER LOO",
        "WATERS EDGE",
        "WATSON SED FARM",
        "WATSON SEED FARM",
        "WATSONS COVE",
        "WEBBS MILL",
        "WEST MOUNT",
        "WEST NASHVILLE",
        "WESTERN HILLS",
        "WESTRIDGE CIRCLE",
        "WESTVIEW PARK",
        "WHEELESS CABIN",
        "WHITE OAK",
        "WHITLEY CIRCLE",
        "WILD RED",
        "WINDCHASE POINTE",
        "WINDY TRAILS",
        "WINSTEAD STORE",
        "WOLLETT MILL"
     );
  }
  
  @Override
  public String getFilter() {
    return "nash911@nashcountync.gov,9300";
  }
  
  @Override
  protected boolean isNotExtraApt(String apt) {
    if (apt.indexOf('/') >= 0) return true;
    return super.isNotExtraApt(apt);
  };
  
  @Override
  public boolean parseMain(String body, Data data) {
  
    body = stripFieldStart(body, "/ ");
    body = body.replace("\n", "  ");
    if (!body.startsWith("NASH911:")) return false;
    body = body.substring(8);
    boolean mBlankFmt = body.startsWith("  ");
    body = body.trim();
    
    // If we are lucky, we can count on the fields being separated by multiple blanks
    if (mBlankFmt) {
      
      // Split out fields by multiple blank separators
      String[] flds = body.split("  +");
      int spt = 0;
      int ept = flds.length;
     
      // First field has to be the address
      String addr = flds[spt++].replace("//", "/");
      parseAddress(StartType.START_ADDR, FLAG_RECHECK_APT | FLAG_ANCHOR_END, addr, data);
      
      // Second field has to be city.  Unless we found city in first field
      // Or unless second field is apt and third field is city
      if (data.strCity.length() == 0) {
        if (spt >= ept) return false;
        String city = flds[spt++];
        if (!isCity(city)) {
          if (CHANNEL_PTN.matcher(city).matches()) {
            data.strChannel = city;
          } else {
            data.strApt = append(data.strApt, "-", city);
          }
          if (spt >= ept) return false;
          city = flds[spt++];
          if (!isCity(city)) return false;
        }
        data.strCity = convertCodes(city, CITY_FIXES);
      }
      
      // Now lets start working from the end.
      // First check for a special info field
      String lastFld = flds[--ept];
      if (HAZARD_PTN.matcher(lastFld).matches()) {
        data.strSupp = lastFld;
        if (spt > ept) return false;
        lastFld = flds[--ept];
      }
      
      // Or a truncated Hazards: label
      else if ("Hazards:".startsWith(lastFld)) {
        if (spt > ept) return false;
        lastFld = flds[--ept];
      }
      
      // Check for (possibly multiple) unit fields 
      // at end.
      while (true) {
        if (spt > ept) return false;
        if (!UNIT_PTN.matcher(lastFld).matches()) break;
        data.strUnit = append(lastFld.replace(' ', '_'), ",", data.strUnit);
        lastFld = flds[--ept];
      }
      
      // This may be preceded by a phone number
      if (PHONE_PTN.matcher(lastFld).matches()) {
        data.strPhone = lastFld;
        if (spt > ept) return false;
        lastFld = flds[--ept];
      }
      
      // Or a empty "252-   -    " phone number which will be split across two fields
      else if (lastFld.equals("-") && flds[ept-1].equals("252-")) {
        ept--;
        if (spt > ept) return false;
        lastFld = flds[--ept];
      }
      
      // Which may be preceded by a (possibly 2 part) name
      if (CALL_LIST.getCode(lastFld) == null) {
        if (!lastFld.equals("UNK")) data.strName = cleanWirelessCarrier(lastFld);
        if (spt > ept) return false;
        lastFld = flds[--ept];
        
        if (spt <= ept && CALL_LIST.getCode(lastFld) == null && 
            CALL_LIST.getCode(flds[ept-1]) != null) {
          data.strName = lastFld + ' ' + data.strName;
          lastFld = flds[--ept];
        }
      }
      
      // Which is preceded by a call description
      data.strCall = lastFld;
      if (spt > ept) return false;
      lastFld = flds[--ept];
      
      // Which may be preceded by a call code
      if (CODE_TABLE.getResult(lastFld, true) != null) {
        data.strCode = lastFld;
        if (spt > ept) return false;
        lastFld = flds[--ept];
      }
      
      // Check for a Geo|Place|Landmark Comment: info field
      while (true) {
        Matcher match = COMMENT_LABEL_PTN.matcher(lastFld);
        if (!match.matches()) break;
        String info = match.group(1);
        if (info == null) {
          data.strPlace = match.group(2);
        } else {
          data.strSupp = append(info, "\n", data.strSupp);
        }
        if (spt > ept) return false;
        lastFld = flds[--ept];
      }
      
      // Anything that hasn't been processed is a cross street
      for (int ii = spt; ii <= ept; ii++) {
        data.strCross = append(data.strCross, " / ", flds[ii]);
      }
    }
    
    // Multiple blanks have been removed, we have do this the hard way
    else {
      body = body.replace("//", "/");
      parseAddress(StartType.START_ADDR, FLAG_RECHECK_APT | FLAG_CROSS_FOLLOWS, body, data);
      String left = getLeft();
      if (left.length() == 0) return false;
      
      // Last token of what is left is "usually" a unit designation
      // But only if it contains at least one digit
      Parser p =  new Parser(left);
      String unit = p.getLast(' ');
      if (UNIT_PTN.matcher(unit).matches()) {
        data.strUnit = unit.replace(' ', '_');
        left = p.get();
      }
      
      // If there is a landmark comment, it nicely separates the cross streets
      // from the comment & call description.  But sorting out where the comment/place
      // ends and the call description begins will require checking each word to 
      // see if it starts a known call description
      Matcher match = COMMENT_LABEL_PTN.matcher(left);
      if (match.find()) {
        left = left.substring(0,match.start()).trim();
        parseAddress(StartType.START_ADDR, FLAG_ONLY_CROSS | FLAG_IMPLIED_INTERSECT | FLAG_ANCHOR_END, left, data);
        
        String extra = match.group(1);
        boolean savePlace = (extra == null);
        if (savePlace) extra = match.group(2);
        
        boolean lastBlank = true;
        boolean found = false;
        for (int pt = 0; pt < extra.length(); pt++) {
          if (Character.isWhitespace(extra.charAt(pt))) {
            lastBlank = true;
          } else if (lastBlank) {
            lastBlank = false;
            String call = CALL_LIST.getCode(extra.substring(pt), true);
            if (call != null) {
              found = true;
              data.strCall = call;
              String tmp = extra.substring(0,pt).trim();
              if (savePlace) data.strPlace = tmp;
              else data.strSupp = tmp;
              data.strName = cleanWirelessCarrier(extra.substring(pt+call.length()).trim());
              break;
            }
          }
        }
        
        // If we could not find a call description, dump everything in the info field.
        if (!found) {
          data.strSupp = extra;
        }
      }
      
      // Otherwise, there may be a  cross street at the start of what is left.  But we
      // will check for a recognized call description first, lest we get tripped
      // up by something like STRUCTURE PAUL LANE being misinterpreted as a 
      // cross street
      else {
        CodeTable.Result cRes = CODE_TABLE.getResult(left);
        if (cRes == null) {
          Result res = parseAddress(StartType.START_ADDR, FLAG_ONLY_CROSS | FLAG_IMPLIED_INTERSECT, left);
          if (res.isValid()) {
            res.getData(data);
            left = res.getLeft();
            cRes = CODE_TABLE.getResult(left);
          }
        }
        
        // Now things get sticky.
        // What is left is either specific call code (which may be multiple words)
        // followed by a name.  Or is all call description :(
        if (cRes != null) {
          data.strCode = cRes.getCode();
          data.strCall  = cRes.getDescription();
          data.strName = cleanWirelessCarrier(cRes.getRemainder());
        } else {
          data.strCall = left;
        }
      }
    }
    data.strAddress = data.strAddress.replace("LEEGETT", "LEGGETT");
    data.strCity = convertCodes(data.strCity, CITY_FIXES);
    return true;
  }
  
  @Override
  public CodeSet getCallList() {
    return CALL_LIST;
  }

  @Override
  public String adjustGpsLookupAddress(String addr) {
    Matcher match = US64_PTN.matcher(addr);
    if (match.matches()) addr = match.group(1) + " US64";
    return addr;
  }
  private static final Pattern US64_PTN = Pattern.compile("(\\d+) US ?64(?: [EW]B)?");
  
  private static final Properties GPS_LOOKUP_TABLE = buildCodeTable(new String[]{
      "454 US64", "35.945492,-78.039942",
      "455 US64", "35.949149,-78.023140",
      "456 US64", "35.955508,-78.007927",
      "457 US64", "35.966097,-77.995484",
      "458 US64", "35.975398,-77.982828",
      "459 US64", "35.981547,-77.967077",
      "460 US64", "35.976349,-77.950999",
      "461 US64", "35.974279,-77.934634",
      "462 US64", "35.976803,-77.918041",
      "463 US64", "35.981781,-77.895261",
      "464 US64", "35.981860,-77.876863"

  });
  
  private static final CodeTable CODE_TABLE = new CodeTable(
      
        // Fire:
        "911 HANGUP", "911 HANGUP",
        "ADMIN-C",    "SERVICE CALL",
        "ADMIN-H",    "SERVICE CALL",
        "ALARM-FIRE", "FIRE ALARM",
        "ALARMS-C",   "UNKNOWN ALARM",
        "ALARMS-H",   "UNKNOWN ALARM",
        "ELECTRICAL", "ELECTRICAL",
        "EXPLOSION",  "EXPLOSION",
        "FUEL SPILL", "FUEL SPILL",
        "GAS LEAK",   "GAS LEAK",
        "MISC-C",     "MISC PROBLEM",
        "MISS PER-C", "MISSING PERSON",
        "MISS PER-H", "MISSING PERSON",
        "MUTUAL AID", "MUTUAL AID",
        "MVA PD",     "MVA PROPERTY DAMAGE",
        "MVA PD/OT",  "MVA OTHER",
        "MVA PI-C",   "MVA PERSON INJURED",
        "MVA PI-H",   "MVA PERSON INJURED",
        "MVA PIN-C",  "MVA PERSON PINNED",
        "MVA PIN-H",  "MVA PERSON PINNED",
        "ODOR",       "ODOR INVESTIGATION",
        "OUTSIDE FI", "OUTSIDE FIRE",
        "PUBL SER-C", "SERVICE CALL",
        "PUBL SER-H", "SERVICE CALL",
        "SERV CALL",  "SERVICE CALL",
        "SMOKE INVE", "SMOKE INVESTIGATION",
        "STRUCTURE",  "STRUCTURE FIRE",
        "TRAF/V/H-C", "MVA UNKNOWN",
        "TRAF/V/H-H", "MVA UNKNOWN",
        "VEH FIRE S", "SMALL VEHICLE FIRE",
        "VEH FIRE L", "LARGE VEHICLE FIRE",
        "VEH FIRE-L", "LARGE VEHICLE FIRE",
        "WATER RESC", "WATER RESCUE",
        
        // Police
        "THEFT-H",    "THEFT",

        // Medical:
        "ABD-C",      "ABDOMINAL PAIN",
        "ABD-H",      "ABDOMINAL PAIN",
        "ALLERGY-C",  "ALLERGIC REACTION",
        "ALLERGY-H",  "ALLERGIC REACTION",
        "ANIMAL-M-H", "ANIMAL BITE",
        "ANIMAL-M-L", "ANIMAL BITE",
        "ASSALT-M-C", "INJURED PERSON",
        "ASSALT-M-H", "INJURED PERSON",
        "BK PAIN-C",  "BACK PAIN",
        "BK PAIN-H",  "BACK PAIN",
        "BREATH-C",   "BREATHING DIFFICULTY",
        "BREATH-H",   "BREATHING DIFFICULTY",
        "BURNS-C",    "SUBJECT BURNED",
        "BURNS-H",    "SUBJECT BURNED",
        "CARBON-C",   "CO INHALATION",
        "CARBON-H",   "CO INHALATION",
        "CARD/ARR-C", "CODE BLUE",
        "CARD/ARR-H", "CODE BLUE",
        "CARDIAC-C",  "CODE BLUE",
        "CARDIAC-H",  "CODE BLUE",
        "CHEST-C",    "CHEST PAIN",
        "CHEST-H",    "CHEST PAIN",
        "CONF SPACE", "CONFINED SPACE RESCUE",
        "DIABETIC-C", "DIABETIC EMERGENCY",
        "DIABETIC-H", "DIABETIC EMERGENCY",
        "DISTURB-H",  "DISTURB",
        "FALLS-C",    "SUBJECT FALLEN",
        "FALLS-H",    "SUBJECT FALLEN",
        "HEADACHE-C", "HEADACHE",
        "HEADACHE-H", "HEADACHE",
        "HEART PR-C", "HEART PROBLEMS",
        "HEART PR-H", "HEART PROBLEMS",
        "HEAT/CLD-C", "HEAT EMERGENCY",
        "HEAT/CLD-H", "HEAT EMERGENCY",
        "HEMORRHA-C", "SUBJECT HEMORRHAGING",
        "HEMORRHA-H", "SUBJECT HEMORRHAGING",
        "LIGHTNING",  "LIGHTNING STRIKES",
        "MEDICAL",    "MEDICAL",
        "OB/PREG-H",  "OB/PREGNANCY",
        "OVERDOSE-C", "OVERDOSE",
        "OVERDOSE-H", "OVERDOSE",
        "PSYC/SUI-C", "PSYCHIATRIC PROBLEM",
        "PSYC/SUI-H", "PSYCHIATRIC PROBLEM",
        "SEIZURES-C", "SEIZURES",
        "SEIZURES-H", "SEIZURES",
        "SHOTSTAB-C", "SHOOTING/ STABBING",
        "SHOTSTAB-H", "SHOOTING/ STABBING",
        "SICK-C",     "SICK CALL",
        "SICK-H",     "SICK CALL",
        "STROKE-C",   "STROKE/ CVA",
        "STROKE-H",   "STROKE/ CVA",
        "SUSPWANT-C", "ASSIST LAW",
        "SUSPWANT-H", "ASSIST LAW",
        "TRANSFER-C", "TRANSFER ASST",
        "TRANSFER-H", "TRANSFER ASST",
        "TRAUMA-C",   "INJURED PERSON",
        "TRAUMA-H",   "INJURED PERSON",
        "UNCONSC-C",  "UNRESPONSIVE PERSON",
        "UNCONSC-H",  "UNRESPONSIVE PERSON",
        "UNK PROB-C", "UNKNOWN MEDICAL",
        "UNK PROB-H", "UNKNOWN MEDICAL",
        "WARRANT SERV", "WARRANT SERVICE"

  );
  
  private static final CodeSet CALL_LIST = new CodeSet(
      "ABDOMINAL PAIN",
      "ABDOMINAL PAINS/PROBLEMS - COLD",
      "ABDOMINAL PAINS/PROBLEMS - HOT",
      "ALLERGIES,REACTIONS,ENVENOMATIONS-HOT",
      "ALL FIRE RELATED ALARMS",
      "ALL LARGE VEHICLE RELATED FIRES - INCLUDING TRACTOR TRAILERS",
      "ALL LAW ENFORCEMENT REALTED ALARMS-COLD",
      "ALL SMALL VEHICLE RELATED FIRES",
      "ANIMAL BITES,ATTACKS-EMERGENCY-HOT (EMS & FRSP ONLY)",
      "ANY OUTSIDE FIRE,GRASS,BRUSH, GRILL, DOG HOUSE, PUMP HOUSE",
      "ASSIST OTHER LAW ENFORCEMENT AGENCY-COLD",
      "BOMB FOUND/SUSPICIOUS PACKAGE (LETTER/ITEM)-HOT",
      "BREATHING DIFFICULTY",
      "BREATHING PROBLEMS-EMERGENCY",
      "BREATHING PROBLEMS-ROUTINE",
      "CARBON MONOXIDE/INHALATION/HAZ MAT/CRBN-HOT",
      "CARDIAC OR RESPIRATORY ARREST-HOT",
      "CHEST PAIN",
      "CHEST PAIN NON-TRAUMATIC-EMERGENCY",
      "CHEST PAIN NON-TRAUMATIC-ROUTINE",
      "CHOKING-EMERGENCY",
      "CHOKING-ROUTINE",
      "CONF. SPACE / STRUCT COLLAPSE/OTHER NON VEH ENTRAPMENT",
      "CONVALESCENT TRANSFER",
      "DEPT TRANSPORTATION",
      "DIABETIC PROBLEMS-EMERGENCY",
      "DIABETIC PROBLEMS-ROUTINE",
      "DOCUMENTS,LOST/FOUND PROP.,MESSAGES,TRANSPORTS-COLD",
      "ELECTRICAL HAZZARD OUSIDE/AWAY FROM A STRUCTURE",
      "ELECTROCUTION/LIGHTNING",
      "EMERGENCY FIRE DISPATCH",
      "EMERGENCY MEDICAL DISPATCH",
      "EMERGENCY POLICE DISPATCH",
      "EXTRICATION/ENTRAPPED (MACHINERY, VEHICLE)",
      "EYE PROBLEMS/INJURIES-EMERGENCY",
      "EYE PROBLEMS/INJURIES-ROUTINE",
      "FALLS-EMERGENCY",
      "FALLS-ROUTINE",
      "FALLS-LIFTING ASST. ROUTINE - EMS ONLY",
      "FUEL SPILL",
      "FIGHTS/PHYSICAL/SEXUAL ASSALT W/ INJUR-HOT (FRSP & EMS ONLY)",
      "FIRE ALARM",
      "GAS LEAK",
      "GAS LEAK/GAS ODOR/ NATURAL OR LP GAS LEAK",
      "HEADACHE-EMERGENCY",
      "HEADACHE-ROUTINE",
      "HEART PROBLEMS",
      "HEART PROBLEMS/AICD-EMERGENCY",
      "HEART PROBLEMS/AICD-ROUTINE",
      "HEAT/COLD EXPOSURE-EMERGENCY",
      "HEAT/COLD EXPOSURE-ROUTINE",
      "HEMORRHAGE/LACERATIONS/BLEEDING-EMERGENCY",
      "HEMORRHAGE/LACERATIONS/BLEEDING-COLD",
      "HIGH ANGLE RESCUE",
      "LIGHTNING STRIKES",
      "LIQUID,SOLID MATERIAL RELEASED OR SPILLED",
      "LOCK-IN/OUT, WELFARE CHECK, SECURITY CHECKS-COLD -SPECIFY",
      "LOCK-IN/OUT, WELFARE CHECK, SECURITY CHECKS-HOT -SPECIFY",
      "MENTAL DISORDER/BEHAVIOR PROBLEMS/SUICIDAL-HOT FRSP&EMS",
      "MISCELLANEOUS CALLS FOR SERVICE-COLD - SPECIFY IN",
      "MOTOR VEH ACCIDENT PROPERTY DAMAGE ONLY VEH OVERTURNED",
      "MOTOR VEH ACCIDENT WITH INJURIES NO ONE PINNED -HOT",
      "MOTOR VEHICLE ACCIDENT WITH INJURIES-COLD",
      "MOTOR VEHICLE ACCIDENT WITH PROPERTY DAMAGE ONLY",
      "MOTOR VEHICLE ACC WITH INJURIES-UNK/PIN IN-HOT",
      "MUTUAL AID ASSIST ANOTHER DEPT - FIRE & EMS ONLY",
      "MVA PERSON INJURED",
      "MVA PERSON PINNED",
      "NON-TRAUMATIC OR NON-RECENT TRAUMA-COLD",
      "OB/CHILDBIRTH/MISCARR-HOT",
      "OD/POISONING-EMERGENCY",
      "OD/POISONING-ROUTINE",
      "OUTSIDE FIRE",
      "PATIENT TRANSFER ROUTINE/EMERGENCY - SPECIFY IN NARRATIVE",
      "SCALDS/BURNS ETC.-HOT",
      "SEIZURES",
      "SEIZURES/CONVULSIONS-EMERGENCY",
      "SEIZURES/CONVULSIONS-ROUTINE",
      "SERV CALL",
      "SERVICE/CITIZEN ASSISTANCE CALLS/",
      "SHOOTING,STABBING, OR OTHER PENETRATING TRAUMA-EMERGENCY",
      "SHOOTING,STABBING, OR OTHER PENETRATING TRAUMA-ROUTINE",
      "SICK CALL-EMERGENCY",
      "SICK CALL-ROUTINE",
      "SMOKE INVESTIGATION OUTSIDE OF A STRUCTURE",
      "STROKE/CVA-EMERGENCY",
      "STROKE/CVA-ROUTINE",
      "STRUCTURE FIRE",
      "SUBJECT FALLEN",
      "TRAFFIC VIOLATION/COMPLAINT/HAZARD/LIVESTOCK/-COLD",
      "TRAUMATIC INJURIES SPECIFIC/ EMERG",
      "TRAUMATIC INJURIES SPECIFIC/ ROUTINE",
      "UNCONSCIOUS/FAINTING-EMERGENCY",
      "UNCONSCIOUS/FAINTING-ROUTINE",
      "UNK TYPE CALL 3RD PARTY CALLER- LAW ENFORCEMENT ONLY-HOT",
      "UNKNOWN PROBLEM/PERSON DOWN -HOT (EMS RELATED)",
      "UNRESPONSIVE PERSON",
      "WARRANT SERV",
      "WARRANT SERVICE",
      "WATER RESCUE"
  );
  
  private static final String[] CITY_LIST = new String[]{
    
      //  Cities
      "BAILEY",
      "CASTALIA",
      "CASTLIA",          // Typo
      "DORTCHES",
      "ELM CITY",
      "MIDDLESEX",
      "MOMEYER",
      "NASHVILLE",
      "RED OAK",
      "ROCKY MOUNT",
      "SHARPSBURG",
      "SPRING HOPE",
      "WHITAKERS",
      "ZEBULON",
      
      // Townships
      "BAILEY",
      "BATTLEBORO",
      "CASTALIA",
      "COOPERS",
      "DRY WELLS",
      "FERRELLS",
      "GRIFFINS",
      "JACKSON",
      "MANNINGS",
      "NASHVILLE",
      "NORTH WHITAKERS",
      "OAK LEVEL",
      "RED OAK",
      "ROCKY MOUNT",
      "SPRING HOPE",
      "SOUTH WHITAKERS",
      "STONY CREEK",
      
      // Franklin County
      "LOUISBURG",
      
      // Halifax county
      "AURELIAN SPRINGS",
      "BRINKLEYVILLE",
      "HEATHSVILLE",
      "ENFIELD",
      "HALIFAX",
      "HOBGOOD",
      "LITTLETON",
      "HOLLISTER",
      "ROANOKE RAPIDS",
      "SCOTLAND NECK",
      "SOUTH ROSEMARY",
      "SOUTH WELDON",
      "WELDON",

      "BUTTERWOOD",
      "CONOCONNARA",
      "FAUCETT",
      "PALMYRA",
      "ROSENEATH",
      
      // Wilson County
      "ELM CITY",
      "SIMS",
      "WILSON",
      
      // Counties
      "EDGECOMBE CO",
      "FRANKLIN CO",
      "JOHNSTON CO",
      "HALIFAX CO",
      "WARREN CO",
      "WILSON CO"
      
  };
  
  private static final Properties CITY_FIXES = buildCodeTable(new String[]{
      "CASTLIA",       "CASTALIA",
      "EDGECOMBE CO",  "EDGECOMBE COUNTY"
  });
}
