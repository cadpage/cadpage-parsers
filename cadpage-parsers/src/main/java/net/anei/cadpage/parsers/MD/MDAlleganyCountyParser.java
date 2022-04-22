package net.anei.cadpage.parsers.MD;

import java.util.Properties;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.anei.cadpage.parsers.CodeSet;
import net.anei.cadpage.parsers.FieldProgramParser;
import net.anei.cadpage.parsers.MsgInfo.Data;
import net.anei.cadpage.parsers.MsgInfo.MsgType;


public class MDAlleganyCountyParser extends FieldProgramParser {

  public MDAlleganyCountyParser() {
    super("ALLEGANY COUNTY", "MD",
          "ADDR/SLP! BOX:BOX? DUE:UNIT!");
    setupCallList(CALL_LIST);
    setupMultiWordStreets(MWORD_STREET_LIST);
    setupPlaceAddressPtn(Pattern.compile("(LOWES OF ALLEGANY COUNTY)|(.*) - "), false);
  }

  @Override
  public String getFilter() {
    return "acgov_911_cad@allconet.org,cad911@alleganygov.org,messaging@iamresponding.com,alleganycad@gmail.com";
  }

  private static final Pattern SUBJECT_PTN = Pattern.compile("CAD|Company +([^ ]+)");
  private static final Pattern ENROUTE_PTN = Pattern.compile("(\\d\\d:\\d\\d) +([-A-Z0-9]+) +(Is Enroute) To: +(.*)");
  private static final Pattern MARKER = Pattern.compile("(\\d\\d:\\d\\d) #(\\d+) +");
  private static final Pattern MASTER2 = Pattern.compile("(\\d\\d?:\\d\\d?:\\d\\d?) +(.*) ([A-Z]{2}\\d{10})");

  @Override
  protected boolean parseMsg(String subject, String body, Data data) {

    Matcher match = SUBJECT_PTN.matcher(subject);
    if (!match.matches()) return false;
    data.strSource = getOptGroup(match.group(1));

    match = ENROUTE_PTN.matcher(body.replace("\n", ""));
    if (match.matches()) {
      setFieldList("TIME UNIT CALL PLACE ADDR APT CITY");
      data.msgType = MsgType.RUN_REPORT;
      data.strTime = match.group(1);
      data.strUnit = match.group(2);
      data.strCall = match.group(3);
      parseAddressField(false, match.group(4).trim(), data);
      return true;
    }

    do {
      match = MARKER.matcher(body);
      if (match.find()) {
        data.strTime = match.group(1);
        data.strCallId = match.group(2);
        body = body.substring(match.end());
        break;
      }

      match = MASTER2.matcher(body);
      if (match.matches()) {
        data.strTime = match.group(1);
        body = match.group(2).trim();
        data.strCallId = match.group(3);
        break;
      }

      if (body.startsWith("CT:")) {
        body = body.substring(3).trim();
        break;
      }

      return false;

    } while (false);

    return super.parseMsg(body, data);
  }

  @Override
  public String getProgram() {
    return "SRC TIME ID " + super.getProgram();
  }

  @Override
  public Field getField(String name) {
    if (name.equals("ADDR")) return new MyAddressField();
    return super.getField(name);
  }

  private class MyAddressField extends AddressField {
    @Override
    public void parse(String field, Data data) {
      if (!parseAddressField(true, field, data)) abort();
    }

    @Override
    public String getFieldNames() {
      return "CALL CODE PLACE ADDR APT CITY ST";
    }
  }

  private static final Pattern MBLANK_PTN = Pattern.compile(" {2,}");
  private static final Pattern CODE_PTN = Pattern.compile("(.*?) (\\d\\d?[A-Z]\\d\\d?[A-Z]?) (.*)");

  private static final Pattern CITY_PTN1 = Pattern.compile("(.*); *([A-Z]+)");
  private static final Pattern CITY_PTN2 = Pattern.compile("(.*), *(PIEDMONT)");
  private static final Pattern APT_PTN = Pattern.compile("(.*)\\b(?:APT|(FLR?)) +([^ ]+)");
  private static final Pattern PLACE_ADDR_APT_PTN = Pattern.compile("(?:(.*?) @?)?\\*(.*)\\* *(.*)");
  private static final Pattern ROUTE_PERIOD_PTN = Pattern.compile("\\b(US|RT|MD)\\.(\\d+)");
  private static final Pattern MM_PTN = Pattern.compile("\\bMM\\b");

  public boolean parseAddressField(boolean parseCall, String field, Data data) {

    field = MBLANK_PTN.matcher(field).replaceAll(" ");
    StartType st = StartType.START_PLACE;
    int flags = 0;
    if (parseCall) {
      st = StartType.START_CALL_PLACE;
      flags = FLAG_START_FLD_REQ;
      Matcher match = CODE_PTN.matcher(field);
      if (match.matches()) {
        st = StartType.START_PLACE;
        flags = 0;
        data.strCall = match.group(1).trim();
        data.strCode = match.group(2);
        field = match.group(3).trim();;
      }
    }

    Matcher match = CITY_PTN1.matcher(field);
    if (match.matches()) {
      field = match.group(1).trim();
      data.strCity = convertCodes(match.group(2), CITY_CODES);
    }

    String apt = "";
    match = APT_PTN.matcher(field);
    if (match.matches()) {
      field = match.group(1).trim();
      String tmp = match.group(2);
      apt = match.group(3);
      if (tmp != null) apt = tmp + ' ' + apt;
    }

    int pt = field.lastIndexOf('*');
    if (pt >= 0) {
      pt = field.indexOf('@', pt+1);
      if (pt >= 0) {
        st = (st == StartType.START_CALL_PLACE ? StartType.START_CALL : StartType.START_ADDR);
        data.strPlace = field.substring(pt+1).trim();
        field = field.substring(0,pt).trim();
        field = stripFieldEnd(field, "[");
      }
    }

    // Sometimes the address / place is enclosed in asterisks
    // Sometimes the place name is inside the asterisks leading or following
    // the address.  Sometimes it precedes the asterisks.  Just have to be
    // flexible
    match = PLACE_ADDR_APT_PTN.matcher(field);
    if (match.matches()) {
      String callPlace = getOptGroup(match.group(1));
      field = match.group(2).trim();
      apt = append(match.group(3).trim(), "-", apt);

      if (st == StartType.START_CALL){
        data.strCall = callPlace;
      } else if (st ==  StartType.START_CALL_PLACE){
        data.strCall = CALL_LIST.getCode(callPlace, true);
        if (data.strCall != null) {
          String place = callPlace.substring(data.strCall.length()).trim();
          data.strPlace = append(place, " - ", data.strPlace);
        } else {
          data.strCall = callPlace;
        }
      } else {
        data.strPlace = append(callPlace, " - ", data.strPlace);
      }

      boolean foundCity = false;
      match = CITY_PTN2.matcher(field);
      if (match.matches()) {
        foundCity = true;
        field = match.group(1).trim();
        data.strCity = match.group(2);
      }

      pt = -1;
      if (data.strPlace.length() == 0) pt = field.indexOf("  ");
      if (pt >=0) {
        String fld1 = field.substring(0,pt).trim();
        String fld2 = field.substring(pt+2).trim();
        Result res1 = parseAddress(StartType.START_ADDR, FLAG_CHECK_STATUS | FLAG_ANCHOR_END, fld1);
        Result res2 = parseAddress(StartType.START_ADDR, FLAG_CHECK_STATUS | FLAG_ANCHOR_END, fld2);
        if (!foundCity && res1.getStatus() > res2.getStatus()) {
          res2 = res1;
          fld1 = fld2;
        }
        data.strPlace = fld1;
        res2.getData(data);
      } else {
        parseAddress(StartType.START_ADDR, FLAG_ANCHOR_END, field, data);
      }

      if (data.strAddress.length() == 0) return false;
    }

    if (data.strAddress.length() == 0) {
      field = ROUTE_PERIOD_PTN.matcher(field).replaceAll("$1 $2");
      field = field.replace('€', '@');
      match = CITY_PTN2.matcher(field);
      if (match.matches()) {
        field = match.group(1).trim();
        data.strCity = match.group(2);
      }
      flags |= FLAG_AT_BOTH | FLAG_ALLOW_DUAL_DIRECTIONS;
      parseAddress(st, flags, field, data);

      if (st == StartType.START_CALL_PLACE || st == StartType.START_PLACE) {
        if (!data.strAddress.startsWith("INTERSTATE") && MM_PTN.matcher(data.strPlace).find()) {
          data.strAddress = append(data.strPlace, " ", data.strAddress);
          data.strPlace = "";
        }
      }
      String left = getLeft();
      if (MM_PTN.matcher(left).find()) {
        data.strAddress = append(data.strAddress, " ", left);
      } else {
        data.strPlace = append(data.strPlace, " - ", getLeft());
      }

      if (data.strAddress.length() == 0) {
        parseAddress(data.strPlace, data);
        data.strPlace = "";
      }
    }

    if (data.strAddress.startsWith("INTERSTATE") && MM_PTN.matcher(data.strPlace).find()) {
      String tmp = data.strAddress;
      data.strAddress = data.strPlace;
      data.strPlace = tmp;
    }

    data.strApt = append(data.strApt, "-", apt);

    if (data.strCity.equals("PIEDMONT")) data.strState = "WV";
    return true;
  }

  private static final String[] MWORD_STREET_LIST = new String[]{
      "AARON RUN",
      "ALI GHAN",
      "ASCAR FARM",
      "BARTLETT RUN",
      "BEACH VIEW",
      "BEALL SCHOOL",
      "BEALLS MILL",
      "BEANS COVE",
      "BEDFORD VALLEY",
      "BIG VEIN",
      "BISHOP MURPHY",
      "BISHOP WALSH",
      "BLAN AVON",
      "BLOOMING FIELDS",
      "BLUE VALLEY",
      "BOTTLE RUN",
      "BROKEN HART MINE",
      "BRUCE HOUSE",
      "BURKE HILL",
      "BURNING MINES",
      "BURTON PARK",
      "BUSKIRK HOLLOW",
      "CABIN RUN",
      "CAMPERS HILL",
      "CANAL FERRY",
      "CANAL PERRY",
      "CANNON RUN",
      "CARL HARVEY",
      "CASH VALLEY",
      "CESNICK FARM",
      "CHESTNUT GROVE",
      "CHISHOLM LINE",
      "CHURCH HILL",
      "CLEMENT ARMSTRONG",
      "COON CLUB",
      "COUNTRY CLUB MALL",
      "COUNTRY CLUB",
      "CREEK BOTTOM",
      "CREEK SIDE",
      "DANS ROCK",
      "DAWSON CEMETERY",
      "DOGWOOD HILL",
      "DR NANCY S GRASMICK",
      "DRY RIDGE",
      "DRY RUN",
      "ECKHART CEMETERY",
      "ECKHART MINES",
      "EVITTS CREEK",
      "FIR TREE",
      "FORT CUMBERLAND",
      "FRANTZ HOLLOW",
      "FROSTBURG TRAILHEAD",
      "GEORGES CREEK SOUTH MILL",
      "GEORGES CREEK",
      "GOLDEN CROSS",
      "GRAVEL HILL",
      "GUN CLUB",
      "HAMPTON INN",
      "HENRY RUSSELL",
      "HIGH ROCK",
      "HIGHLAND ESTATES",
      "HOFFMAN HOLLOW",
      "HOOKER HOLLOW",
      "IRONS MOUNTAIN",
      "KENNELLS MILL",
      "KNOBLEY VIEW",
      "KYLE HILL",
      "LAKE GORDON",
      "LAURA LEE",
      "LAUREL RUN CEMETARY",
      "LAUREL RUN",
      "LOG TRAIL",
      "MAPLE LEAF",
      "MARSH MANOR",
      "MEXICO FARMS",
      "MILL RUN",
      "MOUNT PLEASANT",
      "MOUNT SAVAGE",
      "NAVES CROSS",
      "NORTH BRANCH",
      "OAKLAWN EXT",
      "OCEAN HILL",
      "OLD MINING",
      "ORCHARD MEWS",
      "PALO ALTO",
      "PARADISE HILL",
      "PEA VINE",
      "PINE HILL",
      "PINE RIDGE",
      "PINEY MOUNTAIN",
      "PITTSBURGH PLATE GLA",
      "PLAINS OF MOAB",
      "PLEASANT VIEW",
      "POMPEY SMASH",
      "POND HILL",
      "POTOMAC HOLLOW",
      "POTOMAC VIEW",
      "QUARRY RIDGE",
      "QUEEN ROCK",
      "QUEENS POINT",
      "RECREATION AREA",
      "RED ROCK",
      "ROCKY GAP",
      "ROSE BRIAR",
      "SAINT JOHNS ROCK",
      "SAINT MARYS CHURCH",
      "SAMPSON ROCK",
      "SANDSPRING RUN",
      "SAVAGE RIVER",
      "SCIENCE PARK",
      "SELDOM SEEN",
      "SHADOE HOLLOW",
      "SILVER POINT",
      "SIRES MOUNTAIN",
      "SISLER HILL",
      "SMITH HILL",
      "SMOUSES MILL",
      "SOUTH CRANBERRY SWAMP",
      "SPRING BRIAR",
      "SPRING HOLLOW",
      "SPRING LICK",
      "SQUIRREL NECK",
      "STONEY RUN",
      "STRINGTOWN HOLLOW",
      "SUGAR MAPLE",
      "SUGAR ROW",
      "TAR WATER HOLLOW",
      "TEXAS EASTERN",
      "TIMBER RIDGE",
      "TRAIL RIDGE",
      "UNCONSCIOUS/FAINTING",
      "VALE SUMMIT",
      "VALLEY VIEW",
      "VAN PELT",
      "VERNON ESTATES",
      "VICTORY POST",
      "WALNUT BOTTOM",
      "WASHINGTON HOLLOW",
      "WATER STATION RUN",
      "WELSH HILL",
      "WHISPERING PINES",
      "WHITE CHURCH",
      "WILLOW BROOK",
      "WILLOW CREST",
      "WINEBRENNER HILL",
      "WING RIDGE",
      "WOOD ROSE",
      "YELLOW ROW"
  };

  private static final CodeSet CALL_LIST = new CodeSet(
      "911 INFORMATION CALL",
      "911 TEST CALL",
      "911 TEST CALL TEST PAGE DO NOT RESPOND",
      "ABDOM PAINS ALS",
      "ABDOM PAINS BLS",
      "ABDOMINAL /BACK PAIN",
      "ABDOMINAL PAINS",
      "ACCIDENT NOT LISTED",
      "ACCIDENT PD",
      "ACCIDENT PED STRUCK",
      "ACCIDENT PI",
      "ADDITIONAL 911 CALL NEW/",
      "AIRCRAFT EMERGENCY",
      "ALLERGIC /MED REACT",
      "ALLERGIC REACT ALS",
      "ALLERGIC REACT ALSD",
      "ALTERD LEVEL OF CONS",
      "ALTERED LOC ALS",
      "APARTMENT FIRE",
      "ASSAULT MEDICAL",
      "AUTO ALARM BUSINESS",
      "AUTO ALARM HOUSE",
      "AUTOMOBILE FIRE",
      "BACK PAIN",
      "BACK PAIN ALS",
      "BACK PAIN BLS",
      "BARN FIRE",
      "BEHAVORIAL EMERGENCY",
      "BEHAVIORAL EMERG BLS",
      "BLEEDING INJURY ALS",
      "BLEEDING INJURY BLS",
      "BLEEDING NON TRAUMA",
      "BREATH DIFF ALS",
      "BREATH DIFF BLS",
      "BREATHING DIFFICULTY",
      "BRUSH FIRE",
      "BURNS THERM,ELEC,CHE",
      "BUSINESS FIRE",
      "CARDIAC",
      "CARDIAC EMERG ALS",
      "CHEST PAINS ALS",
      "CHEST PAINS ALSD",
      "CHEST PAINS BLS",
      "CHEST PAINS, HEART",
      "CO DETECTOR COMM",
      "CHOKING",
      "CHOKING PATIENT ALS",
      "CHOKING PATIENT BLS",
      "CO DETECTOR RES",
      "COLLAPSED STRUCTURE",
      "CPR FULL ARREST HOT",
      "CVA/STROKE ALS",
      "DEFAULT EMD",
      "DIABETIC",
      "DIABETIC EMERG ALS",
      "DIABETIC EMERG BLS",
      "DIFFICULTY BREATHING",
      "DUMPSTER FIRE THE F BAR",
      "ELECTROCUTION",
      "EMS SERVICE CALL",
      "FALL 3 FEET OR GREAT",
      "FALL INJURY",
      "FALL PATIENT ALS",
      "FALL PATIENT BLS",
      "FALL PATIENT BLSA",
      "FALL PATIENT BLSB",
      "FIRE STANDBY",
      "FLOODING CONDITIONS",
      "FLUE FIRE",
      "FULL ARREST",
      "FRACTURE/EXTREMITY",
      "GARAGE FIRE COMM",
      "GARAGE FIRE RESIDENT",
      "GENERAL UTIL PAGE",
      "GRINDER PUMP ACTIVAT",
      "GROUND LEVEL FALL",
      "GUNSHOT WOUND",
      "HEADACHE",
      "HOUSE FIRE",
      "LACERATION",
      "LANDING SITE",
      "LINES DOWN",
      "LOCAL OTHER FIRE",
      "MEDICAL ALARM",
      "MEDICAL EMERG ALS",
      "MEDICAL EMERG BLS",
      "MEDICAL EMERGENCY",
      "MEDIC ASSIST",
      "MVC ENTRAPMENT",
      "NATURAL GAS BUSINESS",
      "NATURAL GAS LEAK OUT",
      "NATURAL GAS RESIDENT",
      "OB, CHILDBIRTH",
      "ODOR IN BUSINESS",
      "ODOR IN RESIDENCE",
      "OTHER HAZMAT",
      "OUTSIDE EXPLOSION",
      "OVERDOSE, POISONING",
      "OVERDOSE/POISON ALS",
      "RESCUE CALL",
      "SEARCH",
      "SCHOOL FIRE",
      "SEIZURE",
      "SERVICE CALL NOT LIS",
      "SHED FIRE",
      "SMOKE INVESTIGATION",
      "STABBING",
      "STROKE/CVA",
      "SYNCOPAL EPISODE",
      "TEST PAGE ONLY",
      "TEST PAGE TEST TEST",
      "TRAILER FIRE",
      "TRAIN FIRE",
      "TRANSFORMER ARCING",
      "TRAUMA WITH INJURY",
      "TRAUMA INJURY ALS",
      "TRAUMA INJURY BLS",
      "TREE DOWN",
      "TRUCK FIRE",
      "UNABLE TO WALK",
      "UNCONSCIOUS",
      "UNCONSCIOUS ALS",
      "UNCONSCIOUS BLS",
      "UNCONSCIOUS, UNRESPO",
      "UNCONSCIOUS/FAINTING",
      "UTILITIES",
      "VEHICLE BRAKES FIRE",
      "VEHICLE LEAKING FUEL",
      "WATER LEAK",
      "WATER RESCUE",
      "XFER MINERAL",

      "Is Enroute"
  );

  private static final Properties CITY_CODES = buildCodeTable(new String[]{
      "FRO",  "FROSTBURG"
  });
}
