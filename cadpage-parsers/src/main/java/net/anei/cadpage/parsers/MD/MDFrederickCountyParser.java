package net.anei.cadpage.parsers.MD;

import java.util.Properties;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.anei.cadpage.parsers.CodeSet;
import net.anei.cadpage.parsers.FieldProgramParser;
import net.anei.cadpage.parsers.MsgInfo.Data;

/**
 * Frederick County, MD
 */
public class MDFrederickCountyParser extends FieldProgramParser {

  public MDFrederickCountyParser(){
    super(CITY_CODES, "FREDERICK COUNTY", "MD",
          "CT:ADDR! TIME:TIME? ESZ:BOX? MAP:MAP Disp:UNIT");
    setupCallList(CALL_LIST);
    setupMultiWordStreets(MWORD_STREETS);
  }

  @Override
  public String getFilter() {
    return "www.codemessaging.net,CAD@psb.net,@c-msg.net,messaging@iamresponding.com,89361";
  }
  
  private static final Pattern SUBJECT_PTN = Pattern.compile("911@FredCoMD - (\\d\\d-\\d\\d-\\d{4}) *(\\d\\d:\\d\\d:\\d\\d)");
  @Override
  protected boolean parseMsg(String subject, String body, Data data) {
    
    if (subject.length() > 0) {
      String[] subjects = subject.split("\\|");
      if (subjects.length > 2) return false;
      if (subjects.length == 2) {
        if (subjects[0].equals("CAD")) subject = subjects[1];
        else if (subjects[1].equals("CAD")) subject = subjects[0];
        else return false;
      }
      Matcher match = SUBJECT_PTN.matcher(subject);
      if (match.matches()) {
        data.strDate = match.group(1).replace('-', '/');
        data.strTime = match.group(2);
      }
    }

    body = body.replace("MAP:", " MAP:");
    if (!super.parseMsg(body, data)) return false;
    
    // Parser is getting pretty sloppy, anything starting with CT: will pass
    // this.  Let's make sure they have at least one additional field
    return data.strTime.length() > 0 ||
            data.strBox.length() > 0 ||
            data.strMap.length() > 0 ||
            data.strUnit.length() > 0;
  }
  
  @Override
  public String getProgram() {
    return "DATE TIME " + super.getProgram();
  }
  
  @Override
  public Field getField(String name) {
    if (name.equals("ADDR")) return new MyAddressField();
    if (name.equals("UNIT")) return new MyUnitField();
    return super.getField(name);
  }
  
  // Address field gets complicated
  private static final Pattern MUT_AID_PTN = Pattern.compile("(.*?):? @MA (.*?)(?:(?:[:\\*;/]+) *(.*?)(?:[:@](.*))?)?");
  private static final Pattern PLACE_MARK_PTN = Pattern.compile(": ?(?:@|AT\\b)?");
  private static final Pattern PLACE_MARK_PTN2 = Pattern.compile("[:\\*;]");
  private static final Pattern TRAIL_APT_PTN = Pattern.compile("(.*)[;,] *([-/A-Z0-9a-z]+)");
  private class MyAddressField extends AddressField {
    
    @Override
    public void parse(String field, Data data) {
      
      // Eliminate multiple blanks
      field = field.replaceAll("  +", " ");

      // Everything changes with a mutual aid call
      // should be followed by a city: address
      Matcher match = MUT_AID_PTN.matcher(field);
      if (match.matches()) {
        String call = match.group(1).trim();
        call = stripCity(call, data);
        call = stripFieldEnd(call, "/ default");
        data.strCall = "Mutual Aid: " + call;
        data.strCity = convertCodes(match.group(2).trim(), CITY_CODES);
        String addr = getOptGroup(match.group(3));
        data.strPlace = getOptGroup(match.group(4));
        addr = stripCity(addr, data);
        parseAddress(addr, data);
      }
  
      else {
        
        // Not mutual aid.
        // See if there is a / default marker.  If there is it marks the end of the call description
        StartType st = StartType.START_CALL;
        int pt = field.indexOf("/ default");
        if (pt >= 0) {
          st = StartType.START_ADDR;
          data.strCall = field.substring(0,pt).trim();
          field = field.substring(pt+9).trim();
        }
        
        // Look for a trailing place name
        pt = field.indexOf("LL(");
        if (pt >= 0) {
          pt = field.indexOf(')', pt+3);
        }
        if (pt < 0) pt = 0;
        match = PLACE_MARK_PTN.matcher(field);
        if (match.find(pt)) {
          data.strPlace = field.substring(match.end()).trim();
          field = field.substring(0,match.start()).trim();
        }
        
        while (true) {
          match = TRAIL_APT_PTN.matcher(field);
          if (!match.matches()) break;
          field = match.group(1).trim();
          String apt = match.group(2);
          if (!data.strApt.startsWith(apt)) {
            data.strApt = append(apt, "-", data.strApt);
          }
        }
        
        // Strip off possibly multiple city codes
        field = stripCity(field, data);
        
        int flags = FLAG_NO_CITY;
        if (st == StartType.START_CALL) flags |= FLAG_START_FLD_REQ;
        if (data.strPlace.length() > 0) flags |= FLAG_ANCHOR_END;
        if (data.strApt.length() > 0) flags |= FLAG_NO_IMPLIED_APT;
        parseAddress(st, flags, field, data);
        if (data.strPlace.length() == 0) data.strPlace = getLeft();
      }
      
      // If no address (it happens) substitute the place name
      if (data.strAddress.length() == 0) {
        String addr = data.strPlace;
        data.strPlace = "";
        match = PLACE_MARK_PTN2.matcher(addr);
        if (match.find()) {
          data.strPlace = addr.substring(match.end()).trim();
          addr = addr.substring(0,match.start()).trim();
        }
        addr = stripCity(addr, data);
        parseAddress(addr, data);
      }
      
      // check for combined address and place name
      int pt = data.strAddress.indexOf(" - ");
      if (pt >= 0) {
        String sPart1 = data.strAddress.substring(0,pt).trim();
        String sPart2 = data.strAddress.substring(pt+3).trim();
        int chk1 = checkAddress(sPart1);
        int chk2 = checkAddress(sPart2);
        if (chk1 > chk2) {
          String tmp = sPart1;
          sPart1 = sPart2;
          sPart2 = tmp;
          chk2 = chk1;
        }
        if (chk2 > STATUS_MARGINAL) {
          data.strPlace = append(sPart1, " - ", data.strPlace);
          data.strAddress = sPart2;
        }
      }
      
      // See of this an out of state city
      pt = data.strCity.indexOf(',');
      if (pt >= 0) {
        data.strState = data.strCity.substring(pt+1);
        data.strCity = data.strCity.substring(0,pt);
      }
    }
    
    private String stripCity(String field, Data data) {
      while (true) {
        Result res = parseAddress(StartType.START_OTHER, FLAG_ONLY_CITY | FLAG_ANCHOR_END, field);
        if (res.getCity().length() == 0) break;
        res.getData(data);
        field = res.getStart();
      }
      return field;
    }
    
    @Override
    public String getFieldNames() {
      return "CALL ADDR APT CITY ST PLACE";
    }
  }
  
  private class MyUnitField extends UnitField {
    @Override
    public void parse(String field, Data data) {
      if (field.contains("[") && field.contains("]")) {
        field = field.substring(0,field.indexOf("[")).trim();
      }
      super.parse(field, data);
    }
  }
  
  private static final String[] MWORD_STREETS = new String[]{
    "ADAMSTOWN COMMONS",
    "ADDISON WOODS",
    "AIR GARDEN",
    "ALL SAINTS",
    "BAKER VALLEY",
    "BALLENGER CREEK",
    "BALTIMORE NATIONAL",
    "BAUST CHURCH",
    "BEL AIRE",
    "BETHESDA CHURCH",
    "BIG WOODS",
    "BIGGS FORD",
    "BILL MOXLEY",
    "BLACK HAW",
    "BLUE HERON",
    "BLUE MOUNTAIN",
    "BLUE RIDGE",
    "BLUE SPRUCE",
    "BLUE STONE",
    "BOWMANS FARM",
    "BOYERS MILL",
    "BRETHREN CHURCH",
    "BRIDGE SPRING",
    "BURGESS HILL",
    "CALEB WOOD",
    "CANADA HILL",
    "CAP STINE",
    "CARRIAGE HILL",
    "CARROLL BOYER",
    "CARROLL CREEK",
    "CASTLE ROCK",
    "CATOCTIN CREEK",
    "CATOCTIN FURNACE",
    "CATOCTIN HOLLOW",
    "CATOCTIN MOUNTAIN",
    "CATOCTIN RIDGE",
    "CHURCH HILL",
    "CLYDE YOUNG",
    "COLD BROOK",
    "COLD SPRINGS",
    "CONE BRANCH",
    "COOK BROTHERS",
    "COUNTRY RUN",
    "COWMANS MANOR",
    "CREEK WALK",
    "CROW ROCK",
    "CUNNINGHAM FALLS PARK",
    "DANCE HALL",
    "DEER SPRING",
    "DEVILBISS BRIDGE",
    "DOCTOR PERRY",
    "DRY BRIDGE",
    "DULANEY MILL",
    "DWIGHT D EISENHOWER",
    "ED MCCLAIN",
    "EDGEWOOD FARM",
    "EYLERS VALLEY FLINT",
    "EYLERS VALLEY",
    "FIELD POINTE",
    "FISHERS HOLLOW",
    "FISHING CREEK",
    "FLINT HILL",
    "FOX ROCK",
    "FRANCIS SCOTT KEY",
    "FRIENDS CREEK",
    "GAMBRILL PARK",
    "GATEWAY CENTER",
    "GENE HEMP",
    "GLADHILL BROTHERS",
    "GLEN VALLEY",
    "GOOD INTENT",
    "GRAVEL HILL",
    "GRAY FOX",
    "GREEN VALLEY",
    "GUM SPRING",
    "HARPERS FERRY",
    "HEATHER RIDGE",
    "HESSONG BRIDGE",
    "HIGH BEACH",
    "HOPE COMMONS",
    "HOWARD STUP",
    "INDIAN CEDAR",
    "INDIAN SPRINGS",
    "JACOB BRUNER",
    "JAMES MONROE",
    "JAY BIRD",
    "JESSE SMITH",
    "JIM PHELAN",
    "KEEP TRYST",
    "KEMPTOWN CHURCH",
    "KEYSVILLE BRUCEVILL",
    "KEYSVILLE BRUCEVILLE",
    "LAUREL WOOD",
    "LEE HILL",
    "LEGORE BRIDGE",
    "LILY PONS",
    "LINGANORE WOODS",
    "LINKS BRIDGE",
    "LITTLE BROOK",
    "LOCH NESS",
    "LONG CORNER",
    "LONGS MILL",
    "LOY WOLFE",
    "LYNN BURKE",
    "LYNN CREST",
    "MAE WADE",
    "MAPLE TERRACE",
    "MARKET SPACE",
    "MEETING HOUSE",
    "MICHAELS MILL",
    "MIDDLE POINT",
    "MONOCACY RIVER",
    "MOON MAIDEN",
    "MOTTERS STATION",
    "MOUNT PHILLIP",
    "MOUNT ZION",
    "MOUNTAIN CHURCH",
    "MOUNTAIN VIEW",
    "MOUTH OF MONACACY",
    "NAYLORS MILL",
    "NICODEMUS MILL",
    "NORTH POINTE",
    "OAK HILL",
    "PARK CENTRAL",
    "PARK MILLS",
    "PEACH ORCHARD",
    "PENN SHOP",
    "PENTERRA MANOR",
    "PICNIC WOODS",
    "PLEASANT VALLEY",
    "PLEASANT VIEW",
    "PLEASANT WALK",
    "POINT OF ROCKS",
    "POOLE JONES",
    "PRICES DISTILLERY",
    "QUEBEC SCHOOL",
    "QUINN ORCHARD",
    "RAMP MOTTER",
    "REELS MILL",
    "REICHS FORD",
    "RIVER OAKS",
    "RIVER RUN",
    "ROCK CREEK",
    "ROCKY FOUNTAIN",
    "ROCKY RIDGE",
    "ROCKY SPRINGS",
    "SAINT CLAIRE",
    "SAINT SIMON",
    "SANDRA LEE",
    "SEA GULL",
    "SIXES BRIDGE",
    "SMALL GAINS",
    "SPRING FOREST",
    "SPRING HOLLOW",
    "SPRING RIDGE",
    "STONERS FORD",
    "SUGAR MAPLE",
    "SWIMMING POOL",
    "TALL OAKS",
    "TAYLORS VALLEY",
    "TEEN BARNES",
    "THOMAS JOHNSON",
    "TOLL HOUSE",
    "TULIP TREE",
    "UTICA MILLS",
    "VALLEY VIEW",
    "VILLAGE GATE",
    "VILLAGE OAKS",
    "WAGON SHED",
    "WALKERS VILLAGE",
    "WASH HOUSE",
    "WATER STREET",
    "WATERS EDGE",
    "WEST OAK",
    "WHITE OAK",
    "WHITE ROCK",
    "WILLOW GLEN",
    "WOOD HOLLOW",
    "WORMANS MILL",
    "YELLOW SPRINGS",
    "YOUNG FAMILY"
  };
  
  private static final CodeSet CALL_LIST = new CodeSet(
      "ABDOMINAL PAIN",
      "ABDOMINAL PAIN (ALS)",
      "ADDITIONAL ALARM / FIRE / -RPD_INTER_DISP",
      "ADDITIONAL ALARM / FIRE / 2ND_ALRM_RURAL",
      "ADDITIONAL ALARM / TASK FORCE / EMS_TASK_FORCE",
      "ADDITIONAL ALARM / TASK FORCE / TNKR_TASK_FORCE",
      "AIRCRAFT EMERGENCY / ALERT-2",
      "ALLERGIC REACTION",
      "AMBULANCE TRANSFER",
      "ASSIST PATIENT - NON-EMERGENCY RESPONSE",
      "ASTHMA ATTACK",
      "ATV ACCIDENT",
      "AUTOMATIC MEDICAL ALARM",
      "BACK PAIN",
      "BACK PAIN /",
      "BICYCLIST STRUCK",
      "BRUSH FIRE",
      "BRUSH TRUCK TRANSFER",
      "BUILDING COLLAPSE - RESCUE PRE ALERT",
      "BURN INJURY - NOTIFY FIRE MARSHAL",
      "BUS ACCIDENT - RESCUE PRE ALERT",
      "CARDIAC ARREST",
      "CARDIAC PATIENT",
      "CARDIAC PATIENT BLS",
      "CHIMNEY FIRE (NO EXTENSION) (STRUCTURE PRE-ALERT)",
      "CHEST PAIN",
      "CHEST PAIN BLS",
      "CHIMNEY FIRE (NO EXTENSION)",
      "CHOKING",
      "CO DETECTOR ACTIVATION - NON-EMERGENCY RESPONSE",
      "COMMERCIAL FIRE ALARM / AUTOMATIC",
      "COMMERCIAL FIRE ALARM / WATERFLOW",
      "COMMERCIAL STRUCTURE / FIRE-VISIBLE",
      "COMMERCIAL STRUCTURE / ODOR",
      "COMMERCIAL STRUCTURE / SMOKE",
      "DECREASED LEVEL OF CONSCIOUSNESS",
      "DIABETIC",
      "DIABETIC BLS",
      "ENGINE TRANSFER",
      "EXPIRED PERSON",
      "FARM MACHINERY FIRE",
      "FIRE POLICE EVENT / TRAFFIC",
      "FIRE WITH PERSONS TRAPPED / HOUSE",
      "FLOODING CONDITION",
      "FUEL SPILL",
      "HAZMAT INCIDENT (SPECIFY)",
      "HEAT EXPOSURE BLS",
      "HEAT RELATED EMERGENCY",
      "HELICOPTER LANDING ZONE - NON-EMERGENCY RESPONSE",
      "HEMORRHAGE",
      "HEMORRHAGE BLS",
      "HIGH ANGLE RESCUE",
      "HIGH LIFE HAZARD / HOTEL-FIRE",
      "HIGH LIFE HAZARD / HOTEL-ODOR",
      "HIGH LIFE HAZARD / HOTEL-SMOKE",
      "HIGH RISE >3 STORIES / HIGH-RISE-SMOKE",
      "HOUSE / APPLIANCE FIRE",
      "HOUSE / APPLIANCE FIRE (STRUCTURE PRE-ALERT)",
      "HOUSE / FIRE-VISIBLE",
      "HOUSE / ODOR",
      "HOUSE / SMOKE",
      "ILLEGAL OPEN BURNING - NON-EMERGENCY RESPONSE",
      "INJURED PERSON",
      "INJURED PERSON /",
      "INJURED PERSON (SPECIFY NATURE)",
      "INJURED PERSON FROM ASSAULT",
      "INJURY FROM VEHICLE ACCIDENT",
      "INSIDE GAS LEAK",
      "INSIDE GAS LEAK (STRUCTURE PRE-ALERT)",
      "LARGE NON-DWELLING FIRE / BARN",
      "LOCKOUT - SPECIFY RESPONSE",
      "MENTAL PATIENT - NON-EMERGENCY RESPONSE",
      "MENTAL PATIENT ALS",
      "MOBILE HOME / TRAILER / OFFICE / MOBILE/HOME-FIRE",
      "MOTORCYCLE ACCIDENT",
      "MOUNTAIN RESCUE - NOTIFY PARK SERVICE",
      "MULTI-FAMILY STRUCTURE / FIRE-VISIBLE",
      "MULTI-FAMILY STRUCTURE / ODOR",
      "MULTI-FAMILY STRUCTURE / SMOKE",
      "OBSTETRIC PATIENT",
      "ODOR UNKNOWN INSIDE",
      "OUTSIDE INVESTIGATION",
      "OUTSIDE LINE/TANK >=3D5 GALLONS",
      "OUTSIDE LINE/TANK >=5 GALLONS",
      "OUTSIDE ODOR (NATURAL/LP)",
      "OVERDOSE",
      "OVERDOSE BLS",
      "PEDESTRIAN STRUCK",
      "PERSON FIRE (INSIDE)",
      "POISONING",
      "RAPID WATER RESCUE / POTOMAC_RIVER",
      "RAPID WATER RESCUE / STREAMS_OTHER",
      "REKINDLE",
      "RESIDENTIAL FIRE ALARM",
      "SEIZURE",
      "SERVICE CALL - SPECIFY",
      "SHOOTING",
      "SICK PERSON",
      "SMALL NON-DWELLING STRUCTURE FIRE / GARAGE",
      "SPECIFY NATURE",
      "STABBING",
      "STILL WATER RESCUE BLS",
      "STROKE",
      "STRUCTURE FIRE OUT / EXTINGUISHED",
      "STUCK ELEVATOR",
      "SUBJECT LOCKED IN A VEHICLE (Specify Emergency/Non-Emergency)",
      "SUSTAINED SEIZURE",
      "SYNCOPAL EPISODE",
      "TANKER TRANSFER",
      "TEST - DO NOT DISPATCH",
      "TROUBLE BREATHING",
      "TRUCK FIRE (Specify)",
      "TRUCK TRANSFER",
      "UNCONSCIOUS DIABETIC",
      "UNCONSCIOUS PERSON",
      "UNCONSCIOUS OVERDOSE",
      "UNKNOWN MEDICAL EMERGENCY",
      "VEHICLE ACCIDENT",
      "VEHICLE ACCIDENT WITH ENTRAPMENT",
      "VEHICLE ACCIDENT WITH HAZMAT (SPECIFY)",
      "VEHICLE ACCIDENT (ALS SPECIFY)",
      "VEHICLE FIRE",
      "VEHICLE FIRE (Specify)",
      "VIOLENT MENTAL PATIENT - NON EMERGENCY RESPONSE",
      "WIRES DOWN",
      "WOODS FIRE"
  );
  
  private static final Properties CITY_CODES = 
    buildCodeTable(new String[]{
        "ADAM",   "Adams County,PA",
        "ADAMCO", "Adams County,PA",
        "ADAM CO","Adams County,PA",
        "ADCO",   "Adams County,PA",
        "BRAD",   "Braddock Heights",
        "BRUN",   "Brunswick",
        "CACO",   "Carroll County",
        "CADA",   "Adamstown",
        "CARRCO", "Carroll County",
        "CBRH",   "Braddock Heights",
        "CCLK",   "Clarksburg",
        "CDIC",   "Dickerson",
        "CEMB",   "Emmitsburg",
        "CFR1",   "Frederick City",
        "CFR2",   "Frederick City",
        "CFR3",   "Frederick City",
        "CFR4",   "Frederick City",
        "CIJM",   "New Market",
        "CJEF",   "Jefferson",
        "CKEY",   "Keymar",
        "CKNO",   "Knoxville",
        "CMON",   "Monrovia",
        "CMID",   "Middletown",
        "CNMA",   "New Market",
        "CMTY",   "Mt Airy",
        "CMYE",   "Myersville",
        "CPOR",   "Point of Rocks",
        "CRKY",   "Rocky Ridge",
        "CSAB",   "Sabillasville",
        "CSMT",   "Smithsburg",
        "CTHU",   "Thurmont",
        "CUBR",   "Union Bridge",
        "CWAL",   "Walkersville",
        "CWOD",   "Woodsboro",
        "EMMB",   "Emmitsburg",
        "FORT",   "Fort Detrick",
        "FRAN",   "Franklin County,PA",
        "FRAN CO","Franklin County,PA",
        "FRANCO", "Franklin County,PA",
        "FRCO",   "Franklin County,PA",
        "FRE1",   "Frederick City",
        "FRE2",   "Frederick City",
        "FRE3",   "Frederick City",
        "FRE4",   "Frederick City",
        "HOCO",   "Howard County",
        "HOWACO", "Howard County",
        "JEFF",   "Jefferson County,WV",
        "JEFFCO", "Jefferson County,WV",
        "JEFF CO","Jefferson County,WV",
        "LOCO",   "Loudoun County,VA",
        "LOUD",   "Loudoun County,VA",
        "LOUDCO", "Loudoun County,VA",
        "LOUD CO","Loudoun County,VA",
        "MIDD",   "Middletown",
        "MOCO",   "Montgomery County",
        "MONTCO", "Montgomery County",
        "MTAY",   "Mt Airy",
        "MYER",   "Myersville",
        "NEWM",   "New Market",
        "ROSE",   "Rosemont",
        "THUR",   "Thurmont",
        "WACO",   "Washington County",
        "WALK",   "Walkersville",
        "WASH",   "Washington County", 
        "WASHCO", "Washington County", 
        "WASH CO","Washington County", 
        "WOOD",   "Woodsboro",
        
        "ADAMS COUNTY",        "Adams County,PA",
        "ADAMSTOWN",           "Adamstown",
        "BALLENGER CREEK",     "Ballenger Creek",
        "BARTONSVILLE",        "Bartonsville",
        "BRADDOCK HEIGHTS",    "Braddock Heights",
        "BRUNSWICK",           "Brunswick",
        "BUCKEYSTOWN",         "Buckeystown",
        "BURKITTSVILLE",       "Burkittsville",
        "CARROLL COUNTY",      "Carroll County",
        "CLARKSBURG",          "Clarksburg",
        "CLOVER HILL",         "Clover Hill",
        "CREAGERSTOWN",        "Creagerstown",
        "DICKERSON",           "Dickerson",
        "DISCOVERY",           "Discovery",
        "DISCOVERY-SPRING GARDEN", "Discovery-Spring Garden",
        "EMMITSBURG",          "Emmitsburg",
        "FORT DETRICK",        "Fort Detrick",
        "FOXVILLE",            "Foxville",
        "FRANKLIN COUNTY",     "Franklin County,PA",
        "FREDERICK",           "Frederick City",
        "GRACEHAM",            "Graceham",
        "GREEN VALLEY",        "Green Valley",
        "HOWARD COUNTY",       "Howard County",
        "IJAMSVILLE",          "Ijamsville",
        "JEFFERSON",           "Jefferson",
        "JEFFERSON COUNTY",    "Jeferson County",
        "JOHNSVILLE",          "Johnsville",
        "KEMPTOWN",            "Kemptown",
        "KEYMAR",              "Keymar",
        "KNOXVILLE",           "Knoxville",
        "LADIESBURG",          "Ladiesburg",
        "LAKE LINGANORE",      "Lake Linganore",
        "LEWISTOWN",           "Lewistown",
        "LIBERTYTOWN",         "Libertytown",
        "LINGANORE",           "Linganore",
        "LINGANORE-BARTONSVILLE", "Linganore-Bartonsville",
        "LOUDOUN COUNTY",      "Loudoun County,VA",
        "LOVETTSVILLE",        "Lovettsville,VA",
        "MIDDLETOWN",          "Middletown",
        "MONROVIA",            "Monrovia",
        "MONTGOMERY COUNTY",   "Montgomery County",
        "MOUNT AIRY",          "Mt Airy",
        "MT AIRY",             "Mt Airy",
        "MYERSVILLE",          "Myersville",
        "NEW MARKET",          "New Market",
        "NEW MIDWAY",          "New Midway",
        "PETERSVILLE",         "Petersville",
        "POINT OF ROCKS",      "Point of Rocks",
        "ROCKY RIDGE",         "Rocky Ridge",
        "ROSEMONT",            "Rosemont",
        "SABILLASVILLE",       "Sabillasville",
        "SMITHSBURG",          "Smithsburg",
        "SPRING GARDEN",       "Spring Garden",
        "SUNNY SIDE",          "Sunny Side",
        "THURMONT",            "Thurmont",
        "TUSCARORA",           "Tuscarora",
        "UNION BRIDGE",        "Union Bridge",
        "URBANA",              "Urbana",
        "UTICA",               "Utica",
        "WALKERSVILLE",        "Walkersville",
        "WASHINGTON COUNTY",   "Washington County",
        "WOLFSVILLE",          "Wolfsville",
        "WOODSBORO",           "Woodsboro"
  });
}