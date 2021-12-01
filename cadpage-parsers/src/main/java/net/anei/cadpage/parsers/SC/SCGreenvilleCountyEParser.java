package net.anei.cadpage.parsers.SC;

import net.anei.cadpage.parsers.CodeSet;
import net.anei.cadpage.parsers.MsgInfo.Data;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.anei.cadpage.parsers.FieldProgramParser;

public class SCGreenvilleCountyEParser extends FieldProgramParser {

  public SCGreenvilleCountyEParser() {
    super(SCGreenvilleCountyParser.CITY_LIST, "GREENVILLE COUNTY", "SC",
          "CALL ADDR CITY PLACE! EMPTY! END");
    setupCallList(CALL_LIST);
    setupMultiWordStreets(MWORD_STREET_LIST);
    removeWords("GATEWAY", "PLACE", "ROAD");
  }

  @Override
  public String getFilter() {
    return "InformCADPaging@Greenvillecounty.org,@whfd.org";
  }

  private static final Pattern MISSING_BLANK_PTN = Pattern.compile("(?<=Mutual Aid/Assist Outside Agen|Motor Vehicle Collision/Injury|Struct Fire Resi Single Family|Vehicle Fire Comm/Box/Mot Home)(?! )");
  private static final Pattern MASTER1 = Pattern.compile("(.*?)\\(C\\) (.*?)((?:Non-)?Emergency|Bravo) (\\d{4,6}-\\d{6})\\b *(.*)");
  private static final Pattern MASTER2 = Pattern.compile("Incident Type:(.*?) Location:(.*)");
  private static final Pattern INFO_DELIM_PTN = Pattern.compile("(?: |(?<=;))\\[\\d+\\] ");
  private static final Pattern CITY_DASH_PTN = Pattern.compile("(?<=[A-Z])-(?= )");
  private static final Pattern APT_PTN = Pattern.compile("[A-Z]?\\d{1,5}[A-Z]?|[A-Z]");
  private static final Pattern INFO_BRK_PTN = Pattern.compile("\\[1?\\d\\]");
  private static final Pattern INFO_GPS_PTN = Pattern.compile(".*\\bLAT: ([-+]?\\d{2,3}\\.\\d{6,}) LON: ([-+]?\\d{2,3}\\.\\d{6,})\\b.*");
  private static final Pattern INFO_JUNK_PTN = Pattern.compile("\\*+ADD'L Wireless Info : .*|Automatic Case Number\\(s\\) issued for Incident #.*|\\*+Class of Seri?vi?ce.*");

  @Override
  protected boolean parseMsg(String body, Data data) {

    // Rule out some other alert formats
    if (body.startsWith("CAD:")) return false;
    if (body.startsWith("LOC:")) return false;

    body = stripFieldStart(body, "*");
    body = body.replace(" GREER` ", " GREER ");

    Matcher match = MASTER2.matcher(body);
    if (match.matches()) {
      setFieldList("CALL ADDR APT CITY UNIT");
      data.strCall = match.group(1).trim();
      String addr = match.group(2).trim();
      int pt = addr.indexOf("(C)");
      if (pt >= 0) {
        String cityUnit = addr.substring(pt+3).trim();
        addr = addr.substring(0,pt).trim();
        parseAddress(addr, data);
        parseAddress(StartType.START_ADDR, FLAG_ONLY_CITY, cityUnit, data);
        data.strUnit = getLeft();
      } else {
        parseAddress(StartType.START_ADDR,addr, data);
        data.strUnit = getLeft();
      }
      return true;
    }

    boolean good = false;
    String extra = null;
    match = INFO_DELIM_PTN.matcher(body);
    if (match.find()) {
      good = true;
      extra = body.substring(match.end()).trim();
      body = body.substring(0, match.start());
      if (body.endsWith(" ") || body.contains("   ")) return false;
    }

    if (body.contains(";")) {
      return parseFields(body.split(";", -1), data);
    }


    body = MISSING_BLANK_PTN.matcher(body).replaceFirst(" ");

    match = MASTER1.matcher(body);
    if (match.matches()) {
      good = true;
      setFieldList("CALL CITY ADDR APT PLACE PRI ID X GPS INFO");
      data.strCall = match.group(1).trim();
      String cityAddr = match.group(2).trim();
      data.strPriority = match.group(3);
      data.strCallId = match.group(4);
      data.strCross = match.group(5);

      parseAddress(StartType.START_ADDR, FLAG_ONLY_CITY, cityAddr, data);
      parseAddress(StartType.START_ADDR, FLAG_NO_CITY, getLeft(), data);
      data.strPlace = getLeft();
      if (data.strAddress.length() == 0) {
        String addr = data.strCall;
        data.strCall = "";
        parseAddress(StartType.START_CALL, FLAG_START_FLD_REQ | FLAG_NO_CITY | FLAG_ANCHOR_END, addr, data);
      }
    }

    else {
      setFieldList("CALL ADDR APT CITY PLACE GPS INFO");
      int pt = body.indexOf("(C)");
      if (pt >= 0) {
        good = true;
        String cityPlace = body.substring(pt+3).trim();
        body = body.substring(0, pt).trim();
        pt = body.indexOf("(A)");
        if (pt >= 0) {
          data.strCall = body.substring(0,pt).trim();
          parseAddress(body.substring(pt+3).trim(), data);
        } else {
          parseAddress(StartType.START_CALL, FLAG_IGNORE_AT | FLAG_START_FLD_NO_DELIM | FLAG_NO_CITY | FLAG_ANCHOR_END, body, data);
        }
        cityPlace = CITY_DASH_PTN.matcher(cityPlace).replaceFirst("");
        parseAddress(StartType.START_ADDR, FLAG_ONLY_CITY, cityPlace, data);
        data.strPlace = getLeft();
      } else {
        pt = body.indexOf("(A)");
        if (pt >= 0) {
          data.strCall = body.substring(0,pt).trim();
          body = body.substring(pt+3).trim();
          parseAddress(StartType.START_ADDR, FLAG_IGNORE_AT, body, data);
        } else {
          parseAddress(StartType.START_CALL, FLAG_START_FLD_REQ | FLAG_START_FLD_NO_DELIM | FLAG_IGNORE_AT, body, data);
          if (data.strCall.endsWith(":")) return false;
        }
        data.strPlace = getLeft();
        if (!data.strAddress.isEmpty() && !data.strCity.isEmpty()) good = true;
      }
    }

    if (APT_PTN.matcher(data.strPlace).matches()) {
      data.strApt = append(data.strApt, "-", data.strPlace);
      data.strPlace = "";
    }

    if (extra != null) {
      for (String part : INFO_BRK_PTN.split(extra)) {
        part = part.trim();
        part = stripFieldEnd(part, ",");
        part = stripFieldEnd(part, "[Shared]");

        match = INFO_GPS_PTN.matcher(part);
        if (match.matches()) {
          setGPSLoc(match.group(1)+','+match.group(2), data);
          continue;
        }

        if (INFO_JUNK_PTN.matcher(part).matches()) continue;

        data.strSupp = append(data.strSupp, "\n", part);
      }
    }

    return good;
  }

  @Override
  public Field getField(String name) {
    if (name.equals("CITY")) return new MyCityField();
    return super.getField(name);
  }

  private class MyCityField extends CityField {
    @Override
    public void parse(String field, Data data) {
      field = stripFieldStart(field, "(C)");
      super.parse(field, data);
    }
  }

  private static final String[] MWORD_STREET_LIST = new String[]{
      "ALLENDALE ABBEY",
      "ANDERSON RIDGE",
      "ANSEL SCHOOL",
      "ARBOR CREST",
      "ASHBY GROVE",
      "ASHMORE BRIDGE",
      "ASSEMBLY VIEW",
      "AUTUMN HILL",
      "BAILEY CREEK",
      "BAILEY MILL",
      "BAMBER GREEN",
      "BATES CROSSING",
      "BAY HILL",
      "BEAVER DAM",
      "BELGIAN BLUE",
      "BELUE MILL",
      "BELVUE SCHOOL",
      "BEN HAMBY",
      "BENT CREEK",
      "BERRY GLEN",
      "BERRY MILL",
      "BERRY PINE",
      "BIG FOOT",
      "BIG OAK",
      "BILL TIMMONS",
      "BLACKBERRY VALLEY",
      "BLUE RIDGE",
      "BLUE WATER",
      "BLYTHE SHOALS",
      "BOILING SPRINGS",
      "BONNIE WOODS",
      "BROCKMAN MCCLIMON",
      "BROOK GLENN",
      "BROOKE LEE",
      "BROOKS POINTE",
      "BRUSHY CREEK",
      "BUCHANAN RIDGE",
      "BUNKER HILL",
      "BURL HOLLOW",
      "BURNING BUSH",
      "BURNT NORTON",
      "BUTLER SPRINGS",
      "CAESARS HEAD",
      "CALIBER RIDGE",
      "CALLAHAN MOUNTAIN",
      "CAMP CREEK",
      "CAMPBELL MILL",
      "CANNIE CLARK",
      "CANYON CREST",
      "CARL KOHRT",
      "CARTER RUN",
      "CCC CAMP",
      "CEDAR CLIFF",
      "CEDAR GLENN",
      "CEDAR GROVE",
      "CEDAR LANE",
      "CEDAR PINES",
      "CHANDLER CREEK",
      "CHASTAIN HILL",
      "CHESTNUT MOUNTAIN",
      "CHESTNUT POND",
      "CHICK SPRINGS",
      "CHRIST ROCK CAMP",
      "CHURCH OF GOD",
      "CLARE BANK",
      "CLEAR SPRINGS",
      "CLIMBING ROSE",
      "CLUB TERRACE",
      "COACH HILL",
      "COG HILL",
      "COMMERCIAL AUGUSTA",
      "CONNORS CREEK",
      "COOL SPRINGS CHURCH",
      "COTTAGE CREEK",
      "COUNTRY CLUB",
      "COUNTRY COVE",
      "COUNTRY MIST",
      "CRAIGO CREEK",
      "CREPE MYRTLE",
      "CRESTWOOD FOREST",
      "CRIMSON GLORY",
      "CROSS HILL",
      "CROWN EMPIRE",
      "CUNNINGHAM POINT",
      "DEAN WILLIAMS",
      "DEER RUN",
      "DEVILS FORK",
      "DIVIDING WATER",
      "DRY POCKET",
      "DUCK POND",
      "DUNCAN CHAPEL",
      "DUNKLIN BRIDGE",
      "EAGLE CREEK",
      "EASLEY BRIDGE",
      "EASTON MEADOW",
      "EDWARDS MILL",
      "ELSTAR LOOP",
      "ENCLAVE PARIS",
      "ESTES PLANT",
      "EXECUTIVE CENTER",
      "FAIR CREEK",
      "FAIRVIEW CHURCH",
      "FARRS BRIDGE",
      "FAWN CREEK",
      "FAWN HILL",
      "FEWS BRIDGE",
      "FEWS CHAPEL",
      "FIRE PINK",
      "FIVE FORK PLAZA",
      "FIVE FORKS",
      "FOOT HILLS",
      "FOREST LAKE",
      "FORK SHOALS",
      "FORKED OAK",
      "FORKSVILLE CHURCH",
      "FORREST HAVEN",
      "FOUR OAK",
      "FOX CREEK",
      "FOX RIDGE",
      "FURMAN HALL",
      "GAP CREEK",
      "GARDEN MARKET",
      "GIBSON OAKS",
      "GLASSY FALLS",
      "GLASSY MOUNTAIN",
      "GLOBAL COMMERCE",
      "GLOUCESTER FERRY",
      "GOODWIN BRIDGE",
      "GOWENSVILLE CHURCH",
      "GREAT GLEN",
      "GREEN BANK",
      "GREEN VALLEY",
      "GROCE MEADOW",
      "GSP LOGISTICS",
      "GUM SPRINGS",
      "HAMMETT BRIDGE",
      "HARRISON BRIDGE",
      "HART CUT",
      "HAWK KNOB",
      "HAWK SPRINGS",
      "HIDDEN SPRINGS",
      "HIGH HAT",
      "HILLSIDE CHURCH",
      "HOKE SMITH",
      "HOLLY HILL",
      "HUNTS BRIDGE",
      "INDIAN RIDGE",
      "INDIAN SPRINGS",
      "J VERNE SMITH",
      "JAMISON MILL",
      "JENKINS BRIDGE",
      "JENNINGS MILL",
      "JOHN THOMAS",
      "JONES GAP",
      "JONES KELLEY",
      "JORDAN EBENEZER",
      "JUG FACTORY",
      "KEELER BRIDGE",
      "KEELER MILL",
      "KELSEY GLEN",
      "KNIGHTS SPUR",
      "KNOB CREEK",
      "LA PLATA",
      "LAKE CUNNINGHAM",
      "LAKE SHORE",
      "LAND GRANT",
      "LATIMER MILL",
      "LATIMER RIDGE",
      "LAUREN WOOD",
      "LEE VAUGHN",
      "LEMON CREEK",
      "LEXINGTON PLACE",
      "LIBERTY CHURCH",
      "LIMA BAPTIST CHURCH",
      "LINDSEY BRIDGE",
      "LINDSEY LAKE",
      "LINDSEY RIDGE",
      "LIONS CLUB",
      "LIONS PARK",
      "LISMORE PARK",
      "LITTLE RIO",
      "LITTLE TEXAS",
      "LOCUST HILL",
      "LOFTY RIDGE",
      "LONE TREE",
      "LONG SHOALS",
      "LOST LAKE",
      "LUCILLE SMITH",
      "MAGNOLIA MEADOW",
      "MAGNOLIA PLACE",
      "MAPLE TREE",
      "MARY PEACE STERLING",
      "MATTIE CAMPBELL",
      "MAYS BRIDGE",
      "MCCULLOUGH SCHOOL",
      "MCKITTRICK BRIDGE",
      "MEADOW CREEK",
      "MEADOW FIELD",
      "MEADOW FORK",
      "MEADOW TREE",
      "MILFORD CHURCH",
      "MISTY CREST",
      "MISTY GATE",
      "MITER SAW",
      "MOODY BRIDGE",
      "MOUNTAIN CREEK",
      "MOUNTAIN CREST",
      "MOUNTAIN HEIGHT",
      "MOUNTAIN LAKE",
      "MOUNTAIN VIEW SCHOOL",
      "MOUNTAIN VIEW",
      "MT LEBANON CHURCH",
      "MUSH CREEK HILL",
      "MUSH CREEK",
      "NASH MILL",
      "NEELY MILL",
      "NOBSKA LIGHT",
      "OAK GROVE",
      "OAK HAVEN",
      "OAK PARK",
      "OAK RIDGE",
      "OAK WIND",
      "OIL CAMP CREEK",
      "ONEAL CHURCH",
      "PACKS MOUNTAIN",
      "PALM SPRINGS",
      "PARIS CREEK",
      "PARIS VIEW",
      "PARK PLACE",
      "PARNELL BRIDGE",
      "PEBBLE CREEK",
      "PELHAM SQUARE",
      "PERSIMMON RIDGE",
      "PETER MCCORD",
      "PILGRIMS POINT",
      "PINE FOREST",
      "PINE KNOLL",
      "PINE LOG FORD",
      "PINE VIEW",
      "PINK DILL MILL",
      "PINNACLE LAKE",
      "PIPE LINE",
      "PLACID FOREST",
      "PLANTERS ROW",
      "PLEASANT BROOK",
      "PLEASANT HILL",
      "PLEASANT RETREAT",
      "PLUMLEY SUMMIT",
      "POINSETT COMMON",
      "PONDERS RAY",
      "POPLAR VALLEY",
      "PREWETTE HILL",
      "QUAIL HOLLOW",
      "QUAIL RUN",
      "RED HILL",
      "REGENCY HILL",
      "REID SCHOOL",
      "REVIS CREEK",
      "RIDGE ROCK",
      "RILEY SMITH",
      "RIO VISTA",
      "RIVER FALLS",
      "RIVER OAKS",
      "ROBERTS HILL",
      "ROBIN HOOD",
      "ROCK QUARRY",
      "ROCKY CREEK",
      "ROCKY KNOLL",
      "ROE CENTER",
      "ROE FORD",
      "ROPER MOUNTAIN",
      "RUDDY CREEK",
      "RUTLEDGE LAKE",
      "SALLY GILREATH",
      "SALUDA LAKE",
      "SAM LANGLEY",
      "SANDY FLAT",
      "SCOTTS BLUFF",
      "SHANNON LAKE",
      "SHORT BRANCH",
      "SILVER CREEK",
      "SIMPLY LESS",
      "SINGLE OAK",
      "SLEEPY RIVER",
      "SLIDING ROCK",
      "SPARROW HAWK",
      "SPRING FELLOW",
      "SPRING PARK",
      "SQUIRES CREEK",
      "ST ANDREWS",
      "ST AUGUSTINE",
      "ST FRANCIS",
      "ST MARK",
      "STAMEY VALLEY",
      "STANDING SPRINGS",
      "STAPLEFORD PARK",
      "STATE PARK",
      "STAUNTON BRIDGE",
      "STEWART LAKE",
      "STONE DALE",
      "SUGAR OAK",
      "SULPHUR SPRINGS",
      "SUNNY VALLEY",
      "SUNSET MAPLE",
      "SWEET JULIET",
      "SWEET WATER",
      "SWEET WILLIAM",
      "TABLE ROCK",
      "TALL PINES",
      "TALLEY BRIDGE",
      "TEA OLIVE",
      "TERRA TRACE",
      "TERRY CREEK",
      "TERRY SHOP",
      "TEX MCCLURE",
      "THREE OAK",
      "TIMBER GLEN",
      "TRAMMELL MOUNTAIN",
      "TRIDENT MAPLE",
      "TROTTERS FIELD",
      "TUBBS MOUNTAIN",
      "TURNER BARTON",
      "TURTLE CREEK",
      "TWIN SPRINGS",
      "TYGER BRIDGE",
      "VALLEY CREEK",
      "VALLEY DALE",
      "VALLEY OAK",
      "VALLEY VIEW",
      "VERNER SPRINGS",
      "VICTOR HILL",
      "VIEW SUMMIT",
      "WADE HAMPTON",
      "WAR ADMIRAL",
      "WATERS REACH",
      "WATKINS BRIDGE",
      "WHITE HORSE",
      "WHITE OAK",
      "WICKS CREEK",
      "WILD ORCHARD",
      "WILLIAM MCALISTER",
      "WILLIAM SETH",
      "WINDSOR CREEK",
      "WINDWARD PEAK",
      "WINDY OAK",
      "WOODS CHAPEL",
      "WOODS LAKE"
  };

  private static final CodeSet CALL_LIST = new CodeSet(
      "2ND ALARM - FULLY INVOLVED STRUCTURE FIRESTRUCT FIRE RESI SINGLE FAMILY",
      "ABDOMINAL PAIN",
      "ABDOMINAL PAIN_D1",
      "ABNORMAL BEHAVIOR",
      "ALARM CARBON MONOXIDE",
      "ALARM COMMERCIAL",
      "ALARM HIGH LIFE HAZARD",
      "ALARM HIGH RISE",
      "ALARM MULTI OCCUPANCY",
      "ALARM RESIDENTIAL",
      "ALLERGIC REACTION",
      "ALLERGIES/ENVENOMATIONS",
      "ANIMAL BITE/ATTACK",
      "ASSAULT",
      "ASSAULT/SEXUAL ASSAU",
      "ASSAULT/SEXUAL ASSAULT",
      "AUTO/PEDESTRIAN_D2-M",
      "BACK PAIN",
      "BACK PAIN (NON TRAUMATIC)",
      "BREATHING PROB_C2",
      "BREATHING PROB_D1",
      "BREATHING PROBLEMS",
      "BURNS OR EXPLOSION",
      "ALARM CARBON MONOXID",
      "CARD UPDATED - FUEL SPILLFUEL SPILL",
      "CARDIAC ARREST",
      "CARDIAC/RESP ARREST",
      "CARDIAC/RESP. ARRES",
      "CARDIAC/RESP. ARREST",
      "CHEST PAIN",
      "CHEST PAIN / CHEST D",
      "CHEST PAIN / CHEST DISCOMFOR",
      "CHOKING",
      "CITIZEN ASSIST",
      "CITIZEN ASSIST/SERVI",
      "CITIZEN ASSIST/SERVI",
      "CITIZEN ASSIST/SERVICE CALL",
      "CO/INHALATION/HAZMAT",
      "CONFINED SPACE/STRUCT COLLAPSE(A)",
      "CONVULSIONS/SEIZURES",
      "DIABETIC PROBLEMS",
      "DROWNING OR DIVING INCIDENT",
      "DUMPSTER RUBBISH FIRE",
      "ELECTRICAL HAZARD",
      "ELECTROCUTION/LIGHTN",
      "ELEVATOR/ESCALATOR RESCUE",
      "EMS STANDBY",
      "EXPLOSION",
      "EYE PROBLEM/INJURY",
      "FALL_A2",
      "FALL_B1",
      "FALL_D4",
      "FALLS",
      "FUEL SPILL",
      "FUEL SPILL/FUEL ODOR",
      "GAS LEAK",
      "GAS LEAK/GAS ODOR",
      "GSW/PENETRATING_D2-G",
      "GSW/PENETRATING_D4-G",
      "HAZMAT",
      "HEADACHE",
      "HEART PROBLEMS",
      "HEAT/COLD EXPOSURE",
      "HEMORRHAGE LACERATION",
      "HEMORRHAGE/LACERATIO",
      "HEMORRHAGE/LACERATION",
      "INACCESSIBLE/ENTRAPMENT",
      "LOST PERSON",
      "MEDICAL ALARM",
      "MOTOR VEHICLE COLL W/ ENTRAP",
      "MOTOR VEHICLE COLLIS",
      "MOTOR VEHICLE COLLISION",
      "MOTOR VEHICLE COLLISION INJURY",
      "MOTOR VEHICLE COLLISION ENTRAP",
      "MOTOR VEHICLE COLLISION/INJURY",
      "MUTUAL AID",
      "MUTUAL AID/ASSIST OUTSIDE AGEN",
      "ODOR",
      "ODOR (STRANGE/UNKNOWN)",
      "OUTSIDE FIRE",
      "OUTSIDE FIRE DUMPSTER/RUBISH",
      "OUTSIDE FIRE WADE",
      "OUTSIDE FIRE WILDLAND",
      "OVERDOSE",
      "OVERDOSE/POISON_D1",
      "OVERDOSE/POISON_E1",
      "OVERDOSE/POISONING",
      "OVERDOSE/POISONING_D1",
      "PREGNANCY OB",
      "PREGNANCY/CHILDBIRTH",
      "PSYCHIATRIC/ABNORMAL",
      "PSYCHIATRIC/ABNORMAL BEHAVIOR",
      "SEIZURE_D2",
      "SEIZURES",
      "SERVICE CALL",
      "SICK PERSON",
      "SMOKE INVESTIGATION",
      "SMOKE INVESTIGATION OUTSIDE",
      "STAB/GSW/PENETRATING",
      "STAB/GSW/PENETRATING INJURY",
      "STABBING OR GSW",
      "STROKE",
      "STROKE_C1",
      "STROKE/TIA",
      "STRUCT FIRE HIGH LIFE HAZARD",
      "STRUCT FIRE RESI MUL",
      "STRUCT FIRE RESI MULTI FAMILY",
      "STRUCT FIRE RESI SINGLE FAMILY",
      "STRUCTURE FIRE COMMERCIAL",
      "TRAFFIC INCIDENT",
      "TRAFFIC/TRANSPORT",
      "TRAFFIC/TRANSPORT IN",
      "TRAFFIC/TRANSPORT INCIDENT",
      "TRAUMATIC INJURY",
      "UNCONSCIOUS",
      "UNCONSCIOUS/FAINTING",
      "UNKNOWN PROBLEM",
      "UNKNOWN PROBLEM_B3",
      "VEHICLE FIRE",
      "VEHICLE FIRE COMM/BOX/MOT HOME",
      "WATER RESCUE/SINKING VEHICLE",
      "WATERCRAFT EMERG/COLLISION",
      "ZTP_ACCIDENT_HIT AND RUN",
      "ZTP_ACCIDENT_NO INJURIES",
      "ZTP_ALARM_AUDIBLE",
      "ZTP_ANIMAL_LOOSE",
      "ZTP_ASSAULT",
      "ZTP_BREAKING AND ENTERING BURG",
      "ZTP_BUSINESS CHECK",
      "ZTP_CHECK_WELFARE CHECK",
      "ZTP_DISTURBANCE",
      "ZTP_DOMESTIC_VERBAL",
      "ZTP_HELP_ROUTINE ASSIST",
      "ZTP_LARCENY_PETIT LARCENY",
      "ZTP_NOISE_LOUD MUSIC",
      "ZTP_NON CRIMINAL SERVICE",
      "ZTP_ESCORT_ADULT",
      "ZTP_FIRE_CALL",
      "ZTP_OFFICER_EXTRA PTRL BUS/RES",
      "ZTP_OFFICER_FOLLOW UP",
      "ZTP_OFFICER_REPORT TO",
      "ZTP_SEE COMPLAINANT",
      "ZTP_SHOPLIFTING",
      "ZTP_SUSPICIOUS_PERSON",
      "ZTP_TRAFFIC_RECKLESS DRIVER",
      "ZTP_TRAFFIC_TRAFFIC STOP",
      "ZTP_TRAFFIC_WORKING RADAR AT LS",
      "ZTP_VEHICLE_SUSPICIOUS"
 );
}
