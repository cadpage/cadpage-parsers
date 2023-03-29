package net.anei.cadpage.parsers.NC;

import java.util.Properties;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.anei.cadpage.parsers.CodeSet;
import net.anei.cadpage.parsers.CodeTable;
import net.anei.cadpage.parsers.MsgInfo.Data;
import net.anei.cadpage.parsers.dispatch.DispatchA3AParser;


public class NCNashCountyAParser extends DispatchA3AParser {

  public NCNashCountyAParser() {
    super(NCNashCountyParser.CITY_LIST, "NASH COUNTY", "NC");
    setFieldList("ID ADDR APT CH CITY X PLACE CODE CALL NAME UNIT PHONE INFO " + getFieldList());
    setupGpsLookupTable(GPS_LOOKUP_TABLE);
    setupMultiWordStreets(MWORD_STREET_LIST);
    setupSpecialStreets("TAR RIVER BRIDGE", "WHITAKERS CITY LIMITS");
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

  private static final Pattern MBLANK_DELIM = Pattern.compile(" {2,}");
  private static final Pattern SEMI_DELIM = Pattern.compile(" ;(?: |$)");
  private static final Pattern ID_PTN = Pattern.compile(" *(\\d\\d-\\d{6})(?= )");
  private static final Pattern CHANNEL_PTN = Pattern.compile("TAC.*", Pattern.CASE_INSENSITIVE);
  private static final Pattern TRUNC_DATE_PTN = Pattern.compile("0[1-9]?|1[0-2]?");
  private static final Pattern UNIT_PTN = Pattern.compile("(?!FIRES)(?:\\b(?:\\d*[A-Z]*\\d+[A-Z]?|\\d+-\\d+|[A-Z]*EMS|[A-Z]*FIR?E|[A-Z]*FD|[A-Z]*RES|[A-Z]*CEM|[A-Z]CSO|DOT|TOISN|SI FR|WAKEM|ST\\d+(?:-\\d+)?|(?:BRUSH|EMS|PPO|Nash Car|UNIT|RESCUE|TRUCK|ENG|SQD) ?\\d+)\\b[ ,]?)+");
  private static final Pattern PHONE_PTN = Pattern.compile("\\d{2}[ 0-9]-\\d{3}-\\d{4}");
  private static final Pattern COMMENT_LABEL_PTN = Pattern.compile("\\b(?:Geo|Landmark|Place|Special) Comment:+ *(.*)|NBH: *(.*)");

  @Override
  public boolean parseMain(String body, Data data) {

    body = stripFieldStart(body, "/ ");
    body = body.replace("\n", "  ");
    if (!body.startsWith("NASH911:")) return false;
    body = body.substring(8);

    // Check for leading ID
    Matcher match = ID_PTN.matcher(body);
    if (match.lookingAt()) {
      data.strCallId = match.group(1);
      body = body.substring(match.end());
    }

    body = body.replace("PERSONAL  ITEMS", "PERSONAL ITEMS");
    body = body.replace("EASTERN NC MEDICAL GROUP  ", "EASTERN NC MEDICAL GROUP ");

    // Check for one of two possible field delimiters
    Pattern delimPtn = SEMI_DELIM;
    match = delimPtn.matcher(body);
    if (!match.lookingAt()) {
      delimPtn = MBLANK_DELIM;
      match = delimPtn.matcher(body);
      if (!match.lookingAt()) delimPtn = null;
    }

    // If we identified a delimiter, use it to split the line
    boolean special = false;
    String[] flds = null;
    if (delimPtn != null) {
      body = body.substring(match.end());
      flds = delimPtn.split(body);
    }

    // If not, but the alerts starts with one leading blank, then see if we can break
    // it up with the multi-blank delimiter.  If it set a flag to check some fields
    // that are not properly split in this mode
    else {
      if (body.startsWith(" ")) {
        special = true;
        flds = MBLANK_DELIM.split(body.trim());
        if (flds.length < 4) flds = null;
      }
    }

    // Did any of those field splitting techniques work?
    if (flds != null) {

      // Split out fields
      int spt = 0;
      int ept = flds.length;

      // First field has to be the address
      String addr = flds[spt++].replace("//", "/");
      parseAddress(StartType.START_ADDR, FLAG_IMPLIED_INTERSECT | FLAG_RECHECK_APT | FLAG_ANCHOR_END, addr, data);

      // This has to be followed by city, but there can be a multi-part
      // apt field before we get to the city.  In special parsing mode
      // the city is combined with the following field, so we have to
      // extract the rest of the field and put it back in the array.
      if (data.strCity.length() == 0) {
        if (spt >= ept) return false;
        String city = flds[spt++];
        while (true) {
          int flags = FLAG_ONLY_CITY;
          if (!special) flags |= FLAG_ANCHOR_END;
          parseAddress(StartType.START_ADDR, flags, city, data);
          if (data.strCity.length() > 0) {
            if (special) {
              String left = getLeft();
              if (left.length() > 0) {
                flds[--spt] = left;
              }
            }
            break;
          }

          if (CHANNEL_PTN.matcher(city).matches()) {
            data.strChannel = append(data.strChannel, "/", city);
          } else {
            data.strApt = append(data.strApt, " ", city);
          }
          if (spt >= ept) return false;
          city = flds[spt++];
        }
      }
      data.strCity = convertCodes(data.strCity, NCNashCountyParser.CITY_FIXES);

      // Now lets start working from the end.
      // First check for truncated date field
      String lastFld = flds[--ept];
      if (TRUNC_DATE_PTN.matcher(lastFld).matches()) {
        if (spt > ept) return false;
        lastFld = flds[--ept];
      }

      // Next check for a special info field
      if (lastFld.startsWith("Hazards: ")) {
        data.strSupp = lastFld;
        if (spt > ept) return false;
        lastFld = flds[--ept];
      }

      // Or a truncated Hazards: label
      else if ("Hazards:".startsWith(lastFld)) {
        if (spt > ept) return false;
        lastFld = flds[--ept];
      }

      // Ditto for Medical: label
      if (lastFld.startsWith("Medical: ")) {
        data.strSupp = append(lastFld, "\n", data.strSupp);
        if (spt > ept) return false;
        lastFld = flds[--ept];
      }

      else if ("Medical:".startsWith(lastFld)) {
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

      // Things are different for special field parsing
      if (special) {

        // One field contains special, call code and description and name
        // Which we just happen to have a cool method to parse
        parseCallInfo(false, lastFld, data);
        lastFld = flds[--ept];
      }

      // Regular (non-special) field parsing

      else {

        // Look for a call description or code
        // followed by 0-2 name fields
        int pt = ept;
        boolean found = parseCall(lastFld, data);
        if (!found && --pt >= spt) {
          found = parseCall(flds[pt], data);
          if (!found && --pt >= spt) {
            found = parseCall(flds[pt], data);
          }
        }

        // If we found one, anything between it and ept
        // should form the name
        if (found) {
          if (pt < ept) {
            String name = "";
            for (int tpt = pt+1; tpt<=ept; tpt++) {
              if (!flds[tpt].equals("UNK")) {
                name = append(name, " ", flds[tpt]);
              }
            }
            data.strName = cleanWirelessCarrier(name);
            ept = pt;
          }

          // If we found a call description, but not a code, see
          // if the previous field is a recognized call code
          lastFld = flds[--ept];
          if (spt <= ept) {
            CodeTable.Result cRes = CODE_TABLE.getResult(lastFld, true);
            if (cRes != null && cRes.getCode().length() == lastFld.length()) {
              data.strCode = lastFld;
              lastFld = flds[--ept];
            }
          }
        }

        // If not, just use what we have for a call description
        else {
          data.strCall = lastFld;
          lastFld = flds[--ept];
        }
      }

      // Check for a Geo|Place|Landmark Comment: info field
      // Occasionally these fields contain multiple blanks splitting
      // them across two more data fields
      for (int pt = ept; pt >= spt; pt--) {
        String fld = flds[pt];
        match = COMMENT_LABEL_PTN.matcher(fld);
        if (match.matches()) {
          boolean place = false;
          String info = match.group(1);
          if (info == null) {
            place = true;
            info = match.group(2);
          }
          for (int ii = pt+1; ii<=ept; ii++) {
            info = append(info, " ", flds[ii]);
          }
          if (place) {
            data.strPlace = append(info, " - ", data.strPlace);
          } else {
            data.strSupp = append(info, "\n", data.strSupp);
          }
          ept = pt-1;
        }
      }

      // Anything that hasn't been processed is a cross street
      // If there is only one, check it for implied separators
      if (ept >= spt) {
        if (ept == spt) {
          parseAddress(StartType.START_ADDR, FLAG_ONLY_CROSS | FLAG_IMPLIED_INTERSECT | FLAG_NO_CITY | FLAG_ANCHOR_END, flds[spt], data);
        } else {
          for (int ii = spt; ii <= ept; ii++) {
            data.strCross = append(data.strCross, " / ", flds[ii]);
          }
        }
      }
    }

    // Multiple blanks have been removed, we have do this the hard way :(
    else {

      // Strip off Hazard notice
      int pt = body.indexOf(" Hazards:");
      if (pt >= 0) {
        data.strSupp = body.substring(pt+1).trim();
        body = body.substring(0,pt).trim();
      }

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

      // And parse what is left into cross street special, call and name information
      parseCallInfo(true, left, data);
    }
    data.strAddress = data.strAddress.replace("LEEGETT", "LEGGETT");
    data.strCity = convertCodes(data.strCity, NCNashCountyParser.CITY_FIXES);
    return true;
  }

  /**
   * This is called to handle a combined cross special code call field
   * It is called from two different format parsers, one of which will
   * not include a leading cross street
   * @param leadCross true if we should check for a leading cross street
   * @param field the data field
   * @param data parsed data object
   */
  private void parseCallInfo(boolean leadCross, String field, Data data) {

    // If there is a landmark comment, it nicely separates the cross streets
    // from the comment & call description.  But sorting out where the comment/place
    // ends and the call description begins will require checking each word to
    // see if it starts a known call description
    Matcher match = COMMENT_LABEL_PTN.matcher(field);
    if (leadCross ? match.find() : match.lookingAt()) {
      if (leadCross) {
        String cross = field.substring(0,match.start()).trim();
        parseAddress(StartType.START_ADDR, FLAG_ONLY_CROSS | FLAG_IMPLIED_INTERSECT | FLAG_ANCHOR_END, cross, data);
      }

      // Parse the landmark comment information
      String extra = match.group(1);
      boolean savePlace = (extra == null);
      if (savePlace) extra = match.group(2);

      // Start scanning through the comment information
      // for every word start, see if it matches a know call code
      // or call description
      boolean lastBlank = true;
      boolean found = false;
      for (int pt = 0; pt < extra.length(); pt++) {
        if (Character.isWhitespace(extra.charAt(pt))) {
          lastBlank = true;
        } else if (lastBlank) {
          lastBlank = false;
          String tmp = extra.substring(pt);
          CodeTable.Result cRes = CODE_TABLE.getResult(tmp, true);
          if (cRes != null) {
            found = true;
            data.strCode = cRes.getCode();
            data.strCall = cRes.getDescription();
            tmp = cRes.getRemainder();
            String call = CALL_LIST.getCode(tmp, true);
            if (call != null) {
              data.strCall = call;
              tmp = tmp.substring(call.length()).trim();
            }
          }
          else {
            String call = CALL_LIST.getCode(tmp, true);
            if (call != null) {
              found = true;
              data.strCall = call;
              tmp = tmp.substring(call.length()).trim();
            }
          }

          if (found) {
            String info = extra.substring(0,pt).trim();
            if (savePlace) data.strPlace = info;
            else data.strSupp = info;

            data.strName = cleanWirelessCarrier(tmp);
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
      CodeTable.Result cRes = CODE_TABLE.getResult(field);
      if (leadCross && cRes == null) {
        Result res = parseAddress(StartType.START_ADDR, FLAG_ONLY_CROSS | FLAG_IMPLIED_INTERSECT, field);
        if (res.isValid()) {
          res.getData(data);
          field = res.getLeft();
          cRes = CODE_TABLE.getResult(field);
        }
      }

      // Now things get sticky.
      // What is left is either specific call code (which may be multiple words)
      // followed by a name.  Or is all call description :(
      if (cRes != null) {
        data.strCode = cRes.getCode();
        field = cRes.getRemainder();
        String call = CALL_LIST.getCode(field, true);
        if (call != null) {
          field = field.substring(call.length()).trim();
        } else {
          call = cRes.getDescription();
        }
        data.strCall  = call;
        data.strName = cleanWirelessCarrier(field);
      } else {
        data.strCall = field;
      }
    }
  }

  private boolean parseCall(String field, Data data) {
    String call = CALL_LIST.getCode(field, true);
    if (call != null && call.length() == field.length()) {
      data.strCall = field;
      return true;
    }
    CodeTable.Result cRes = CODE_TABLE.getResult(field, true);
    if (cRes != null && cRes.getCode().length() == field.length()) {
      data.strCode = field;
      data.strCall = cRes.getDescription();
      return true;
    }

    return false;
  }

  @Override
  public CodeSet getCallList() {
    return CALL_LIST;
  }

  @Override
  public String adjustMapAddress(String sAddr) {
    return sAddr.replace("OLD SH RD", "OLD SPRING HOPE RD");
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

  private static final String[] MWORD_STREET_LIST = new String[]{
      "ADOLPHUS T BOONE",
      "AIR TERMINAL",
      "ANNIE LEE",
      "ANNIE LEIGH",
      "ASH LILY",
      "AVENTON GIN",
      "BAINES LOOP",
      "BALDY HILL",
      "BARNES HILL CH",
      "BARNES HILL CHURCH",
      "BARNHILL FARM",
      "BATTLE PARK",
      "BATTLEBORO LEEGETT",
      "BATTLEBORO LEGGET",
      "BATTLEBORO LEGGETT",
      "BATTLEBORO LEGGIT",
      "BEAVER DAM",
      "BELLAMY MILL",
      "BEND OF THE RIVER",
      "BIG WOODS",
      "BLACK CLOUD",
      "BLUE HERON",
      "BODDIE MILL POND",
      "BONE ACRES",
      "BONE POND",
      "BONES ACRES",
      "BONES CUT OFF",
      "BRADLEY CUTCHIN",
      "BRANTLEY PARK",
      "BRIAR CREEK",
      "BRIDGE TENDER",
      "BUCK DEANS",
      "BUCKS DEANS",
      "BULL HEAD",
      "BULLOCK SCHOOL",
      "BULLUCK SCHOOL",
      "BURNT MILL",
      "CABIN PATH",
      "CAMP CHARLES",
      "CARRIAGE FARM",
      "CARRIAGE HOUSE",
      "CARTER GROVE",
      "CASCADE CREEK",
      "CASTALIA LOOP",
      "CEDAR GROVE SCHOOL LOOP",
      "CEDAR LAKE",
      "CHILDRENS HOME",
      "CHUCK WAGON",
      "CLAUDE LEWIS",
      "COKER TOWN",
      "COLLIE TYSON",
      "COOL SPRING",
      "COOL SPRINGS",
      "COOPER FIELDS",
      "COOPERS SCHOOL",
      "COUNTRY CLUB",
      "COUNTY HOME",
      "COUNTY LINE",
      "CRESCENT MEADOWS",
      "CROOKED SWAMP",
      "CROSS CREEK",
      "CURTIS ELLIS",
      "DAVIS STORE",
      "DAVIS WORRELL",
      "DOUBLE WIDE",
      "DUNBAR WOODS",
      "EAGLE RIDGE",
      "EAST EVANS",
      "EL SHADDAI",
      "ERKIN SMITH",
      "FIRE TOWER FARM",
      "FIRE TOWER",
      "FISHING CREEK",
      "FLAG POND LOOP",
      "FLAT ROCK",
      "FLOOD STORE",
      "FLOODS STORE",
      "FLOWER HILL",
      "FOREST COVE",
      "FOREST EDGE",
      "FOREST HILL",
      "FOREST VIEW",
      "FOUNTAIN BRANCH",
      "FOX RUN",
      "GASKILL FARM",
      "GEORGE PACE",
      "GLOVER PARK MEMORIAL",
      "GOLD ROCK",
      "GREAT BRANCH",
      "GREEN HILLS",
      "GREEN PASTURE",
      "GREEN POND LOOP",
      "GREEN RIDGE",
      "GREYS MILL",
      "GRIFFIN FARM",
      "GROVE CHURCH",
      "HARPER FARM",
      "HARRIS COLLIE",
      "HORNES CHURCH",
      "HUNTER HILL",
      "HUNTER RIDGE",
      "JAMES BUNN",
      "JOE ELLEN",
      "JOHN J SHARPE",
      "JUSTICE BRANCH",
      "KING CHARLES",
      "L A GREEN FARM",
      "LAKE HAVEN",
      "LAKE RIDGE",
      "LAKE ROYALE",
      "LAKE VIEW",
      "LANCASTER STORE",
      "LEMON BRIDGE",
      "LEWIS SCHOOL",
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
      "OFF DORTCHES",
      "P TAYLOR STORE",
      "PEACHTREE HILLS",
      "PINE PARK",
      "PLANER MILL",
      "PLEASANT GROVE CHURCH",
      "PLEASANT GROVE",
      "POLE CAT",
      "PREACHER JOYNER",
      "PULLEN PASTURE",
      "QUEEN ANNE",
      "QUIET WATERS",
      "RACE TRACK",
      "RALEIGH WILSON",
      "RED BUD",
      "RED COLEY",
      "RED OAK BATTLEBORO",
      "RED OAK HILLS",
      "RED OAK",
      "REEDY BRANCH",
      "REGES STORE",
      "RIVER BEND",
      "RIVER GLENN",
      "RIVER LAKE",
      "ROCK QUARRY",
      "ROCK RIDGE",
      "ROCKY CROSS",
      "ROLLING ACRES",
      "ROLLING ROCK",
      "ROSE HILL",
      "ROSE LOOP",
      "SALEM SCHOOL",
      "SAMARIA CHURCH",
      "SANCTIFIED CHURCH",
      "SANDY CROSS",
      "SANDY FORK",
      "SANDY HILL CHURCH",
      "SANDY KNOB",
      "SAPONY CREEK",
      "SCHOOL HOUSE",
      "SEVEN BRIDGES",
      "SEVEN PATHS",
      "SHADY LANE",
      "SHEEP PASTURE",
      "SHILOH CHURCH",
      "SHILOH SCHOOL",
      "SOCIAL PLAIN",
      "SOUTHERN NASH HIGH",
      "SPEIGHT CHAPEL",
      "SPEIGHTS CHAPEL",
      "SPENCER COCKRELL",
      "SPRING HILL",
      "SPRING HOPE",
      "SPRING POND",
      "SQUIRREL DEN",
      "ST JOHN",
      "STANLEY PARK",
      "STONE HERITAGE",
      "STONE PARK",
      "STONE ROSE",
      "STONE WHITLEY",
      "STONEY CREEK",
      "STONEY HILL CHURCH",
      "STONY CREEK",
      "STRAIGHT GATE",
      "SUGAR HILL",
      "SUMMERLEA PLACE",
      "SUTTERS CREEK",
      "SWIFT CREEK SCHOOL",
      "TABERNACLE CHURCH",
      "TAR RIVER CHURCH",
      "TAYLOR WOODS",
      "TAYLORS GIN",
      "TAYLORS STORE",
      "THOMAS A BETTS",
      "THOMAS BETTS",
      "THOMPSON CHAPEL",
      "THORNE FARM",
      "TOM GEORGE",
      "TOM MATTHEWS",
      "TOWN CREEK",
      "TOWN HALL",
      "TRESSELL LOOP",
      "TWIN POND",
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
      "WEST HAVEN",
      "WEST MOUNT",
      "WEST NASHVILLE",
      "WESTERN HILLS",
      "WESTRIDGE CIRCLE",
      "WESTVIEW PARK",
      "WHEELESS CABIN",
      "WHITE OAK HILL",
      "WHITE OAK",
      "WHITLEY CIRCLE",
      "WILD RED",
      "WINDCHASE POINTE",
      "WINDY OAK",
      "WINDY TRAILS",
      "WINSTEAD STORE",
      "WOLLETT MILL",

  };

  private static final CodeTable CODE_TABLE = new CodeTable();
  private static final CodeSet CALL_LIST = new CodeSet();

  static {
    boolean isCode = true;
    String code = null;
    for (String value : new String[]{

      // Fire:
      "911 HANGUP", "911 HANGUP",
      "ADMIN-C",    "SERVICE CALL",
      "ADMIN-H",    "SERVICE CALL",
      "AIRCRAFT",   "AIRCRAFT EMERGENCIES/CRASH",
      "ALARM-FIRE", "ALL FIRE RELATED ALARMS",
      "ALARMS-C",   "ALL LAW ENFORCEMENT REALTED ALARMS-COLD",
      "ALARMS-H",   "ALL LAW ENFORCEMENT REALTED ALARMS-HOT",
      "ASST OTH-C", "ASSIST OTHER LAW ENFORCEMENT AGENCY-COLD",
      "ASST OTH-H", "ASSIST OTHER LAW ENFORCEMENT AGENCY-HOT",
      "CHEST-H",    "CHEST PAIN NON-TRAUMATIC-EMERGENCY",
      "ELECTRICAL", "ELECTRICAL",
      "ELECTRO-H",  "ELECTROCUTION/LIGHTNING",
      "EXPLOSION",  "EXPLOSION",
      "EXTRICATIO", "EXTRICATION/ENTRAPPED (MACHINERY, VEHICLE)",
      "FIRE",       "EMERGENCY FIRE DISPATCH",
      "FUEL ODOR",  "FUEL ODOR",
      "FUEL SPILL", "FUEL SPILL",
      "GAS LEAK",   "GAS LEAK/GAS ODOR/ NATURAL OR LP GAS LEAK",
      "HAZMAT",     "LIQUID,SOLID MATERIAL RELEASED OR SPILLED",
      "INACCESS-H", "FARM MACHINARY,INACCESIBLE,OTHER ENTRAP-HOT",
      "MISC-C",     "MISCELLANEOUS CALLS FOR SERVICE-COLD - SPECIFY IN",
      "MISC-H",     "MISCELLANEOUS CALLS FOR SERVICE-HOT - SPECIFY IN",
      "MUTUAL AID", "MUTUAL AID ASSIST ANOTHER DEPT - FIRE & EMS ONLY",
      "MVA PD",     "MOTOR VEHICLE ACCIDENT WITH PROPERTY DAMAGE ONLY",
      "MVA PD/OT",  "MOTOR VEH ACCIDENT PROPERTY DAMAGE ONLY VEH OVERTURNED",
      "MVA PI-C",   "MOTOR VEHICLE ACCIDENT WITH INJURIES-COLD",
      "MVA PI-H",   "MOTOR VEH ACCIDENT WITH INJURIES NO ONE PINNED -HOT",
      "MVA PIN-C",  "MOTOR VEHICLE ACC WITH INJURIES-UNK/PIN IN-COLD",
      "MVA PIN-H",  "MOTOR VEHICLE ACC WITH INJURIES-UNK/PIN IN-HOT",
      "OB/PREG-H",  "OB/CHILDBIRTH/MISCARR-HOT",
      "ODOR",       "ODOR INVESTIGATION",
      "OUTSIDE FI", "ANY OUTSIDE FIRE,GRASS,BRUSH, GRILL, DOG HOUSE, PUMP HOUSE",
      "PUBL SER-C", "LOCK-IN/OUT, WELFARE CHECK, SECURITY CHECKS-COLD -SPECIFY",
      "PUBL SER-H", "LOCK-IN/OUT, WELFARE CHECK, SECURITY CHECKS-HOT -SPECIFY",
      "SERV CALL",  "SERVICE/CITIZEN ASSISTANCE CALLS/",
      "SOCIAL SERV","SOCIAL SERVICES",
      "SMOKE INVE", "SMOKE INVESTIGATION OUTSIDE OF A STRUCTURE",
      "STRUCTURE",  "STRUCTURE FIRE",
      "VEH FIRE S", "ALL SMALL VEHICLE RELATED FIRES",
      "VEH FIRE L", "ALL LARGE VEHICLE RELATED FIRES - INCLUDING TRACTOR TRAILERS",
      "VEH FIRE-L", "ALL LARGE VEHICLE RELATED FIRES - INCLUDING TRACTOR TRAILERS",
      "WATER RESC", "WATER RESCUE",
      null,         "BOMB FOUND/SUSPICIOUS PACKAGE (LETTER/ITEM)-COLD",
      null,         "BOMB FOUND/SUSPICIOUS PACKAGE (LETTER/ITEM)-HOT",
      null,         "CONF. SPACE / STRUCT COLLAPSE/OTHER NON VEH ENTRAPMENT",
      null,         "DEPT TRANSPORTATION",
      null,         "ELECTRICAL HAZZARD OUSIDE/AWAY FROM A STRUCTURE",
      null,         "HIGH ANGLE RESCUE",

      // Police
      "ASSAULT-C",  "FIGHTS/PHYSICAL/SEXUAL ASSAULT-COLD (LAW ENFORCEMENT ONLY)",
      "ASSAULT-H",  "FIGHTS/PHYSICAL/SEXUAL ASSAULT-HOT (LAW ENFORCEMENT ONLY)",
      "BURGLARY-C", "BURGLARY (BREAK AND ENTER) HOME INVASION, - COLD",
      "BURGLARY-H", "BURGLARY (BREAK AND ENTER) HOME INVASION, - HOT",
      "CHASE",      "FOOT OR VEHICLE CHASE",
      "DISTURB-C",  "DISTURBANCE/NUISANCE-COLD",
      "DISTURB-H",  "DISTURBANCE/NUISANCE-HOT",
      "DAMAGE-C",   "DAMAGE/VANDALISM/OR OTHER MISCHIEF ACTIVITY-COLD",
      "DAMAGE-H",   "DAMAGE/VANDALISM/OR OTHER MISCHIEF ACTIVITY-HOT",
      "DOMESTIC-C", "DOMESTIC VIOLENCE/DISTRUBANCE-COLD",
      "DOMESTIC-H", "DOMESTIC VIOLENCE/DISTRUBANCE-HOT",
      "DRUGS-C",    "DRUGS VIOLATION-COLD",
      "DRUGS-H",    "DRUGS VIOLATION-HOT",
      "DWI-C",      "DRIVING UNDER THE INFLUENCE & PEDESTRIANS-COLD",
      "DWI-H",      "DRIVING UNDER THE INFLUENCE & PEDESTRIANS-HOT",
      "HARASS-C",   "HARASSMENT/STALKING/THREATS/ MADE BY PHONE OR IN PERSON-COLD",
      "HARASS-H",   "HARASSMENT/STALKING/THREATS/ MADE BY PHONE OR IN PERSON-HOT",
      "HOPE",       "HOPE PARTICIPANT",
      "MENTAL-C",   "MENTAL DISORDER-COLD (LAW ENFORCEMENT ONLY)",
      "MENTAL-H",   "MENTAL DISORDER-HOT (LAW ENFORCEMENT ONLY)",
      "MISS PER-C", "MISSING/RUNAWAY/FOUND PERSON-COLD",
      "MISS PER-H", "MISSING/RUNAWAY/FOUND PERSON-HOT",
      "POLICE",     "EMERGENCY POLICE DISPATCH",
      "PRISONER-T", "PRISONER TRANSPORT",
      "ROB/CARJ-C", "ARMED ROBBERY/ CARJACKING-COLD",
      "ROB/CARJ-H", "ARMED ROBBERY/ CARJACKING-HOT",
      "SCHOOL VISIT","LAW ENFORCEMENT SCHOOL VISIT",
      "SUPPMENT-C", "FOLLOW-UP INVESTIGATION/ADDITIONAL INFORMATION-COLD",
      "SUPPMENT-H", "FOLLOW-UP INVESTIGATION/ADDITIONAL INFORMATION-HOT",
      "SUSPWANT-C", "SUSPICIOUS PERSON/VEHICLE/CIRCUMSTANCES/WANTED PERSON-COLD",
      "SUSPWANT-H", "SUSPICIOUS PERSON/VEHICLE/CIRCUMSTANCES/WANTED PERSON-HOT",
      "THEFT-C",    "LARCENY/THEFT OF MONEY,PERSONAL ITEMS, VEHICLES, ETC-COLD",
      "THEFT-H",    "LARCENY/THEFT OF MONEY,PERSONAL ITEMS, VEHICLES, ETC-HOT",
      "TRAF/V/H-C", "TRAFFIC VIOLATION/COMPLAINT/HAZARD/LIVESTOCK/-COLD",
      "TRAF/V/H-H", "TRAFFIC VIOLATION/COMPLAINT/HAZARD/LIVESTOCK/-HOT",
      "TRAFFIC STOP", "TRAFFIC STOP",
      "TRESPASS-C", "TRESPASSING/UNWANTED PERSON(S)-COLD",
      "TRESPASS-H", "TRESPASSING/UNWANTED PERSON(S)-HOT",
      "UNKNOWN-C",  "UNK TYPE CALL 3RD PARTY CALLER- LAW ENFORCEMENT ONLY-COLD",
      "UNKNOWN-H",  "UNK TYPE CALL 3RD PARTY CALLER- LAW ENFORCEMENT ONLY-HOT",
      "WARRANT SERV", "WARRANT SERVICE",
      "WEAPONS-C",  "POSSIBLE GUNSHOTS HEARD/INCIDENTS INVOLVING WEAPONS-COLD",
      "WEAPONS-H",  "POSSIBLE GUNSHOTS HEARD/INCIDENTS INVOLVING WEAPONS-HOT",
      null,         "UNK TYPE CALL 3RD PARTY CALLER- LAW ENFORCEMENT ONLY-HOT",

      // Medical:
      "ABD-C",      "ABDOMINAL PAINS/PROBLEMS - COLD",
      "ABD-H",      "ABDOMINAL PAINS/PROBLEMS - HOT",
      "ALLERGY-C",  "ALLERGIES,REACTIONS,ENVENOMATIONS-COLD",
      "ALLERGY-H",  "ALLERGIES,REACTIONS,ENVENOMATIONS-HOT",
      "ANIMAL-M-C", "ANIMAL BITES,ATTACKS-EMERGENCY-COLD (EMS & FRSP ONLY)",
      "ANIMAL-M-H", "ANIMAL BITES,ATTACKS-EMERGENCY-HOT (EMS & FRSP ONLY)",
      "ANIMAL-M-L", "ANIMAL BITE",
      "ASSALT-M-C", "FIGHTS/PHYSICAL/SEXUAL ASSAULT W/ INJURIES.-COLD (EMS ONLY)",
      "ASSALT-M-H", "FIGHTS/PHYSICAL/SEXUAL ASSAULT W/ INJURIES.-HOT (EMS ONLY)",
      null,         "FIGHTS/PHYSICAL/SEXUAL ASSAULT W/ INJUR-COLD (FRSP & EMS ONLY))",
      null,         "FIGHTS/PHYSICAL/SEXUAL ASSAULT W/ INJUR-HOT (FRSP & EMS ONLY)",
      null,         "FIGHTS/PHYSICAL/SEXUAL ASSALT W/ INJUR-COLD (FRSP & EMS ONLY)",
      null,         "FIGHTS/PHYSICAL/SEXUAL ASSALT W/ INJUR-HOT (FRSP & EMS ONLY)",
      "BK PAIN-C",  "NON-TRAUMATIC OR NON-RECENT TRAUMA-COLD",
      "BK PAIN-H",  "NON-TRAUMATIC OR NON-RECENT TRAUMA-HOT",
      "BREATH-C",   "BREATHING PROBLEMS-ROUTINE",
      "BREATH-H",   "BREATHING PROBLEMS-EMERGENCY",
      "BURNS-C",    "SCALDS/BURNS ETC.-COLD",
      "BURNS-H",    "SCALDS/BURNS ETC.-HOT",
      "CARBON-C",   "CARBON MONOXIDE/INHALATION/HAZ MAT/CRBN-COLD",
      "CARBON-H",   "CARBON MONOXIDE/INHALATION/HAZ MAT/CRBN-HOT",
      "CARD/ARR-C", "CARDIAC OR RESPIRATORY ARREST-OBVIOUS DEATH-COLD",
      "CARD/ARR-H", "CARDIAC OR RESPIRATORY ARREST-HOT",
      "CARDIAC-C",  "CODE BLUE",
      "CARDIAC-H",  "CODE BLUE",
      "CHEST-C",    "CHEST PAIN NON-TRAUMATIC-ROUTINE",
      "CHEST-H",    "CHEST PAIN NON-TRAUMATIC-EMERGENCY",
      "CHOKING-C",  "CHOKING-ROUTINE",
      "CHOKING-H",  "CHOKING-EMERGENCY",
      "CODE PCI",   "CODE PCI TRANSFER",
      "CONF SPACE", "CONFINED SPACE RESCUE",
      "CONVALESCENT","CONVALESCENT TRANSFER",
      "DIABETIC-C", "DIABETIC PROBLEMS-ROUTINE",
      "DIABETIC-H", "DIABETIC PROBLEMS-EMERGENCY",
      "DISTURB-H",  "DISTURB",
      "ASST FD/EMS","EMS ASSIST FIRE DEPT",
      "EYE-C",      "EYE PROBLEMS/INJURIES-ROUTINE",
      "EYE-H",      "EYE PROBLEMS/INJURIES-EMERGENCY",
      "FALLS-C",    "FALLS-LIFTING ASST. ROUTINE - EMS ONLY",
      "FALLS-H",    "FALLS-EMERGENCY",
      "HEADACHE-C", "HEADACHE-ROUTINE",
      "HEADACHE-H", "HEADACHE-EMERGENCY",
      "HEART PR-C", "HEART PROBLEMS/AICD-ROUTINE",
      "HEART PR-H", "HEART PROBLEMS/AICD-EMERGENCY",
      "HEAT/CLD-C", "HEAT/COLD EXPOSURE-ROUTINE",
      "HEAT/CLD-H", "HEAT/COLD EXPOSURE-EMERGENCY",
      "HEMORRHA-C", "HEMORRHAGE/LACERATIONS/BLEEDING-COLD",
      "HEMORRHA-H", "HEMORRHAGE/LACERATIONS/BLEEDING-EMERGENCY",
      "LIGHTNING",  "LIGHTNING STRIKES",
      "MEDICAL",    "EMERGENCY MEDICAL DISPATCH",
      "MENTAL-H",   "MENTAL DISORDER-HOT (LAW ENFORCEMENT ONLY)",
      "OB/PREG-C",  "OB/CHILDBIRTH/MISCARR-COLD",
      "OB/PREG-H",  "OB/CHILDBIRTH/MISCARR-HOT",
      "OVERDOSE-C", "OD/POISONING-ROUTINE",
      "OVERDOSE-H", "OD/POISONING-EMERGENCY",
      "PSYC/SUI-C", "MENTAL DISORDER/BEHAVIOR PROBLEMS/SUICIDAL-COLD FRSP&EMS",
      "PSYC/SUI-H", "MENTAL DISORDER/BEHAVIOR PROBLEMS/SUICIDAL-HOT FRSP&EMS",
      "SEIZURES-C", "SEIZURES/CONVULSIONS-ROUTINE",
      "SEIZURES-H", "SEIZURES/CONVULSIONS-EMERGENCY",
      "SERV CALL",  "SERVICE/CITIZEN ASSISTANCE CALLS",
      "SHOTSTAB-C", "SHOOTING,STABBING, OR OTHER PENETRATING TRAUMA-ROUTINE",
      "SHOTSTAB-H", "SHOOTING,STABBING, OR OTHER PENETRATING TRAUMA-EMERGENCY",
      "SICK-C",     "SICK CALL-ROUTINE",
      "SICK-H",     "SICK CALL-EMERGENCY",
      "STROKE-C",   "STROKE/CVA-ROUTINE",
      "STROKE-H",   "STROKE/CVA-EMERGENCY",
      "SUSPWANT-C", "ASSIST LAW",
      "SUSPWANT-H", "ASSIST LAW",
      "TRANSFER-C", "PATIENT TRANSFER ROUTINE/EMERGENCY - SPECIFY IN NARRATIVE",
      "TRANSFER-H", "PATIENT TRANSFER ROUTINE/EMERGENCY - SPECIFY IN NARRATIVET",
      "TRAUMA-C",   "TRAUMATIC INJURIES SPECIFIC/ ROUTINE",
      "TRAUMA-H",   "TRAUMATIC INJURIES SPECIFIC/ EMERG",
      "UNCONSC-C",  "UNCONSCIOUS/FAINTING-ROUTINE",
      "UNCONSC-H",  "UNCONSCIOUS/FAINTING-EMERGENCY",
      "UNK PROB-C", "UNKNOWN PROBLEM/PERSON DOWN -COLD (EMS RELATED)",
      "UNK PROB-H", "UNKNOWN PROBLEM/PERSON DOWN -HOT (EMS RELATED)",

      // Other
      "ADMIN-C",    "DOCUMENTS,LOST/FOUND PROP.,MESSAGES,TRANSPORTS-COLD",
      "ADMIN-H",    "DOCUMENTS,LOST/FOUND PROP.,MESSAGES,TRANSPORTS-HOT"
    }) {
      if (isCode) {
        code = value;
      } else {
        if (code != null) {
          CODE_TABLE.put(code, value);
        }
        CALL_LIST.add(value);
      }
      isCode = !isCode;
    }
  }
}
