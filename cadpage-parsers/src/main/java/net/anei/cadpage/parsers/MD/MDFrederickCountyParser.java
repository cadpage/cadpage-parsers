package net.anei.cadpage.parsers.MD;

import java.util.Properties;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.anei.cadpage.parsers.CodeSet;
import net.anei.cadpage.parsers.FieldProgramParser;
import net.anei.cadpage.parsers.MsgInfo.Data;
import net.anei.cadpage.parsers.SplitMsgOptions;
import net.anei.cadpage.parsers.SplitMsgOptionsCustom;

/**
 * Frederick County, MD
 */
public class MDFrederickCountyParser extends FieldProgramParser {

  public MDFrederickCountyParser(){
    super(CITY_CODES, "FREDERICK COUNTY", "MD",
          "CT:ADDR! TIME:TIME? ESZ:BOX? MAP:MAP TG:CH Disp:UNIT");
    setupCallList(CALL_LIST);
    setupMultiWordStreets(MWORD_STREETS);
  }

  @Override
  public String getFilter() {
    return "www.codemessaging.net,CAD@psb.net,@c-msg.net,messaging@iamresponding.com,IntergraphNotificati@frederickcountymd.gov,89361";
  }
  
  @Override
  public SplitMsgOptions getActive911SplitMsgOptions() {
    return new SplitMsgOptionsCustom(){
      @Override public boolean splitBlankIns() { return false; }
      @Override public boolean mixedMsgOrder() { return true; }
      @Override public int splitBreakLength() { return 240; }
    };
  }

  private static final Pattern SUBJECT_PTN = Pattern.compile("911@FredCoMD - (\\d\\d-\\d\\d-\\d{4}) *(\\d\\d:\\d\\d:\\d\\d)");
  @Override
  protected boolean parseMsg(String subject, String body, Data data) {

    if (subject.length() == 0 && body.startsWith("911@FredCoMD - ")) {
      int pt = body.indexOf('\n');
      if (pt < 0) return false;
      subject = body.substring(0,pt).trim();
      body = body.substring(pt+1).trim();
    }
    
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
    
    int pt = body.lastIndexOf("... http");
    if (pt >= 0) {
      data.strInfoURL = body.substring(pt+4);
      body = body.substring(0,pt).trim();
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
    return "DATE TIME " + super.getProgram() + " URL";
  }
  
  @Override
  public Field getField(String name) {
    if (name.equals("ADDR")) return new MyAddressField();
    if (name.equals("UNIT")) return new MyUnitField();
    return super.getField(name);
  }
  
  // Address field gets complicated
  private static final Pattern MUT_AID_PTN = Pattern.compile("(.*?):? @MA[- ]([A-Z]+(?: (?:CO|CNTY))?)[ :\\*;/@]*(.*?)(?:[:@](.*))?");
  private static final Pattern UNIT_PREFIX_PTN = Pattern.compile("(ENGINE \\S+) +(.*)");
  private static final Pattern NOT_TRAIL_PLACE_PTN = Pattern.compile(".*MM|.*\\bMILE MARKER|OFF .*", Pattern.CASE_INSENSITIVE);
  private static final Pattern ADDR_APT_PTN = Pattern.compile("(.*?)[, ]+APT *(\\S*)");
  private static final Pattern PLACE_MARK_PTN2 = Pattern.compile("[\\*;]");
  private static final Pattern APT_PTN = Pattern.compile("(?:RM|LOT|ROOM|APT|UNIT)[- ]*(.*)|\\d{1,4}[A-Z]?|[A-Z]|IAO", Pattern.CASE_INSENSITIVE);
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
        int pt = call.indexOf("/ default");
        if (pt >= 0) call = call.substring(0, pt).trim();
        data.strCall = "Mutual Aid: " + call;
        data.strCity = convertCodes(match.group(2).trim(), CITY_CODES);
        String addr = getOptGroup(match.group(3));
        String place = match.group(4);
        
        match = UNIT_PREFIX_PTN.matcher(addr);
        if (match.matches()) {
          data.strCall = append(data.strCall, " - ", match.group(1));
          addr = match.group(2);
        }
        
        if (place != null) {
          addr = stripCity(addr, data);
          parseAddress(addr, data);
          place = place.trim();
          match = APT_PTN.matcher(place);
          if (match.matches()) {
            String apt = match.group(1);
            if (apt == null) apt = place;
            data.strApt = append(data.strApt, "-", apt);
          } else {
            data.strPlace = place;
          }
        } else {
          parseAddress(StartType.START_ADDR, FLAG_NO_IMPLIED_APT, addr, data);
          place = getLeft();
          if (place.startsWith("&") || place.startsWith("/")) {
            data.strAddress = append (data.strAddress, " & ", place.substring(1).trim());
          } else if (NOT_TRAIL_PLACE_PTN.matcher(place).matches()) {
            data.strAddress = append(data.strAddress, " ", place);
          } else {
            data.strPlace = place;
          }
        }
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
        
        String extra = null;
        pt = field.indexOf(':', pt);
        if (pt >= 0) {
          extra = field.substring(pt+1).trim();
          field = field.substring(0,pt).trim();
        }
        
        match = ADDR_APT_PTN.matcher(field);
        if (match.matches()) {
          field = match.group(1);
          data.strApt = match.group(2);
        }
        
        // Strip off possibly multiple city codes
        field = stripCity(field, data);
        
        int flags = FLAG_NO_CITY;
        if (st == StartType.START_CALL) flags |= FLAG_START_FLD_REQ;
        if (data.strPlace.length() > 0) flags |= FLAG_ANCHOR_END;
        if (data.strApt.length() > 0) flags |= FLAG_NO_IMPLIED_APT;
        parseAddress(st, flags, field, data);
        if (data.strPlace.length() == 0) data.strPlace = getLeft();
        
        if (extra != null) {
          for (String part : extra.split(":")) {
            part = part.trim();
            if (part.length() == 0) continue;
            if (data.strAddress.length() == 0) {
              parseAddress(stripFieldStart(part, "@"), data);
            }
            else if (part.startsWith("@")) {
              part = part.substring(1).trim();
              data.strPlace = append(data.strPlace, " - ", part);
            } else {
              match = APT_PTN.matcher(part);
              if (match.matches()) {
                String apt = match.group(1);
                if (apt == null) apt = part;
                data.strApt = append(data.strApt, "-", apt);
              }
              else if (data.strPlace.length() == 0) {
                data.strPlace = append(data.strPlace, " - ", part);
              } else {
                data.strApt = append(data.strApt, "-", part);
              }
            }
          }
        }
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
      
      Matcher match = PLACE_MARK_PTN2.matcher(field);
      if (match.find()) {
        String part = field.substring(match.end()).trim();
        field = field.substring(0,match.start()).trim();
        match = APT_PTN.matcher(part);
        if (match.matches()) {
          String apt = match.group(1);
          if (apt == null) apt = part;
          data.strApt = append(data.strApt, "-", apt);
        } else {
          data.strPlace = append(data.strPlace, " - ", part);
        }
      }
      
      while (true) {
        Result res = parseAddress(StartType.START_OTHER, FLAG_ONLY_CITY | FLAG_ANCHOR_END, field);
        if (res.getCity().length() == 0) break;
        String saveCity = data.strCity;
        res.getData(data);
        if (saveCity.length() > 0) data.strCity = saveCity;
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
    "ALBERT STAUB",
    "ALL SAINTS",
    "AMBER MEADOW",
    "APPLES CHURCH",
    "ARABY CHURCH",
    "ARBOR SQUARE",
    "ARCTIC TERN",
    "BAKER VALLEY",
    "BALLENGER CENTER",
    "BALLENGER CREEK",
    "BALTIMORE NATIONAL",
    "BARK HILL",
    "BAUST CHURCH",
    "BEALLS FARM",
    "BEAR BRANCH",
    "BEAR DEN",
    "BEL AIRE",
    "BENNIE DUNCAN",
    "BENNIES HILL",
    "BETHESDA CHURCH",
    "BICTON COMMONS",
    "BIG WOODS",
    "BIGGS FORD",
    "BILL DORSEY",
    "BILL MOXLEY",
    "BLACK CREEK",
    "BLACK DUCK",
    "BLACK HAW",
    "BLACKS MILL",
    "BLUE HERON",
    "BLUE MOUNTAIN",
    "BLUE RIDGE",
    "BLUE SPRUCE",
    "BLUE STONE",
    "BOLLINGER SCHOOL",
    "BOONSBORO MTN",
    "BOSS ARNOLD",
    "BOWMANS FARM",
    "BOX ELDER",
    "BOYERS MILL",
    "BRETHREN CHURCH",
    "BRIDGE SPRING",
    "BRIDLE PATH",
    "BROKEN REED",
    "BROOK VALLEY",
    "BULL FROG",
    "BURGESS HILL",
    "BUSH CREEK",
    "CALEB WOOD",
    "CANADA HILL",
    "CANDLE RIDGE",
    "CANVAS BACK",
    "CAP STINE",
    "CARRIAGE HILL",
    "CARROLL BOYER",
    "CARROLL CREEK VIEW",
    "CARROLL CREEK",
    "CASH SMITH",
    "CASTLE ROCK",
    "CATHOLIC CHURCH",
    "CATOCTIN CREEK",
    "CATOCTIN FURNACE",
    "CATOCTIN HIGHLANDS",
    "CATOCTIN HOLLOW",
    "CATOCTIN MOUNTAIN",
    "CATOCTIN RIDGE",
    "CATOCTIN VISTA",
    "CHARTWELL CRESCENT",
    "CHESTNUT GROVE",
    "CHURCH HILL",
    "CLYDE YOUNG",
    "COACH HOUSE",
    "COACH LIGHT",
    "COLD BROOK",
    "COLD SPRINGS",
    "CONE BRANCH",
    "CONRADS FERRY",
    "COOK BROTHERS",
    "COUNTRY RUN",
    "COWMANS MANOR",
    "CREEK WALK",
    "CROW ROCK",
    "CRUMS HOLLOW",
    "CUB HUNT",
    "CUNNINGHAM FALLS PARK",
    "DANCE HALL",
    "DEER SPRING",
    "DEVILBISS BRIDGE",
    "DOCTOR BELT",
    "DOCTOR PERRY",
    "DOWNEY MILL",
    "DROUGHT SPRING",
    "DRY BRIDGE",
    "DULANEY MILL",
    "DUTCHMANS CREEK",
    "DWIGHT D EISENHOWER",
    "DYRHAM PARK",
    "ED MCCLAIN",
    "EDGEWOOD FARM",
    "ELMER DERR",
    "ELMER SCHOOL",
    "ENGLISH MUFFIN",
    "EYLERS VALLEY FLINT",
    "EYLERS VALLEY",
    "FIELD POINTE",
    "FIRE TOWER",
    "FISH HATCHERY",
    "FISHERS HOLLOW",
    "FISHING CREEK",
    "FIVE SHILLINGS",
    "FLINT HILL",
    "FOREST STREAM CLUB",
    "FOUNTAIN SCHOOL",
    "FOUR POINTS BRIDGE",
    "FOUR POINTS",
    "FOX CHASE",
    "FOX ROCK",
    "FRANCIS SCOTT KEY",
    "FREDERICK CROSSING",
    "FRIENDS CREEK",
    "GAMBRILL PARK",
    "GARRETTS MILL",
    "GAS HOUSE",
    "GATEWAY CENTER",
    "GENE HEMP",
    "GEORGE THOMAS",
    "GEORGIA PACIFIC",
    "GLADHILL BROTHERS",
    "GLEN VALLEY",
    "GOLD MINE",
    "GOOD INTENT",
    "GRAVEL HILL",
    "GRAY FOX",
    "GREEN VALLEY",
    "GUM SPRING",
    "HAMPTON VALLEY",
    "HARBAUGH VALLEY",
    "HARP HILL",
    "HARPERS FERRY",
    "HAUGHS CHURCH",
    "HEATHER RIDGE",
    "HELLS DELIGHT",
    "HESSONG BRIDGE",
    "HIGH BEACH",
    "HIGH KNOB",
    "HIGHLAND SCHOOL",
    "HIGHPOINT VIEW",
    "HOLLOW REED",
    "HOPE COMMONS",
    "HOPE MILLS",
    "HOWARD STUP",
    "HUNT CLUB",
    "INDIAN CEDAR",
    "INDIAN SPRINGS",
    "IVY HILL",
    "JACKS MOUNTAIN",
    "JACOB BRUNER",
    "JAMES MONROE",
    "JAY BIRD",
    "JEFFERSON TECHNOLOGY",
    "JENNIFER LYNNE",
    "JESSE SMITH",
    "JIM PHELAN",
    "JIM SMITH",
    "JOE FOSS",
    "JOHN BROWN",
    "JOHN DRAPER",
    "JOHN MILLS",
    "KEEP TRYST",
    "KEMPTOWN CHURCH",
    "KEYSVILLE BRUCEVILL",
    "KEYSVILLE BRUCEVILLE",
    "LABORING SONS",
    "LAKE COVENTRY",
    "LAKE POINT",
    "LANDER CREEK",
    "LAUREL WOOD",
    "LEE HILL",
    "LEGORE BRIDGE",
    "LEW WALLACE",
    "LEWIS MILL",
    "LILY PONS",
    "LIME KILN",
    "LINGANORE WOODS",
    "LINKS BRIDGE",
    "LITTLE BROOK",
    "LITTLE ROCK",
    "LITTLE SPRING",
    "LOCH HAVEN",
    "LOCH NESS",
    "LONG CORNER",
    "LONG FARM",
    "LONGS MILL",
    "LOY WOLFE",
    "LYNN BURKE",
    "LYNN CREST",
    "MAE WADE",
    "MANOR WOODS",
    "MAPLE TERRACE",
    "MARCIES CHOICE",
    "MARKET SPACE",
    "MARTHA MASON",
    "MEETING HOUSE",
    "MICHAELS MILL",
    "MIDDLE POINT",
    "MILL FORGE",
    "MILL ISLAND",
    "MILLS MANOR",
    "MISTY MEADOW",
    "MONARCH RIDGE",
    "MONOCACY BOTTOM",
    "MONOCACY RIVER",
    "MONOCACY VIEW",
    "MOON MAIDEN",
    "MOTTERS STATION",
    "MOUNT EPHRAIM",
    "MOUNT LENA",
    "MOUNT PHILLIP",
    "MOUNT TABOR",
    "MOUNT ZION",
    "MOUNTAIN APPROACH",
    "MOUNTAIN CHURCH",
    "MOUNTAIN VIEW",
    "MOUTH OF MONACACY",
    "NAYLORS MILL",
    "NICODEMUS MILL",
    "NORTH POINTE",
    "OAK HILL",
    "OLIVE SCHOOL",
    "PARK CENTRAL",
    "PARK MILLS",
    "PEACH ORCHARD",
    "PENN SHOP",
    "PENNYFIELDS LOCK",
    "PENTERRA MANOR",
    "PETE WILES",
    "PICNIC WOODS",
    "PLEASANT VALLEY",
    "PLEASANT VIEW",
    "PLEASANT WALK",
    "POINT OF ROCKS",
    "POOLE JONES",
    "POTOMAC OVERLOOK",
    "POTOMAC VIEW",
    "PRICES DISTILLERY",
    "PUBLIC SAFETY",
    "QUAIL KNOB",
    "QUEBEC SCHOOL",
    "QUINN ORCHARD",
    "QUIRAUK SCHOOL",
    "RAMP MOTTER",
    "RED HILL",
    "RED WING",
    "REELS MILL",
    "REICHS FORD",
    "RENO MONUMENT",
    "RHODE ISLAND",
    "RIDGE CREST",
    "RIPPLING BROOK",
    "RIVER OAKS",
    "RIVER RUN",
    "ROCK CREEK",
    "ROCKY FOUNTAIN",
    "ROCKY RIDGE",
    "ROCKY SPRINGS",
    "RODDY CREEK",
    "ROUND HILL",
    "ROY SHAFER",
    "ROYAL OAK",
    "SAGE HEN",
    "SAINT ANTHONY",
    "SAINT ANTHONYS",
    "SAINT CLAIRE",
    "SAINT MARKS",
    "SAINT PAUL",
    "SAINT SIMON",
    "SANDRA LEE",
    "SANDY HOOK",
    "SANDY SPRING",
    "SEA GULL",
    "SHAFERS MILL",
    "SILVER CREST",
    "SIXES BRIDGE",
    "SMALL GAINS",
    "SPRING FOREST",
    "SPRING HOLLOW",
    "SPRING RIDGE",
    "SPRING RUN",
    "STONE RIDGE",
    "STONERS FORD",
    "SUGAR MAPLE",
    "SUGARLOAF BLUE",
    "SUGARLOAF MOUNTAIN",
    "SWIMMING POOL",
    "TALL OAKS",
    "TALLYN HUNT",
    "TAYLORS VALLEY",
    "TEEN BARNES",
    "THOMAS JOHNSON",
    "TOLL HOUSE",
    "TOMS CREEK CHURCH",
    "TULIP TREE",
    "TWIN ARCH",
    "UNION RIDGE",
    "UPLAND RIDGE",
    "UTICA MILLS",
    "VALLEY VIEW",
    "VILLAGE GATE",
    "VILLAGE OAKS",
    "WAGON SHED",
    "WALKERS VILLAGE",
    "WALNUT RIDGE",
    "WALTER MARTZ",
    "WASH HOUSE",
    "WATER STREET",
    "WATERS EDGE",
    "WEST OAK",
    "WHITE FLINT",
    "WHITE OAK",
    "WHITE ROCK",
    "WILD FIG",
    "WILLOW GLEN",
    "WILLOW OAK",
    "WILLOW TREE",
    "WINDY HILL",
    "WINTER BROOK",
    "WOOD HOLLOW",
    "WORMANS MILL",
    "WYE CREEK",
    "YELLOW SHEAVE",
    "YELLOW SPRINGS",
    "YOUNG FAMILY"
  };
  
  private static final CodeSet CALL_LIST = new CodeSet(
      "911 HANG UP INVESTIGATION",
      "ABDOMINAL PAIN",
      "ABDOMINAL PAIN (ALS)",
      "ADDITIONAL ALARM / FIRE / -RPD_INTER_DISP",
      "ADDITIONAL ALARM / FIRE / 2ND_ALRM_RURAL",
      "ADDITIONAL ALARM / FIRE / 2ND_ALRM_URBAN",
      "ADDITIONAL ALARM / FIRE / 3RD_ALRM_RURAL",
      "ADDITIONAL ALARM / FIRE / 3RD_ALRM_URBAN",
      "ADDITIONAL ALARM / TASK FORCE / BRUSH_TASK_FORCE",
      "ADDITIONAL ALARM / TASK FORCE / EMS_TASK_FORCE",
      "ADDITIONAL ALARM / TASK FORCE / FIRE_TASK_FORCE",
      "ADDITIONAL ALARM / TASK FORCE / TNKR_TASK_FORCE",
      "AIRCRAFT EMERGENCY / ALERT-2",
      "ALLERGIC REACTION",
      "ALLERGIC REACTION - BLS RESPONSE",
      "ALERT 1 / SMALL AIRCRAFT (MINOR EMERGENCY)",
      "AMBULANCE TRANSFER",
      "AMBULANCE TRANSFER / default",
      "APPLIANCE FIRE",
      "APPLIANCE FIRE / CONTAINED",
      "APPLIANCE FIRE / CONTAINED / LIGHT SMOKE",
      "ASSIST PATIENT - NON-EMERGENCY RESPONSE",
      "ASTHMA ATTACK",
      "ATV ACCIDENT",
      "AUTOMATIC MEDICAL ALARM",
      "BACK PAIN",
      "BACK PAIN (ALS RESPONSE)",
      "BACK PAIN /",
      "BICYCLIST STRUCK",
      "BRUSH FIRE",
      "BRUSH TRUCK TRANSFER",
      "BUILDING COLLAPSE - RESCUE PRE ALERT",
      "BUILDING FIRE (PRE-ALERT)",
      "BUILDING FIRE W/ ENTRAPMENT (PRE-ALERT)",
      "BUILDING FIRE W/ HAZMAT (PRE-ALERT)",
      "BURN INJURY - NOTIFY FIRE MARSHAL",
      "BURNT FOOD",
      "BUS ACCIDENT - RESCUE PRE ALERT",
      "CARDIAC ARREST",
      "CARDIAC PATIENT",
      "CARDIAC PATIENT BLS",
      "CHIMNEY FIRE",
      "CHIMNEY FIRE (NO EXTENSION) (STRUCTURE PRE-ALERT)",
      "CHEST PAIN",
      "CHEST PAIN BLS",
      "CHIMNEY FIRE (NO EXTENSION)",
      "CHOKING",
      "CO DETECTOR ACTIVATION (NON-EMERGENCY RESPONSE)",
      "CO DETECTOR ACTIVATION - NON-EMERGENCY RESPONSE",
      "COLD EXPOSURE",
      "COMMERCIAL-INDUSTRIAL / FIRE / SMOKE (PREALERT)",
      "COMMERCIAL-INDUSTRIAL / ODOR OF SMOKE",
      "COMMERCIAL BLDG / CO DETECTOR ACTIVATION WITH SICK PERSON(S)",
      "COMMERCIAL BLDG / GENERAL FIRE ALARM ACTIVATION",
      "COMMERCIAL BLDG / HEAT DETECTOR ACTIVATION",
      "COMMERCIAL BLDG / KEYPAD ACTIVATION",
      "COMMERCIAL BLDG / PULL STATION ACTIVATION",
      "COMMERCIAL BLDG / SMOKE DETECTOR ACTIVATION (no fire or smoke)",
      "COMMERCIAL BLDG / SPRINKLER-WATERFLOW ACTIVATION",
      "COMMERCIAL BLDG / UNKNOWN ALARM TYPE",
      "COMMERCIAL FIRE ALARM",
      "COMMERCIAL FIRE ALARM / AUTOMATIC",
      "COMMERCIAL FIRE ALARM / WATERFLOW",
      "COMMERCIAL STRUCTURE / FIRE-VISIBLE",
      "COMMERCIAL STRUCTURE / ODOR",
      "COMMERCIAL STRUCTURE / SMOKE",
      "DECEASED PERSON",
      "DECREASED LEVEL OF CONSCIOUSNESS",
      "DECREASED LOC AFTER A FALL",
      "DETAIL / TRAINING",
      "DIABETIC",
      "DIABETIC BLS",
      "DIVISION OF FIRE AND RESCUE SERVICES NOTIFICATION",
      "ELECTRICAL BURN - NOTIFY POWER COMPANY",
      "ELECTRICAL HAZARD",
      "ELECTRICAL HAZARD NEAR OR IN WATER",
      "ELECTRICAL ODOR",
      "ELEVATOR MALFUNCTION / OCCUPIED",
      "ELEVATOR MALFUNCTION / OVERRIDE",
      "ENGINE TRANSFER",
      "ENTRAPMENT / except PERIPHERAL",
      "ENTRAPMENT / FINGER OR TOE TRAPPED",
      "ENTRAPMENT / PERIPHERAL ENTRAPMENT",
      "EXPIRED PERSON",
      "EXPLOSION (UNKNOWN TYPE W INJURIES)",
      "EYE INJURY",
      "EYE INJURY ALS",
      "FARM MACHINERY FIRE",
      "FIRE ALARM SITUATION UNKNOWN",
      "FIRE POLICE EVENT / TRAFFIC",
      "FIRE REPORTED OUT",
      "FIRE WITH PERSONS TRAPPED / HOUSE",
      "FLOODING CONDITION",
      "FUEL ODOR / OUTSIDE",
      "FUEL SPILL",
      "FUEL SPILL / MINOR SPILL",
      "FUEL SPILL / UNCONTAINED",
      "HANGING",
      "HAZMAT INCIDENT (SPECIFY)",
      "HEAT EXPOSURE BLS",
      "HEAT RELATED EMERGENCY",
      "HELICOPTER LANDING ZONE - NON-EMERGENCY RESPONSE",
      "HEMORRHAGE",
      "HEMORRHAGE BLS",
      "HIGH ANGLE RESCUE",
      "HIGH LIFE HAZARD / FIRE / SMOKE (PRE-ALERT)",
      "HIGH LIFE HAZARD / GENERAL ALARM ACTIVATION",
      "HIGH LIFE HAZARD / HOTEL-FIRE",
      "HIGH LIFE HAZARD / HOTEL-ODOR",
      "HIGH LIFE HAZARD / HOTEL-SMOKE",
      "HIGH LIFE HAZARD / OTHER TYPE ALARM ACTIVATION",
      "HIGH LIFE HAZARD / PULL STATION ACTIVATION",
      "HIGH LIFE HAZARD / SMOKE DETECTOR ACTIVATION (no fire or smoke)",
      "HIGH RISE >3 STORIES / HIGH-RISE-SMOKE",
      "HOUSE / APPLIANCE FIRE",
      "HOUSE / APPLIANCE FIRE (STRUCTURE PRE-ALERT)",
      "HOUSE / FIRE-VISIBLE",
      "HOUSE / ODOR",
      "HOUSE / SMOKE",
      "HOUSE EXPLOSION",
      "HOUSE FIRE (PRE-ALERT)",
      "HOUSE FIRE W/ ENTRAPMENT (PRE-ALERT)",
      "HOUSE FIRE W/ INJURY (PRE-ALERT)",
      "ILLEGAL OPEN BURNING - NON-EMERGENCY RESPONSE",
      "INJURED PERSON",
      "INJURED PERSON /",
      "INJURED PERSON / ARREST, UNCONSCIOUS, EXTREME FALL",
      "INJURED PERSON / ASSAULT",
      "INJURED PERSON / PATIENT NOT ALERT",
      "INJURED PERSON (SPECIFY NATURE)",
      "INJURED PERSON FROM ANIMAL BITE OR ATTACK",
      "INJURED PERSON FROM ASSAULT",
      "INJURED PERSON FROM ASSAULT (ALS) / SCENE SECURE",
      "INJURED PERSON FROM A VEHICLE ACCIDENT",
      "INJURED PERSON W/ DECREASED LOC",
      "INJURY FROM VEHICLE ACCIDENT",
      "INSIDE GAS LEAK",
      "INSIDE GAS LEAK (STRUCTURE PRE-ALERT)",
      "INSIDE GAS LEAK W SICK PERSON(S)",
      "INSIDE GAS LEAK W/ SICK PERSON(S)",
      "INVESTIGATION / MISCELLANEOUS / FIRE",
      "LARGE BRUSH FIRE",
      "LARGE NON-DWELLING FIRE (PRE-ALERT)",
      "LARGE NON-DWELLING FIRE / BARN",
      "LARGE NON-DWELLING / FIRE / SMOKE (PRE-ALERT)",
      "LARGE NON-DWELLING / TRAPPED PERSON(S) (PRE-ALERT)",
      "LIGHT SMOKE IN THE BUILDING",
      "LIGHT SMOKE IN THE HOUSE",
      "LOCKOUT - SPECIFY RESPONSE",
      "MEDIC UNIT TRANSFER",
      "MENTAL PATIENT - (NON-EMERGENCY RESPONSE)",
      "MENTAL PATIENT - NON-EMERGENCY RESPONSE",
      "MENTAL PATIENT ALS",
      "MISCELLANEOUS (SPECIFY)",
      "MOBILE HOME / TRAILER / OFFICE / MOBILE/HOME-FIRE",
      "MOBILE HOME-TRAILER / FIRE / SMOKE",
      "MOTORCYCLE ACCIDENT",
      "MOUNTAIN OR TRAIL RESCUE",
      "MOUNTAIN RESCUE - NOTIFY PARK SERVICE",
      "MUD / MARSH / QUICKSAND RESCUE",
      "MULTI-FAMILY / CO DETECTOR ACTIVATION (Non-Emergency Response)",
      "MULTI-FAMILY / GENERAL FIRE ALARM ACTIVATION",
      "MULTI-FAMILY / SMOKE DETECTOR ACTIVATION (no fire or smoke)",
      "MULTI-FAMILY / SPRINKLER-WATERFLOW ACTIVATION",
      "MULTI-FAMILY / UNKNOWN ALARM TYPE",
      "MULTI-FAMILY DWELLING FIRE (PRE-ALERT)",
      "MULTI-FAMILY STRUCTURE / FIRE-VISIBLE",
      "MULTI-FAMILY STRUCTURE / ODOR",
      "MULTI-FAMILY STRUCTURE / SMOKE",
      "MUTUAL AID TO (SPECIFY)",
      "NOSEBLEED",
      "NOTIFICATION",
      "OBSTETRIC PATIENT",
      "OBSTETRIC PATIENT ALS",
      "ODOR / INSIDE (not smoke)",
      "ODOR / INSIDE WITH SICK PERSON(S)",
      "ODOR OF SMOKE IN THE BUILDING",
      "ODOR OF SMOKE IN THE HOUSE",
      "ODOR UNKNOWN INSIDE",
      "OUTSIDE / INVESTIGATION OF SMOKE",
      "OUTSIDE FIRE (SPECIFY)",
      "OUTSIDE FIRE / SMALL BRUSH FIRE",
      "OUTSIDE FIRE / SMALL BRUSH FIRE WITH VEHICLE",
      "OUTSIDE FIRE / VEHICLE (specify)",
      "OUTSIDE FIRE / LARGE BRUSH",
      "OUTSIDE FIRE / LARGE BRUSH FIRE",
      "OUTSIDE FIRE / LARGE BRUSH / default",
      "OUTSIDE FIRE EXTINGUISHED",
      "OUTSIDE GAS LEAK",
      "OUTSIDE GAS TANK LEAK",
      "OUTSIDE INVESTIGATION",
      "OUTSIDE INVESTIGATION OF SMOKE",
      "OUTSIDE LINE/TANK >=3D5 GALLONS",
      "OUTSIDE LINE/TANK >=5 GALLONS",
      "OUTSIDE ODOR (NATURAL/LP)",
      "OUTSIDE TANK > 5 GAL",
      "OUTSIDE TANK LEAK <5 GAL (NATURAL/LP)",
      "OVERDOSE",
      "OVERDOSE BLS",
      "PEDESTRIAN STRUCK",
      "PERSON FIRE (INSIDE)",
      "POISONING",
      "POISONING BLS",
      "PUBLIC SERVICE DETAIL / INSPECTION",
      "RAPID WATER RESCUE / POTOMAC_RIVER",
      "RAPID WATER RESCUE / STREAMS_OTHER",
      "REKINDLE",
      "RESCUE / HIGH ANGLE",
      "RESCUE / MOUNTAIN OR TRAIL",
      "RESIDENTIAL / SINGLE-FAMILY / BURNED FOOD",
      "RESIDENTIAL / SINGLE-FAMILY / FIRE / SMOKE (PRE-ALERT)",
      "RESIDENTIAL / SINGLE-FAMILY / ODOR OF SMOKE",
      "RESIDENTIAL / SINGLE-FAMILY / TRAPPED PERSON(S) (PRE-ALERT)",
      "RESIDENTIAL / MULTI-FAMILY / BURNED FOOD",
      "RESIDENTIAL / MULTI-FAMILY / FIRE / SMOKE (PRE-ALERT)",
      "RESIDENTIAL / MULTI-FAMILY / ODOR OF SMOKE",
      "RESIDENTIAL / MULTI-FAMILY / TRAPPED PERSON(S) (PRE-ALERT)",
      "RESIDENTAL FIRE ALARM",
      "RESIDENTIAL FIRE ALARM",
      "SEIZURE",
      "SERVICE CALL - SPECIFY",
      "SHOOTING",
      "SICK PERSON",
      "SINGLE FAMILY / CO ACTIVATION (Non-Emergency Response)",
      "SINGLE FAMILY / CO ACTIVATION WITH SICK PERSON(S)",
      "SINGLE FAMILY / GENERAL ALARM ACTIVATION",
      "SINGLE FAMILY / KEYPAD ACTIVATION",
      "SINGLE FAMILY / SMOKE DETECTOR ACTIVATION (no fire)",
      "SMALL BRUSH FIRE",
      "SMALL NON-DWELLING FIRE",
      "SMALL NON-DWELLING / FIRE / SMOKE",
      "SMALL NON-DWELLING STRUCTURE FIRE / GARAGE",
      "SMOKE DETECTOR ACTIVATION - NON-EMERGENCY RESPONSE",
      "SPECIFY NATURE",
      "SPILL / LARGE / UNCONTAINED / OUTSIDE",
      "SQUAD TRANSFER",
      "STABBING",
      "STILL WATER RESCUE BLS",
      "STRANDED PERSON / NON-THREATENED",
      "STRANDED PERSON / VEH NON THREATENED",
      "STROKE",
      "STRUCTURE / EXTINGUISHED",
      "STRUCTURE / UNKNOWN / FIRE / SMOKE (PRE-ALERT)",
      "STRUCTURE / UNKNOWN / ODOR OF SMOKE",
      "STRUCTURE FIRE / FIRE / SMOKE (PRE-ALERT)",
      "STRUCTURE FIRE OUT / EXTINGUISHED",
      "STUCK ELEVATOR",
      "SUBJECT LOCKED IN A VEHICLE (Specify Emergency/Non-Emergency)",
      "SUSPICIOUS ITEM WITH EXPOSURE",
      "SUSTAINED SEIZURE",
      "SYNCOPAL EPISODE",
      "SYNCOPAL EPISODE ALS",
      "SWIFT WATER RESCUE",
      "TANKER TRANSFER",
      "TEST - DO NOT DISPATCH",
      "TRANSFORMER OR POLE FIRE (SPECIFY)",
      "TRENCH RESCUE",
      "TROUBLE BREATHING",
      "TRUCK FIRE (Specify)",
      "TRUCK TRANSFER",
      "UNCONSCIOUS DIABETIC",
      "UNCONSCIOUS PERSON",
      "UNCONSCIOUS OVERDOSE",
      "UNKNOWN MEDICAL EMERGENCY",
      "UNKNOWN ODOR INSIDE",
      "UNKNOWN ODOR INSIDE W/ SICK PERSON",
      "UNKNOWN ODOR OUTSIDE",
      "UNKNOWN SITUATION / GENERAL ALARM ACTIVATION",
      "UNKNOWN SITUATION / OTHER ALARM TYPES",
      "UNKNOWN SITUATION / SMOKE DETECTOR ACTIVATION (no fire or smoke)",
      "VEHICLE ACCIDENT",
      "VEHICLE ACCIDENT / RESPIRATORY OR CHEST PAIN",
      "VEHICLE ACCIDENT / ROLLOVER",
      "VEHICLE ACCIDENT (ALS SPECIFY)",
      "VEHICLE ACCIDENT WITH ENTRAPMENT",
      "VEHICLE ACCIDENT WITH HAZMAT (SPECIFY)",
      "VEHICLE ACCIDENT W/ EJECTION",
      "VEHICLE ACCIDENT W/ ROLLOVER",
      "VEHICLE ACCIDENT W/ TROUBLE BREATHING OR CHEST PAIN",
      "VEHICLE FIRE",
      "VEHICLE FIRE / EXTINGUISHED",
      "VEHICLE FIRE / NON-STRUCTURE THREAT",
      "VEHICLE FIRE (Specify)",
      "VEHICLE FIRE THREATENING A STRUCTURE",
      "VIOLENT MENTAL PATIENT - (NON EMERGENCY RESPONSE)",
      "VIOLENT MENTAL PATIENT - NON EMERGENCY RESPONSE",
      "WASHDOWN - NON-EMERGENCY RESPONSE",
      "WATER RESCUE (Specify)",
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
        "BURK",   "Burkitsville",
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
        "CTAN",   "Taneytown",
        "CTHU",   "Thurmont",
        "CTUS",   "Tuscarora",
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
        "URBA",   "Urbana",
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
        "NEWWINDSOR CNWI",     "New Windsor",
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