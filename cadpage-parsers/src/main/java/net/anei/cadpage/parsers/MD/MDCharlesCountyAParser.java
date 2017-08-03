//Sender: rc.263@c-msg.net
package net.anei.cadpage.parsers.MD;

import java.util.Properties;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.anei.cadpage.parsers.CodeSet;
import net.anei.cadpage.parsers.FieldProgramParser;
import net.anei.cadpage.parsers.MsgInfo.Data;

public class MDCharlesCountyAParser extends FieldProgramParser {
  
  @Override
  public String getFilter() {
    return "@c-msg.net,dispatch@ccso.us,@sms.mdfiretech.com";
  }


  public MDCharlesCountyAParser() {
    super("CHARLES COUNTY", "MD", 
          "( ADDR CALL | CALL ADDR ) ( ID | INFO/N+? UNIT_INFO ID ) ID2/D TIME CH");
    setupMultiWordStreets(MWORD_STREET_LIST);
  }
  
  @Override
  public int getMapFlags() {
    return MAP_FLG_SUPPR_LA;
  }
  
  private static final Pattern TG_ID_TIME_PATTERN = Pattern.compile("(.*?) (?:TG: *(\\S*) +)?(\\b[EF]\\d{9}(?: +\\d+))(?: (\\d\\d:\\d\\d))?");
  private static final Pattern MAP_PATTERN = Pattern.compile("\\b\\d{1,2} ?[A-Z]\\d{1,2}(?:-\\d?[A-Z]\\d{1,2})?\\b");
  private static final Pattern COUNTY_PATTERN = Pattern.compile("[, ]*\\b([A-Z]{2,3}) CO(?:UNTY)?\\b[, ]*");
  private static final Pattern UNIT_PATTERN = Pattern.compile("(?:(?:\\b\\d{1,2}[A-D])\\b,? *)+");
  private static final Pattern DIR_OF_FIX_PATTERN = Pattern.compile("\\b([NSEW]) & O\\b");
  
  @Override
  protected boolean parseMsg(String subject, String body, Data data) {

    // Check for cancel reason
    String cancel = "";
    if (body.startsWith("Cancel Reason:")) {
      int pt = body.indexOf('\n');
      if (pt < 0) return false;
      cancel = body.substring(0,pt).trim();
      body = body.substring(pt+1).trim();
    }
    
    // Kick out any wayward version B or C messages that wander this way
    if (body.contains("\nmdft.us/")) return false;
    if (body.contains("\nDISTRICT:")) return false;
    
    // See if this is the new dash delimited field format
    String[] flds = body.split(" - ");
    if (flds.length >= 4) {
      if (!parseFields(flds, data)) return false;
    }

    // Try old format
    else {
      setFieldList("CALL CITY ST UNIT ADDR APT PLACE CODE MAP INFO CH ID TIME");
      boolean good = false;
      Matcher match = TG_ID_TIME_PATTERN.matcher(body);
      if (match.matches()) {
        good = true;
        body = match.group(1).trim();
        data.strChannel = getOptGroup(match.group(2));
        data.strCallId = match.group(3).replace(' ', '-');
        data.strTime = getOptGroup(match.group(4));
      }
      
      // There is almost always a code pattern (or whatever it really is)
      // marking the end of the address
      match = MAP_PATTERN.matcher(body);
      if (match.find()) {
        good = true;
        data.strMap = match.group();
        data.strSupp = body.substring(match.end()).trim();
        body = body.substring(0,match.start()).trim();
      }
      
      // We have to have at least one of the two optional constructs
      if (!good) return false;
      
      // Life gets easier if we can find a leading call description
      String call = CALL_LIST.getCode(body);
      if (call != null) {
        data.strCall = call;
        body = body.substring(call.length()).trim();
        body = stripFieldStart(body, ",");
      }
      
      // See if there is a mutual aid county following the call
      match = COUNTY_PATTERN.matcher(body);
      if (data.strCall.length() > 0 ? match.lookingAt() : match.find()) {
        if (data.strCall.length() == 0) data.strCall = body.substring(0,match.start());
        parseCounty(match.group(1), data);
        body = body.substring(match.end());
      }
      
      // See if we can find a unit field following the call description
      // This is only found for older calls
      match = UNIT_PATTERN.matcher(body);
      if (data.strCall.length() > 0 ? match.lookingAt() : match.find()) {
        if (data.strCall.length() == 0) data.strCall = stripFieldEnd(body.substring(0,match.start()).trim(), ",");
        data.strUnit = stripFieldEnd(match.group().trim(), ",");
        body = body.substring(match.end());
        body = stripFieldStart(body, ",");
      }
      
      // Anything following a trailing comma is a (probably) place name
      int flags = FLAG_NO_IMPLIED_APT;
      StartType st = StartType.START_ADDR;
      
      if (data.strCall.length() == 0)   {
        st = StartType.START_CALL;
        flags |= FLAG_START_FLD_REQ;
      }
      
      // See what we find in what is left
      parseAddress(st, flags, body, data);
      String left = getLeft();
      if (left.length() > 0) {
        int pt = left.indexOf(',');
        if (pt >= 0) {
          data.strAddress = append(data.strAddress, " ", left.substring(0,pt).trim());
          data.strPlace = left.substring(pt+1).trim();
        }
        else if (left.startsWith("/")) {
          data.strAddress = append(data.strAddress, " & ", left.substring(1).trim());
        } 
        else {
          data.strPlace = left;
        }
      }
      
      data.strAddress = DIR_OF_FIX_PATTERN.matcher(data.strAddress).replaceAll("$1/O");
      
      data.strCall = stripFieldEnd(data.strCall, ",");
      
      // If the place name we found looks like an apt number, move it
      if (data.strPlace.toUpperCase().startsWith("APT ")) {
        data.strApt = data.strPlace.substring(4).trim();
        data.strPlace = "";
      } else if (data.strPlace.startsWith("#")) {
        data.strApt = data.strPlace;
        data.strPlace = "";
      }
    }

    data.strCall = append(cancel, " - ", data.strCall);

    return true;
  }
  
  @Override
  public Field getField(String name) {
    if (name.equals("ADDR")) return new MyAddressField();
    if (name.equals("UNIT_INFO")) return new MyUnitInfoField();
    if (name.equals("ID")) return new IdField("[EF]\\d{9}", true);
    if (name.equals("ID2")) return new IdField("\\d+", true);
    if (name.equals("TIME")) return new TimeField("\\d\\d:\\d\\d", true);
    return super.getField(name);
  }
  
  private static final Pattern APT_PTN = Pattern.compile("\\b(?:APT|RM|ROOM|SUITE|LOT|UNIT)[ #]*(.*)$");
  private class MyAddressField extends AddressField {
    
    @Override
    public boolean checkParse(String field, Data data) {
      
      // This is only called to process the first of two fields, which could be address/call or
      // call address.  The address/call version is obsolete and supported for historical reasons
      // so we won't spend too much time looking for it
      
      // See if it is a predefined call
      // in which case it is most definately not an address
      if (CALL_LIST.getCode(field) != null) return false;

      // If next field matches predefined call description this is
      // an address.  If not, assume that it isn't.
      if (CALL_LIST.getCode(getRelativeField(+1)) == null) return false;

      parse(field, data);
      return true;
    }
    
    @Override
    public void parse(String field, Data data) {
      
      Matcher match = COUNTY_PATTERN.matcher(field);
      if (match.lookingAt()) {
        parseCounty(match.group(1), data);
        field = field.substring(match.end());
      }
      
      int pt = field.indexOf(',');
      if (pt >= 0) {
        String place = field.substring(pt+1).trim();
        field = field.substring(0,pt).trim();
        match = APT_PTN.matcher(place);
        if (match.find()) {
          String apt = match.group(1);
          if (apt == null) apt = place;
          data.strApt = apt;
          place = place.substring(0,match.start()).trim();
        } 
        data.strPlace = place;
      }
      super.parse(field, data);
    }
    
    @Override
    public String getFieldNames() {
      return "CITY ST " + super.getFieldNames() + " PLACE";
    }
  }
  
  private static final Pattern UNIT_PTN = Pattern.compile("[,A-Z0-9]+");
  private class MyUnitInfoField extends InfoField {
    @Override
    public void parse(String field, Data data) {
      if (UNIT_PTN.matcher(field).matches()) {
        data.strUnit = field;
      } else {
        super.parse(field, data);
      }
    }
    
    @Override
    public String getFieldNames() {
      return super.getFieldNames() + " UNIT";
    }
  }
  
  private void parseCounty(String code, Data data) {
    String city = COUNTY_TABLE.getProperty(code);
    if (city != null) {
      int pt = city.indexOf('/');
      if (pt >= 0) {
        data.strState = city.substring(pt+1);
        city = city.substring(0,pt);
      }
      data.strCity = city;
    } else {
      data.strCity = code + " COUNTY";
    }
  }
  
  @Override
  public CodeSet getCallList() {
    return CALL_LIST;
  }
  
  private static final String[] MWORD_STREET_LIST = new String[]{
    "ADAMS WILLETT",
    "ALL FAITH CHURCH",
    "ANIMAL SHELTER",
    "ANN HARBOR",
    "ANNAPOLIS WOODS",
    "BEL AIRE",
    "BEL ALTON NEWTOWN",
    "BEL ALTON-NEWTOWN",
    "BENT OAK",
    "BIRCH MANOR",
    "BROOMS ISLAND",
    "BRYAN POINT",
    "BUDDS CREEK",
    "BUDDS FERRY",
    "BUMPY OAK",
    "BUNKER HILL",
    "BURNING BUSH",
    "BUSINESS CENTER",
    "CAMERON RIDGE",
    "CARRICO MILL",
    "CHARING CROSS",
    "CHARLOTTE HALL",
    "CHIPPING WOOD",
    "COBB ISLAND",
    "CRESCENT RUN",
    "DEER FARM",
    "DEL RAY",
    "DIXIE LINE",
    "DOVE TREE",
    "DUNNINGTON THOMAS",
    "DURHAM CHURCH",
    "EDELEN STATION",
    "FALLEN TIMBER",
    "FLORA CORNER",
    "FRIENDSHIP LANDING",
    "GALLANT GREEN",
    "GLEN ALBIN",
    "GOLDEN BEACH",
    "GOOSE BAY",
    "GOSPEL UNION",
    "GREEN WOODS",
    "HALLOWING POINT",
    "HANCOCK RUN",
    "HARD BARGAIN",
    "HARVEST RIDGE",
    "HIGH TIMBER",
    "HIKER BIKER",
    "HOLLY SPRINGS",
    "INDIAN HEAD",
    "INDIAN HILLS",
    "INDIAN KING",
    "INDUSTRIAL PARK",
    "JENIFER SCHOOL",
    "JW WILLIAMS",
    "KEMPSFORD FIELD",
    "LA PLATA",
    "LANDS END",
    "LAUREL RIDGE",
    "LINDEN FARM",
    "LINDEN GROVE",
    "LIVERPOOL POINT",
    "LONG FIELD",
    "LUCILLE THORNTON",
    "MARBLE ARCH",
    "MARBURY RUN",
    "MARSHALL HALL",
    "MARSHALLS CORNER",
    "MARSTON MOOR",
    "MARYLAND POINT",
    "MASON SPRINGS",
    "MASONS SPRING",
    "MATTAWOMAN BEANTOWN",
    "MATTAWOMAN CREEK",
    "MEADOW OVERLOOK",
    "METROPOLITAN CHURCH",
    "MONTGOMERYS POST",
    "MT VICTORIA",
    "NEALE SOUND",
    "NORTH SOLOMONS ISLAND",
    "OAK FOREST",
    "OAK GLEN",
    "OLDE VILLAGE",
    "PALE MORNING",
    "PALM DESERT",
    "PENNS HILL",
    "PEP RALLY",
    "PINE CONE",
    "PINEY CHURCH",
    "PINEY HILL",
    "PISGAH MARBURY",
    "POMONKEY CREEK",
    "POPES CREEK",
    "POPLAR HILL",
    "PORT TOBACCO",
    "POST OFFICE",
    "POTOMAC RIVER",
    "PRINCE FREDERICK",
    "PRINCE FREDRICK",
    "QUEENS GROVE",
    "RADIO STATION",
    "RED HILL",
    "RIVER WATCH",
    "RIVERS EDGE",
    "ROBINSON FARM",
    "ROCK POINT",
    "ROCK PT",
    "SCOUTS HONOR",
    "SEA GULL BEACH",
    "SEAGULL BEACH",
    "SHADOW PARK",
    "SHORE ACRES",
    "SIMMS LANDING",
    "SMALLWOOD CHURCH",
    "SMALLWOOD PARK",
    "SMOKEHOUSE ROW",
    "SOLOMONS ISLAND",
    "SORREL RIDGE",
    "SPRING GROVE",
    "ST FLORIAN",
    "ST MARTHA",
    "ST MARYS",
    "ST NICHOLAS",
    "ST PATRICK'S",
    "ST PHILLIP'S",
    "ST THOMAS",
    "STUMP NECK",
    "SUMMIT HILL",
    "SWAN POINT",
    "SWEDEN PT",
    "TED BOWLING",
    "THOMPSONS CORNER",
    "THREE NOTCH",
    "TOBACCO VIEW",
    "TRENT HALL",
    "TRINITY CHURCH",
    "TURTLE DOVE",
    "VIKING SON",
    "WALDORF MARKET",
    "WALTER THOMAS",
    "WAVERLY POINT",
    "WEST DARES BEACH",
    "WOOD GLEN",
    "ZEKIAH RUN"
  };

  private static final CodeSet CALL_LIST = new CodeSet(
      "ABDOMINAL PAIN ALS",
      "ABDOMINAL PAIN BLS",
      "AFA COMMERCIAL BUILDING",
      "AFA SF DWELLING",
      "AIRCRAFT(SML)DOWN ON LAND",
      "ALARM CO DETECTOR",
      "ALARM UNKNOWN MEDICAL ALS",
      "ALARM UNKNOWN MEDICAL BLS",
      "ALLERGIC REACTION ALS",
      "ALLERGIC REACTION BLS",
      "ASSAULT ALS",
      "ASSAULT BLS",
      "ASSIST FIRE DEPARTMENT",
      "ASSIST THE AMBULANCE",
      "ASSIST THE POLICE",
      "ASSIST THE POLICE ALS",
      "ASSIST THE POLICE BLS",
      "ASSIST THE PUBLIC",
      "ATTEMPTED SUICIDE ALS",
      "ATTEMPTED SUICIDE BLS",
      "BACK PAIN ALS",
      "BACK PAIN BLS",
      "BOAT AT MARINA OR MARINA FIRE",
      "BOAT DISTRESS NOT TAKING ON WATER",
      "BOAT DISTRESS TAKING ON WATER",
      "BOAT OVERDUE OR FLARE",
      "BOAT PERSON IN WATER",
      "BRUSH FIRE (Class 1,2,3)",
      "BURN ALS",
      "BURN BLS",
      "CHEST PAINS ALS",                                                                                                        
      "CHEST PAINS BLS",
      "CHIMNEY FIRE SF DWELLING",
      "CO POISONING ALS",
      "CO POISONING BLS",
      "COMMERCIAL BUILDING FIRE, STRUCTURE, BLDG",                                                                          
      "DIABETIC ALS",
      "DIABETIC BLS",
      "EXPLOSION NO FIRE, ALS",
      "EXPLOSION NO FIRE, BLS",
      "EXPLOSION WITH FIRE, ALS",
      "EXPLOSION WITH FIRE, BLS",
      "EYE INJURY ALS",
      "EYE INJURY BLS",
      "FALL ALS",                                                                                                               
      "FALL BLS",    
      "FIRE REPORTED OUT",
      "FLAM GAS LEAK ODOR INSIDE MF COMM BLDG",
      "FLAM GAS LEAK ODOR INSIDE SFD",
      "FLOODED CONDITION",
      "FUEL SPILL",
      "GAS LEAK INSIDE SF DWELLING",
      "HAZMAT BOX",
      "HAZMAT COMMERCIAL BLDG FIRE",
      "HAZMAT INVESTIGATION",
      "HAZMAT LOCAL",
      "HAZMAT SERVICE CALL",
      "HEADACHE ALS",
      "HEADACHE BLS",
      "HEART PROBLEMS ALS",
      "HEART PROBLEMS BLS",
      "HEAT COLD EXPOSURE ALS",
      "HEAT COLD EXPOSURE BLS",
      "HEMORRHAGE ALS",
      "HEMORRHAGE BLS",
      "LANDING SITE",
      "LANDING SITE, LZ, MISC",
      "MVC ALS",
      "MVC BLS",
      "MVC EXTRICATION",
      "MVC HAZMAT",
      "MVC LARGE VEHICLE",
      "MVC MASS CASUALTY",
      "MVC MOTORCYCLE ALS",
      "MVC MOTORCYCLE BLS",
      "MVC PEDESTRIAN OR BIKE",
      "MVC ROLLOVER EJECTED",
      "MVC UNDETERMINED",
      "MVC VEHICLE IN WATER",
      "MVC VEHICLE INTO BLDG",
      "NEAR FAINTING ALS",
      "NEAR FAINTING BLS",
      "OUTSIDE FIRE",
      "OVERDOSE ALS",
      "OVERDOSE BLS",
      "PERSON NOT BREATHING ALS",
      "PERSON NOT BREATHING BLS",
      "POISONING ALS",
      "POISONING BLS",
      "PREGNANCY ALS",
      "PREGNANCY BLS",
      "PREGNANCY NO HEMORRHAGE ALS",
      "PREGNANCY NO HEMORRHAGE BLS",
      "PRIORITY 1 UPGRADE ALS ON SCENE",
      "PSYCH EMERGENCY ALS",
      "PSYCH EMERGENCY BLS",
      "PUBLIC EDUCATION FIRE",
      "REKINDLE",
      "RESCUE BUILDING COLLAPSE",
      "RESCUE HIGH ANGLE",
      "RESCUE INACCESSIBLE TERRAIN",
      "RESCUE OCC VEH IN WATER WITH INJURIES",
      "RESCUE PERSON IN WATER OR ICE",
      "RESCUE SUBJECT TRAPPED",
      "SEARCH LOST PERSON",
      "SEIZURES ALS",
      "SEIZURES BLS",
      "SEIZURES INEFFECTIVE BREATHING",
      "SERVICE CALL FD",
      "SHOOTING ALS",
      "SHOOTING BLS",
      "SICK PERSON ALS",
      "SICK PERSON BLS",
      "SIT FND CALL CANCELLED NO UNIT ENROUTE",
      "SIT FND CALL CANCELLED UNIT ENROUTE",
      "SIT FND FALSE ALARM GOOD INTENT",
      "SPECIAL PLANNED EVENT",
      "STABBING ALS",
      "STABBING BLS",
      "STANDBY ALS",
      "STANDBY BLS",
      "STANDBY FIRE",
      "STROKE ALS",
      "STROKE BLS",
      "STRUC APPLIANCE MALF SF DWELLING",
      "STRUC FIRE COMMERCIAL BLDG",
      "STRUC FIRE DETACHED SHED GARAGE",
      "STRUC FIRE BARN",
      "STRUC FIRE HIGH LIFE HAZARD",
      "STRUC FIRE MF DWELLING",
      "STRUC FIRE PERSON TRAPPED MF COMM",
      "STRUC FIRE SF DWELLING",
      "STRUC ODOR OF SMOKE MF COMM BLDG",
      "STRUC ODOR OF SMOKE SF DWELLING",
      "STRUC OUTLET SMOKING MF COMM BLDG",
      "STRUC OUTLET SMOKING SF DWELLING",
      "STRUC, ODOR OF SMOKE - NO FIRE, M/F DWELLING, TOWNHOUSE, APARTMENT, COMMERCIAL BLDG, MULTI FAMILY HOUSE, BUILDING",
      "TRANSFER (2) ENGINES",
      "TRANSFER ALS",
      "TRANSFER BLS",
      "TRANSFER FIRE",
      "TRANSFORMER FIRE",
      "TRAUMATIC INJURY ALS",
      "TRAUMATIC INJURY BLS",
      "TREE DOWN",
      "TREE DOWN, MISC",
      "TREE ON A HOUSE NO COLLAPSE",
      "TROUBLE BREATHING ALS",
      "TROUBLE BREATHING BLS",
      "TROUBLE BREATHING ALS INEFFECT BREATHING",
      "UNAUTHORIZED UNATTENDED FIRE",
      "UNCONSCIOUS ALS",
      "UNCONSCIOUS BLS",
      "UNCONSCIOUS INEFFECTIVE BREATHING",
      "UNDETERMINED FIRE",
      "UNKNOWN MEDICAL ALS",
      "UNKNOWN MEDICAL BLS",
      "UNKNOWN MEDICAL EMERGENCY ALS",
      "UNKNOWN MEDICAL EMERGENCY BLS",
      "VEHICLE FIRE",
      "VEHICLE FIRE LARGE",
      "WASH DOWN",
      "WIRES DOWN/ARCING",
      "WF WORKING FIRE UPGRADE",
      "WORKING FIRE ALERT",
      "WORKING INCIDENT ALERT",
      
      // Old stuff
      "ASSIST THE AMBULANCE, MISC"
      
  );
  
  private static Properties COUNTY_TABLE = buildCodeTable(new String[]{
      "CAL", "CALVERT COUNTY",
      "KG",  "KING GEORGE COUNTY/VA",
      "PG",  "PRINCE GEORGES COUNTY",
      "SM",  "ST MARYS COUNTY"
  }); 
}