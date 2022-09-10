package net.anei.cadpage.parsers.SC;

import net.anei.cadpage.parsers.CodeSet;
import net.anei.cadpage.parsers.MsgInfo.Data;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.anei.cadpage.parsers.FieldProgramParser;

public class SCGreenvilleCountyEParser extends FieldProgramParser {

  public SCGreenvilleCountyEParser() {
    super(SCGreenvilleCountyParser.CITY_LIST, "GREENVILLE COUNTY", "SC",
          "CALL ADDR CITY PLACE! INFO! INFO/N+? ( ID ( PRI X UNIT | CH UNIT ) | PRI ID UNIT/C+ ) END");
    setupCallList(CALL_LIST);
    setupMultiWordStreets(MWORD_STREET_LIST);
    removeWords("GATEWAY", "PLACE", "ROAD");
    setupProtectedNames("LOOK OFF");
  }

  @Override
  public String getFilter() {
    return "InformCADPaging@Greenvillecounty.org,@whfd.org";
  }

  private static final Pattern MISSING_BLANK_PTN = Pattern.compile("(?<=Mutual Aid/Assist Outside Agen|Motor Vehicle Collision/Injury|Struct Fire Resi Single Family|Vehicle Fire Comm/Box/Mot Home)(?! )");
  private static final Pattern INFO_DELIM_PTN = Pattern.compile("(?: |(?<=;))\\[\\d+\\] ");
  private static final Pattern TIME_FLD_PTN = Pattern.compile(";\\d\\d:\\d\\d:\\d\\d;");
  private static final Pattern CITY_DASH_PTN = Pattern.compile("(?<=[A-Z])-(?= )");
  private static final Pattern APT_PTN = Pattern.compile("[A-Z]?\\d{1,5}[A-Z]?|[A-Z]");

  @Override
  protected boolean parseMsg(String body, Data data) {

    // Rule out some other alert formats
    if (body.startsWith("CAD:")) return false;
    if (body.startsWith("LOC:")) return false;

    body = stripFieldStart(body, "*");
    body = body.replace(" GREER` ", " GREER ");

    boolean good = false;
    String extra = null;
    Matcher match = INFO_DELIM_PTN.matcher(body);
    if (match.find()) {
      good = true;
      if (body.charAt(match.start()-1) != ';') {
        extra = body.substring(match.end()).trim();
        body = body.substring(0, match.start());
        if (body.endsWith(" ") || body.contains("   ")) return false;
      }
    }

    if (body.contains(";")) {
      
      // Reject NCPolkCounty alerts
      if (TIME_FLD_PTN.matcher(body).find()) return false;
      
      return parseFields(body.split(";", -1), data);
    }
    
    if (body.contains("~")) {
      return parseFields(body.split("~", -1), data);
    }

    else {

      body = MISSING_BLANK_PTN.matcher(body).replaceFirst(" ");

      setFieldList("CALL ADDR APT CITY ALERT PLACE INFO GPS");
      body = stripLeadAlert(body, data);
      int pt = body.indexOf("Struct Fire");
      if (pt > 0) {
        data.strSupp = body.substring(0,pt).trim();
        body = body.substring(pt);
      }
      pt = body.indexOf("(C)");
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
        data.strPlace = stripLeadAlert(getLeft(), data);
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
        data.strPlace = stripLeadAlert(getLeft(), data);
        if (!data.strAddress.isEmpty() && !data.strCity.isEmpty()) good = true;
      }

      if (APT_PTN.matcher(data.strPlace).matches()) {
        data.strApt = append(data.strApt, "-", data.strPlace);
        data.strPlace = "";
      }
    }

    if (extra != null) {
      parseInfo(extra, data);
    }

    return good;
  }

  private static final Pattern LEAD_ALERT_PTN = Pattern.compile("(\\*+[A-Z]+\\*+(?:[A-Z]+\\*+)?|ATU ON STANDBY|CALL CANCELLED.|ALL RESPONDING UNITS USE OPS ?\\d+|TAKE (?:ALL )?TRAFFIC TO (?:OPS ?\\d+|TAC \\d+ ZONE \\d+ CHAN \\d+)) *(.*)", Pattern.CASE_INSENSITIVE);

  private String stripLeadAlert(String field, Data data) {
    Matcher match = LEAD_ALERT_PTN.matcher(field);
    if (match.matches()) {
      data.strAlert = match.group(1);
      field = match.group(2);
    }
    return field;
  }

  @Override
  public Field getField(String name) {
    if (name.equals("CITY")) return new MyCityField();
    if (name.equals("PLACE")) return new MyPlaceField();
    if (name.equals("ID")) return new IdField("(?:\\d{6}|[A-Z]{2}\\d{2})-\\d{6}", true);
    if (name.equals("PRI")) return new PriorityField("(?:Non-)?Emergency|Special Assignment", true);
    if (name.equals("UNIT")) return new MyUnitField();
    if (name.equals("INFO")) return new MyInfoField();
    return super.getField(name);
  }

  private class MyCityField extends CityField {
    @Override
    public void parse(String field, Data data) {
      field = stripFieldStart(field, "(C)");
      super.parse(field, data);
    }
  }

  private class MyPlaceField extends PlaceField {
    @Override
    public void parse(String field, Data data) {
      data.strPlace = stripLeadAlert(field, data);
    }
    
    @Override
    public String getFieldNames() {
      return "ALERT PLACE";
    }
  }
  
  private static final Pattern CH_PTN = Pattern.compile("(.*)\\bOPS\\b.*|ADMIN .*");
  private class MyUnitField extends UnitField {
    @Override
    public void parse(String field, Data data) {
      field = stripFieldStart(field, "Units Assigned");
      if (CH_PTN.matcher(field).matches()) {
        data.strChannel = append(data.strChannel, "/", field);
      } else {
        super.parse(field, data);
      }
    }
  }
  
  private class MyInfoField extends InfoField {
    @Override
    public void parse(String field, Data data) {
      parseInfo(field, data);
    }
    
    @Override
    public String getFieldNames() {
      return "INFO GPS";
    }
  }

  private static final Pattern TRAIL_INFO_DATA = Pattern.compile("[, ]*((?:Non-)?Emergency|Routine|Self Initiated|Traffic Stop|(?:Alpha|Bravo|Charlie|Delta|Echo)-[AB]LS|Combo \\d) +(\\d{4,8}-\\d{6})(?: +(.*?))?$");
  private static final Pattern UNIT_X_PTN = Pattern.compile("((?: *\\b[A-Z]+\\d+\\b)+) *(.*)");
  private static final Pattern INFO_BRK_PTN = Pattern.compile("\\[1?\\d\\]");
  private static final Pattern INFO_GPS_PTN = Pattern.compile(".*\\bLAT: ([-+]?\\d{2,3}\\.\\d{6,}) LON: ([-+]?\\d{2,3}\\.\\d{6,})\\b.*");
  private static final Pattern INFO_JUNK_PTN = Pattern.compile("\\*+ADD'L Wireless Info : .*|Automatic Case Number\\(s\\) issued for Incident #.*|\\*+Class of Seri?vi?ce.*");

  private void parseInfo(String extra, Data data) {
    
    Matcher match = TRAIL_INFO_DATA.matcher(extra);
    if (match.find()) {
      extra = extra.substring(0, match.start());
      data.strPriority = getOptGroup(match.group(1));
      data.strCallId = match.group(2);
      String cross = getOptGroup(match.group(3));
      match = UNIT_X_PTN.matcher(cross);
      if (match.matches()) {
        data.strUnit = match.group(1).replace(' ', ',');
        cross = match.group(2);
      }
      data.strCross = cross;
    }
    
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
  
  @Override
  public String getProgram() {
    return super.getProgram() + " PRI ID UNIT CH X";
  }

  private static final String[] MWORD_STREET_LIST = new String[]{
    
      "ABNER CREEK",
      "ALAN KENT",
      "ALLENDALE ABBEY",
      "ALTA VISTA",
      "ANDERSON MILL",
      "ANDERSON RIDGE",
      "ANSEL SCHOOL",
      "ARBOR CREST",
      "ARBOR WOODS",
      "ARDMORE SPRINGS",
      "ASHBY CROSS",
      "ASHBY GROVE",
      "ASHMORE BRIDGE",
      "ASSEMBLY VIEW",
      "AUTUMN HILL",
      "AVALON CHASE",
      "AWANITA CAMP",
      "BAILEY CREEK",
      "BAILEY FALLS",
      "BAILEY MILL",
      "BALENTINE LAKE",
      "BAMBER GREEN",
      "BARN SWALLOW",
      "BARNETT VALLEY",
      "BARRED OWL",
      "BASS COVE",
      "BATES BRIDGE",
      "BATES CROSSING",
      "BATES VIEW",
      "BAY HILL",
      "BEAUMONT CREEK",
      "BEAVER CREEK",
      "BEAVER DAM",
      "BEAVER RUN",
      "BECKY DON",
      "BECKY GIBSON",
      "BEE TREE",
      "BEECH CLIFF",
      "BEECH SPRINGS",
      "BELGIAN BLUE",
      "BELLA CITTA",
      "BELLE MEADE",
      "BELUE CEMETERY",
      "BELUE MILL",
      "BELVUE SCHOOL",
      "BEN HAMBY",
      "BENT CREEK",
      "BENT TWIG",
      "BEREA FOREST",
      "BEREA MIDDLE SCHOOL",
      "BERRY GLEN",
      "BERRY MILL",
      "BERRY PINE",
      "BETHUEL CHURCH",
      "BIG FOOT",
      "BIG OAK",
      "BILL TIMMONS",
      "BLACK HAWK",
      "BLACKBERRY VALLEY",
      "BLAZING STAR",
      "BLUE LAKE",
      "BLUE RIDGE",
      "BLUE WALL",
      "BLUE WATER",
      "BLYTHE SHOALS",
      "BOB WHITE",
      "BOILING SPRINGS",
      "BONNIE WOODS",
      "BRANDI STARR",
      "BRIDGE VIEW",
      "BRIDLE PATH",
      "BRISTLE FERN",
      "BROAD VISTA",
      "BROADMAR FARMS",
      "BROCKMAN HILL",
      "BROCKMAN MCCLIMON",
      "BROKEN ARROW",
      "BROKEN PAST",
      "BROOK GLENN",
      "BROOK LAUREL",
      "BROOKE LEE",
      "BROOKS POINTE",
      "BRUSHY CREEK",
      "BUBBLING CREEK",
      "BUCHANAN RIDGE",
      "BUNKER HILL",
      "BUR OAK",
      "BURGESS SCHOOL",
      "BURL HOLLOW",
      "BURNING BUSH",
      "BURNT NORTON",
      "BUTLER SPRINGS",
      "CAESARS HEAD",
      "CALIBER RIDGE",
      "CALLAHAN MOUNTAIN",
      "CAMP CREEK",
      "CAMP WABAK",
      "CAMPBELL COVERED BRIDGE",
      "CAMPBELL MILL",
      "CANNIE CLARK",
      "CANYON CREST",
      "CARL KOHRT",
      "CARRIAGE PARK",
      "CARTER RUN",
      "CARTERS GREEN",
      "CASEY LEE",
      "CCC CAMP",
      "CEDAR BLUFF",
      "CEDAR CLIFF",
      "CEDAR CREEK",
      "CEDAR FALLS",
      "CEDAR GLENN",
      "CEDAR GROVE",
      "CEDAR LANE",
      "CEDAR PINES",
      "CEDAR SUMMIT",
      "CHANDLER CREEK",
      "CHAPMAN GROVE",
      "CHARING CROSS",
      "CHASTAIN HILL",
      "CHEROKEE VALLEY",
      "CHERRY BLOSSOM",
      "CHESTNUT MOUNTAIN",
      "CHESTNUT POND",
      "CHESTNUT RIDGE",
      "CHESTNUT WOODS",
      "CHICK SPRINGS",
      "CHRIST ROCK CAMP",
      "CHRISTMAS TREE",
      "CHURCH OF GOD",
      "CLARE BANK",
      "CLAY THORN",
      "CLEAR SPRINGS",
      "CLIMBING ROSE",
      "CLUB CART",
      "CLUB FOREST",
      "CLUB TERRACE",
      "COACH HILL",
      "COG HILL",
      "COLLINS MILLS",
      "COMMERCIAL AUGUSTA",
      "CONESTEE LAKE",
      "CONNORS CREEK",
      "COOL CREEK",
      "COOL MEADOW",
      "COOL RIVER",
      "COOL SPRINGS CHURCH",
      "COOLEY BRIDGE",
      "COTTAGE CREEK",
      "COUNTRY CLUB",
      "COUNTRY COVE",
      "COUNTRY CREEK",
      "COUNTRY MIST",
      "COUNTRY WALK",
      "CRAG RIVER",
      "CRAIGO CREEK",
      "CREPE MYRTLE",
      "CRESCENT RIDGE",
      "CRESTWOOD FOREST",
      "CRIMSON GLORY",
      "CRIPPLE CREEK",
      "CROSS HILL",
      "CROWN EMPIRE",
      "CROWN RIDGE",
      "CRYSTAL FALLS",
      "CUNNINGHAM POINT",
      "CYPRESS COVE",
      "DAVE GARRETT",
      "DE KALB",
      "DEAN CRAIN",
      "DEAN WILLIAMS",
      "DEAN WOODS",
      "DEER RUN",
      "DEL NORTE",
      "DEVILS FORK",
      "DIVIDING WATER",
      "DOINA CREEK",
      "DOLCE VITA",
      "DRIFT AWAY",
      "DRY POCKET",
      "DUCK POND",
      "DUFFS MOUNTAIN",
      "DUG HILL",
      "DUNCAN CHAPEL",
      "DUNHAM BRIDGE",
      "DUNKLIN BRIDGE",
      "EAGLE CREEK",
      "EAGLES CREST",
      "EASLEY BRIDGE",
      "EASTON MEADOW",
      "EBENEZER CHURCH",
      "EDWARDS MILL",
      "EL CENTRO",
      "ELLEN THOMPSON",
      "ELSTAR LOOP",
      "ENCLAVE PARIS",
      "ENOREE FARM",
      "ESTES PLANT",
      "EUROPEAN PLUM",
      "EXECUTIVE CENTER",
      "FAIR CREEK",
      "FAIRVIEW CHURCH",
      "FALLOUT SHELTER",
      "FARRS BRIDGE",
      "FAWN CREEK",
      "FAWN HILL",
      "FEWS BRIDGE",
      "FEWS CHAPEL",
      "FIRE PINK",
      "FIVE FORK PLAZA",
      "FIVE FORKS",
      "FLAT ROCK",
      "FLORAL WOOD",
      "FOOT HILLS",
      "FOREST LAKE",
      "FORK SHOALS",
      "FORKED OAK",
      "FORKSVILLE CHURCH",
      "FORREST HAVEN",
      "FOUR OAK",
      "FOX CREEK",
      "FOX FARM",
      "FOX RIDGE",
      "FURMAN HALL",
      "FURMAN MALL",
      "GAP CREEK",
      "GARDEN MARKET",
      "GARDEN SPRING",
      "GARY ARMSTRONG",
      "GIBSON OAKS",
      "GLASSY FALLS",
      "GLASSY MOUNTAIN FARMS",
      "GLASSY MOUNTAIN",
      "GLASSY MOUNTAINN",
      "GLASSY RIDGE",
      "GLENN OAKS",
      "GLOBAL COMMERCE",
      "GLOUCESTER FERRY",
      "GOLDEN AMBER",
      "GOLDEN STRIP",
      "GOODWIN BRIDGE",
      "GOWENSVILLE CHURCH",
      "GRACEFUL SEDGE",
      "GRAY FOX",
      "GREAT GLEN",
      "GREEN BANK",
      "GREEN VALLEY",
      "GREER TOWN",
      "GREY HAWK",
      "GRIFFIN MILL",
      "GROCE MEADOW",
      "GROUSE RIDGE",
      "GROVE RESERVE",
      "GSP LOGISTICS",
      "GUM SPRINGS CHURCH",
      "GUM SPRINGS",
      "HAMMETT BRIDGE",
      "HARRISON BRIDGE",
      "HART CUT",
      "HAWK KNOB",
      "HAWK NEST",
      "HAWK SPRINGS",
      "HAWK VALLEY",
      "HAWKINS HILL",
      "HAWTHORNE PARK",
      "HERITAGE WOODS",
      "HIDDEN HILLS",
      "HIDDEN SPRINGS",
      "HIGH HAT",
      "HIGH PEAK",
      "HILLSIDE CHURCH",
      "HITCHING POST",
      "HOGBACK MOUNTAIN",
      "HOKE SMITH",
      "HOLIDAY DAM",
      "HOLLY HILL",
      "HOLLY SPRINGS",
      "HOMES POND",
      "HUFF CREEK",
      "HUGH SMITH",
      "HUNTERS LANDING",
      "HUNTS BRIDGE",
      "HWY 25",
      "INDIAN RIDGE",
      "INDIAN SPRINGS",
      "IRISH OAKS",
      "IVY WOODS",
      "J VERNE SMITH",
      "J WALTER MOON",
      "JACKSON GROVE",
      "JAMES WALKER",
      "JAMISON MILL",
      "JENKINS BRIDGE",
      "JENNINGS MILL",
      "JOHN SUDDETH",
      "JOHN THOMAS",
      "JONES GAP",
      "JONES KELLEY",
      "JORDAN EBENEZER",
      "JUG FACTORY",
      "KEELER BRIDGE",
      "KEELER MILL",
      "KELSEY GLEN",
      "KERMIT WATSON",
      "KERSEY GALE",
      "KEY HOLLOW",
      "KNIGHTS SPUR",
      "KNOB CREEK",
      "LA PLATA",
      "LA ROSA",
      "LAKE CUNNINGHAM",
      "LAKE RIDGE",
      "LAKE SHORE",
      "LAND GRANT",
      "LANDRUM CREEK",
      "LATIMER MILL",
      "LATIMER RIDGE",
      "LAUGHING TREE",
      "LAUREN KAY",
      "LAUREN LEIGH",
      "LAUREN WOOD",
      "LEE VAUGHN",
      "LEMON CREEK",
      "LENHARDT GROVE",
      "LEXINGTON PLACE",
      "LIBERTY CHURCH",
      "LIMA BAPTIST CHURCH",
      "LINCOLN CHAPEL",
      "LINDSEY BRIDGE",
      "LINDSEY HILL",
      "LINDSEY LAKE",
      "LINDSEY RIDGE",
      "LIONS CLUB",
      "LIONS PARK",
      "LISMORE PARK",
      "LITTLE CREEK",
      "LITTLE MIKE",
      "LITTLE RIO",
      "LITTLE TEXAS",
      "LOCUST HILL",
      "LOFTY RIDGE",
      "LOG SHOALS",
      "LONE TREE",
      "LONESOME DOVE",
      "LONG SHOALS",
      "LOOK OFF",
      "LOOK UP LODGE",
      "LORD BYRON",
      "LOST LAKE",
      "LOST SWAMP",
      "LUCILLE SMITH",
      "MADDOX BRIDGE",
      "MADISON HAVEN",
      "MAGNOLIA MEADOW",
      "MAGNOLIA PLACE",
      "MANLEY CRAIN",
      "MAPLE CREEK",
      "MAPLE CREST",
      "MAPLE LAKE",
      "MAPLE TREE",
      "MAPLESTEAD FARMS",
      "MARKED BEECH",
      "MARY CAMILLA JUDSON",
      "MARY PEACE STERLING",
      "MASON FARM",
      "MATTHEWS CREEK",
      "MATTIE CAMPBELL",
      "MAULDIN FARMS",
      "MAYS BRIDGE",
      "MCCULLOUGH SCHOOL",
      "MCKITTRICK BRIDGE",
      "MEADOW CREEK",
      "MEADOW FIELD",
      "MEADOW FORK",
      "MEADOW TREE",
      "MEADOW WOOD",
      "MILFORD CHURCH",
      "MILFORD MALL",
      "MILL PARK",
      "MILL VALLEY",
      "MISTY CREST",
      "MISTY GATE",
      "MISTY MOUNTAIN",
      "MITER SAW",
      "MONROE BRIDGE",
      "MOODY BRIDGE",
      "MOON ACRES",
      "MORGAN WOODS",
      "MOTOR BOAT CLUB",
      "MOUNT LEBANON CHURCH",
      "MOUNTAIN CREEK CHURCH",
      "MOUNTAIN CREEK",
      "MOUNTAIN CREST",
      "MOUNTAIN HEIGHT",
      "MOUNTAIN LAKE",
      "MOUNTAIN SLOPE",
      "MOUNTAIN VIEW SCHOOL",
      "MOUNTAIN VIEW",
      "MT LEBANON CHURCH",
      "MUSH CREEK HILL",
      "MUSH CREEK",
      "NASH MILL",
      "NEELY MILL",
      "NOBSKA LIGHT",
      "NOTRE DAME",
      "OAK CREST",
      "OAK FOREST",
      "OAK GROVE",
      "OAK HAVEN",
      "OAK HILL",
      "OAK PARK",
      "OAK RIDGE",
      "OAK WIND",
      "OIL CAMP CREEK",
      "OLDE ORCHARD",
      "OLIVER SCOTTS MILL",
      "ONEAL CHURCH",
      "ORCHARD CREST",
      "PACE BRIDGE",
      "PACKS MOUNTAIN RIDGE",
      "PACKS MOUNTAIN",
      "PALM SPRINGS",
      "PARIS CREEK",
      "PARIS MOUNTAIN",
      "PARIS VIEW",
      "PARK GREEN",
      "PARK PLACE",
      "PARK VISTA",
      "PARKER CONE",
      "PARNELL BRIDGE",
      "PATROL CLUB",
      "PEACH ORCHARD",
      "PEACH RIDGE",
      "PEACH VALLEY",
      "PEARLE BROOK",
      "PEARSON LAKE",
      "PEBBLE CREEK",
      "PECAN GROVE",
      "PELHAM COMMONS",
      "PELHAM PARK",
      "PELHAM SQUARE",
      "PERSIMMON RIDGE",
      "PETER MCCORD",
      "PHILLIPS MCCALL",
      "PIEDMONT PARK",
      "PILGRIMS POINT",
      "PINE CREEK",
      "PINE CREST",
      "PINE FOREST",
      "PINE KNOLL",
      "PINE LAKE",
      "PINE LOG FORD",
      "PINE NEEDLE",
      "PINE RIDGE",
      "PINE THICKET",
      "PINE VIEW",
      "PINEY MOUNTAIN",
      "PINK CAMPBELL",
      "PINK DILL MILL",
      "PINNACLE LAKE",
      "PIPE LINE",
      "PLACID FOREST",
      "PLANTERS ROW",
      "PLAYER ESTATES",
      "PLEASANT BROOK",
      "PLEASANT GROVE",
      "PLEASANT HILL",
      "PLEASANT RETREAT",
      "PLUMLEY SUMMIT",
      "POINSETT COMMON",
      "PONDERS RAY",
      "POPLAR VALLEY",
      "POWERS GARDEN",
      "PREWETTE HILL",
      "QUAIL HOLLOW",
      "QUAIL RUN",
      "RABON CHASE",
      "RED HILL",
      "RED HOLLY",
      "RED ROBIN",
      "REEDY FORK",
      "REEDY POINTE",
      "REEDY SPRINGS",
      "REGENCY HILL",
      "REID SCHOOL",
      "RENAISSANCE ROW",
      "REQUEST JOHNS",
      "REVIS CREEK",
      "RIDGE PASS",
      "RIDGE ROCK",
      "RILEY SMITH",
      "RIMMON HILL",
      "RIO VISTA",
      "RIVER BIRCH",
      "RIVER FALLS LODGE",
      "RIVER FALLS",
      "RIVER MEADOWS",
      "RIVER OAKS",
      "RIVER ROCK",
      "RIVERSIDE CHASE",
      "ROBERTS HILL",
      "ROBIE ANN",
      "ROBIN HOOD",
      "ROCK QUARRY",
      "ROCK SIDE",
      "ROCKY CREEK",
      "ROCKY KNOLL",
      "ROCKY TOP",
      "ROE CENTER",
      "ROE FORD",
      "ROPER MOUNTAIN",
      "ROSE PINK",
      "RUDDY CREEK",
      "RUSTY BROOK",
      "RUTLEDGE LAKE",
      "RYDER CUP",
      "SAGE GLEN",
      "SALLY GILREATH",
      "SALUDA HILL CHURCH",
      "SALUDA LAKE",
      "SAM LANGLEY",
      "SANDY FLAT",
      "SAW MILL",
      "SCOTTS BLUFF",
      "SEYLE ST",
      "SHADY OAK",
      "SHANNON LAKE",
      "SHILOH BEND",
      "SHORT BRANCH",
      "SILVER CREEK",
      "SILVER SHOALS",
      "SIMPLY LESS",
      "SINGLE OAK",
      "SKY RANCHE",
      "SLATTON SHOALS",
      "SLEEPY RIVER",
      "SLIDING ROCK",
      "SMITH TRACTOR",
      "SMYTHE MILL",
      "SOUTH END",
      "SOUTHERN PINE",
      "SPARROW HAWK",
      "SPENCER CREEK",
      "SPRING FELLOW",
      "SPRING FOREST",
      "SPRING PARK",
      "SQUIRES CREEK",
      "ST ANDREWS",
      "ST AUGUSTINE",
      "ST CHARLES",
      "ST FRANCIS",
      "ST LUKE METHODIST CHURCH",
      "ST MARK",
      "STAMEY VALLEY",
      "STANDING SPRINGS",
      "STAPLEFORD PARK",
      "STATE PARK",
      "STAUNTON BRIDGE",
      "STEEL CITY",
      "STEPHEN GARRETT",
      "STERLING GROVE",
      "STEWART HILL",
      "STEWART LAKE",
      "STONE DALE",
      "STONE MEADOW",
      "STREAM RUN",
      "SUGAR OAK",
      "SULPHUR SPRINGS",
      "SUNNY VALLEY",
      "SUNSET GLORY",
      "SUNSET MAPLE",
      "SUNSET MOUNTAIN",
      "SUTHERLAND HILL",
      "SWAMP RABBIT",
      "SWEET GUM VALLEY",
      "SWEET JULIET",
      "SWEET WATER",
      "SWEET WILLIAM",
      "TABLE ROCK STATE PARK",
      "TABLE ROCK",
      "TALL BIRCH",
      "TALL PINES",
      "TALLEY BRIDGE",
      "TEA OLIVE",
      "TERRA TRACE",
      "TERRAPIN CROSS",
      "TERRY CREEK",
      "TERRY SHOP",
      "TEX MCCLURE",
      "THE CLIFFS",
      "THOMAS EDWARDS",
      "THORN RIDGE",
      "THREE OAK",
      "TIGER BRIDGE",
      "TIGERVILLE ELEMENTARY SCHOOL",
      "TIMBER GLEN",
      "TIMBER ROCK",
      "TOUCH ME NOT",
      "TRAMMELL MOUNTAIN",
      "TREATY POINT",
      "TRIDENT MAPLE",
      "TRILLIUM CREEK",
      "TRIPLE CREEK",
      "TROTTERS FIELD",
      "TUBBS MOUNTAIN",
      "TURKEY CREEK",
      "TURNER BARTON",
      "TURNING LEAF",
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
      "WAKE FOREST",
      "WAKE HILL",
      "WALNUT CREST",
      "WALNUT TRACE",
      "WAR ADMIRAL",
      "WATERS REACH",
      "WATKINS BRIDGE",
      "WATSON MOUNTAIN",
      "WAYCROSS CHURCH",
      "WEBB CREEK",
      "WHITE EMPRESS",
      "WHITE HORSE",
      "WHITE OAK",
      "WICKS CREEK",
      "WILD DOGWOOD",
      "WILD GEESE",
      "WILD ORCHARD",
      "WILD TURKEY",
      "WILKES LEE",
      "WILL CENTER",
      "WILLIAM MCALISTER",
      "WILLIAM SETH",
      "WILLOW CREEK",
      "WILLOW FALLS",
      "WILSON BRIDGE",
      "WINDING ROCK",
      "WINDSOR CREEK",
      "WINDWARD PEAK",
      "WINDY BLUFF",
      "WINDY OAK",
      "WINSTONS CHASE",
      "WONDERLAND RANGE",
      "WOOD HAYES",
      "WOODLAND CHASE",
      "WOODLAND HILLS",
      "WOODS CHAPEL",
      "WOODS EDGE",
      "WOODS LAKE",
      "YOUTH CAMP"
  };

  private static final CodeSet CALL_LIST = new CodeSet(
      "2ND ALARM - FULLY INVOLVED STRUCTURE FIRESTRUCT FIRE RESI SINGLE FAMILY",
      "ABDOMINAL PAIN",
      "ABDOMINAL PAIN_D1",
      "ABNORMAL BEHAVIOR",
      "AIRCRAFT EMERGENCY UNKNOWN",
      "ALARM CARBON MONOXIDE",
      "ALARM COMMERCIAL",
      "ALARM HIGH LIFE HAZARD",
      "ALARM HIGH RISE",
      "ALARM MULTI OCCUPANCY",
      "ALARM RESIDENTIAL",
      "ALLERGIC REACTION",
      "ALLERGIES/ENVENOMATIONS",
      "ALLERGIES/ENVEN_B1",
      "ANIMAL BITE",
      "ANIMAL BITE/ATTACK",
      "ASSAULT",
      "ASSAULT/SEXUAL ASSAU",
      "ASSAULT/SEXUAL ASSAULT",
      "AUTO/PEDESTRIAN_D2-M",
      "BACK PAIN",
      "BACK PAIN (NON TRAUMATIC)",
      "BREATHING PROB_C1",
      "BREATHING PROB_C2",
      "BREATHING PROB_D1",
      "BREATHING PROB_D2",
      "BREATHING PROB_E1",
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
      "CHEST PAIN/DISCOMFOR",
      "CHOKING",
      "CITIZEN ASSIST",
      "CITIZEN ASSIST/SERVI",
      "CITIZEN ASSIST/SERVI",
      "CITIZEN ASSIST/SERVICE CALL",
      "CO INHALATION HAZMAT",
      "CO/INHALATION/HAZMAT",
      "CONFINED SPACE/STRUCT COLLAPSE(A)",
      "CONVULSIONS/SEIZURES",
      "DIABETIC PROBLEMS",
      "DROWNING OR DIVING INCIDENT",
      "DROWNING/DIVING ACCI",
      "DUMPSTER RUBBISH FIRE",
      "ELECTRICAL HAZARD",
      "ELECTROCUTION/LIGHTN",
      "ELEVATOR RESCUE",
      "ELEVATOR/ESCALATOR RESCUE",
      "EMS STANDBY",
      "EMS STANDBY REQUEST",
      "EMS STANDBY REQUESTE",
      "EXPLOSION",
      "EYE PROBLEM/INJURY",
      "FALL_A2",
      "FALL_B1",
      "FALL_D3",
      "FALL_D4",
      "FALLS",
      "FUEL SPILL",
      "FUEL SPILL/FUEL ODOR",
      "GAS LEAK",
      "GAS LEAK/GAS ODOR",
      "GSW/PENETRATING_B5-G",
      "GSW/PENETRATING_D1-G",
      "GSW/PENETRATING_D4-G",
      "GSW/PENETRATING_D6-G",
      "HAZMAT",
      "HEADACHE",
      "HEART PROBLEM_D1",
      "HEART PROBLEM_D2",
      "HEART PROBLEM_D3",
      "HEART PROBLEM_D4",
      "HEART PROBLEMS",
      "HEAT OR COLD EXPOSURE",
      "HEAT/COLD EXPOSURE",
      "HEMORRHAGE LACERATION",
      "HEMORRHAGE/LACERATIO",
      "HEMORRHAGE/LACERATION",
      "HIGH ANGLE RESCUE",
      "INACCESSIBLE ENTRAPMENT",
      "INACCESSIBLE/ENTRAPMENT",
      "LIGHTNING STRIKE",
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
      "SEIZURE_B1",
      "SEIZURE_C4",
      "SEIZURE_D2",
      "SEIZURES",
      "SERVICE CALL",
      "SICK PERSON",
      "SICK PERSON_A",
      "SICK PERSON_B",
      "SICK PERSON_C",
      "SICK PERSON_D",
      "SMOKE INVESTIGATION",
      "SMOKE INVESTIGATION OUTSIDE",
      "SPECIAL ASSIGNMENT",
      "STAB/GSW/PENETRATIN",
      "STAB/GSW/PENETRATING",
      "STAB/GSW/PENETRATING INJURY",
      "STABBING OR GSW",
      "STANDBY: LAW",
      "STANDBY: STRUCTURE F",
      "STROKE",
      "STROKE_C1",
      "STROKE/TIA",
      "STRUCT FIRE HIGH LIFE HAZARD",
      "STRUCT FIRE RESI MUL",
      "STRUCT FIRE RESI MULTI FAMILY",
      "STRUCT FIRE RESI SINGLE FAMILY",
      "STRUCTURE FIRE COMMERCIAL",
      "TRAFFIC INCIDENT",
      "TRAFFIC_TRAFFIC STOP",
      "TRAFFIC/TRANSPORT",
      "TRAFFIC/TRANSPORT IN",
      "TRAFFIC/TRANSPORT INCIDENT",
      "TRAUMATIC INJURY",
      "UNCONSCIOUS",
      "UNCONSCIOUS/FAINTING",
      "UNKNOWN PROBLEM",
      "UNKNOWN PROBLEM_B2",
      "UNKNOWN PROBLEM_B3",
      "UNKNOWN PROBLEM_D1",
      "VEHICLE FIRE",
      "VEHICLE FIRE COMM/BOX/MOT HOME",
      "VEHICLE FIRE COMMERCIAL",
      "WATER RESCUE/SINKING VEHICLE",
      "WATERCRAFT EMERG/COLLISION",
      "WILDLAND FIRE",
      "ZTP_ACCIDENT_HIT AND RUN",
      "ZTP_ACCIDENT_NO INJURIES",
      "ZTP_ALARM_AUDIBLE",
      "ZTP_ALARM_COMMERCIAL SILENT",
      "ZTP_ALARM_RESIDENTIAL PANIC",
      "ZTP_ANIMAL_ANIMAL BITE",
      "ZTP_ANIMAL_LOOSE",
      "ZTP_ASSAULT",
      "ZTP_BREAKING AND ENTERING BURG",
      "ZTP_BUSINESS CHECK",
      "ZTP_CHECK_WELFARE CHECK",
      "ZTP_CODES ENFORCEMENT",
      "ZTP_DISTURBANCE",
      "ZTP_DOMESTIC_VERBAL",
      "ZTP_EMS REQ ASSISTANCE",
      "ZTP_ESCORT_ADULT",
      "ZTP_FIRE_CALL",
      "ZTP_FRAUD_FORGERY/DECEIT",
      "ZTP_HELP_ROUTINE ASSIST",
      "ZTP_LARCENY_PETIT LARCENY",
      "ZTP_MINOR IN POSS TOBACCO",
      "ZTP_NOISE_LOUD MUSIC",
      "ZTP_NON CRIMINAL SERVICE",
      "ZTP_OFFICER_EXTRA PTRL BUS/RES",
      "ZTP_OFFICER_FOLLOW UP",
      "ZTP_OFFICER_REPORT TO",
      "ZTP_ROADWAY_OBSTRUCTION",
      "ZTP_SEE COMPLAINANT",
      "ZTP_SEXUAL_OTHER",
      "ZTP_SHOPLIFTING",
      "ZTP_SUSPICIOUS_PERSON",
      "ZTP_TRAFFIC_DUI",
      "ZTP_TRAFFIC_RECKLESS DRIVER",
      "ZTP_TRAFFIC_TRAFFIC STOP",
      "ZTP_TRAFFIC_WORKING RADAR AT LN",
      "ZTP_TRAFFIC_WORKING RADAR AT LS",
      "ZTP_VEHICLE_STRANDED MOTORIST",
      "ZTP_VEHICLE_SUSPICIOUS"
 );
}
