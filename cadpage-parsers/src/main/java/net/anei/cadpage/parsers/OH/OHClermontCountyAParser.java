package net.anei.cadpage.parsers.OH;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Properties;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.anei.cadpage.parsers.CodeSet;
import net.anei.cadpage.parsers.MsgInfo.Data;
import net.anei.cadpage.parsers.dispatch.DispatchB3Parser;

/**
 * Clermont county, OH
 */
public class OHClermontCountyAParser extends DispatchB3Parser {

  private static final Pattern RETURN_PHONE_PTN = Pattern.compile("(Return Phone: \\d{10}) +(.*)");

  public OHClermontCountyAParser() {
    super(CITY_LIST, "CLERMONT COUNTY", "OH", B2_OPT_CALL_CODE | B2_CROSS_FOLLOWS);
    setupCallList(CALL_LIST);
    setupMultiWordStreets(MWORD_STREET_LIST);
  }

  @Override
  public String getFilter() {
    return "911-CENTER@clermontcountyohio.gov,911@clermontcountyohio.gov,777,888";
  }

  @Override
  public boolean parseHtmlMsg(String subject, String body, Data data) {
    if (body.startsWith("<table")) return false;
    return super.parseHtmlMsg(subject, body, data);
  }

  @Override
  public boolean parseMsg(String subject, String body, Data data) {

    if (subject.startsWith("Alert:")) return false;
    if (body.startsWith("LOC:")) return false;

    boolean mark = body.startsWith("911-CENTER:");
    if (mark) body = body.substring(11).trim();

    // Check for return phone message pattern
    Matcher match = RETURN_PHONE_PTN.matcher(body);
    if (match.matches()) {
      setFieldList("CALL ADDR APT CITY INFO");
      data.strCall = match.group(1);
      data.strSupp = match.group(2);
      Result res = parseAddress(StartType.START_OTHER, FLAG_IGNORE_AT, data.strSupp);
      if (res.isValid()) res.getData(data);
      return true;
    }

    if (subject.equals("Text Message")) subject = "";
    else if (body.startsWith(subject)) subject = "";
    if (!super.parseMsg(subject, body, data) &&
        !(mark && parseFallback(body, data))) return false;
    if (data.strCity.length() == 0) {
      if (data.strName.equals("WARCO COMM")) data.strCity = "WARREN COUNTY";
    }
    if (data.strCity.endsWith(" CO")) data.strCity += "UNTY";
    if (KY_CITY_SET.contains(data.strCity)) data.strState = "KY";
    return true;
  }

  private static final Pattern START_UNIT_PTN = Pattern.compile("([A-HJ-Z]\\d+|STA ?\\d+) +");
  private static final Pattern RESPOND_TO_PTN = Pattern.compile("(?:(.*?) )?RESPOND (?:TO )?(.*?)(?: IN (.*?))?(?: FOR (.*))?");
  private boolean parseFallback(String body, Data data) {
    setFieldList("UNIT CALL ADDR APT CITY INFO");
    data.initialize(this);

    Matcher match = START_UNIT_PTN.matcher(body);
    if (match.lookingAt()) {
      data.strUnit = match.group(1);
      body = body.substring(match.end());
    }

    match = RESPOND_TO_PTN.matcher(body);
    if (match.matches()) {
      data.strCall = getOptGroup(match.group(1));
      String addr = match.group(2).trim();
      String city = match.group(3);
      String call = match.group(4);
      if (city != null) data.strCity = convertCodes(city.trim(), CITY_ABRV_TABLE);
      if (call != null) data.strCall = append(data.strCall, " - ", call);
      int flags = 0;
      if (call != null) flags = FLAG_ANCHOR_END;
      if (city != null) flags = (FLAG_NO_CITY | FLAG_ANCHOR_END);
      parseAddress(StartType.START_ADDR, flags, addr, data);
      if (data.strCall.length() == 0) data.strCall = getLeft();
      else data.strSupp = getLeft();
    } else {
      parseAddress(StartType.START_CALL, FLAG_START_FLD_REQ, body, data);
      data.strSupp = getLeft();
    }
    return data.strCall.length() > 0 && data.strAddress.length() > 0;
  }

  @Override
  public String getProgram() {
    return super.getProgram().replace("CITY", "CITY ST");
  }

  @Override
  protected boolean isPageMsg(String body) {
    return body.contains(" Cad:") || body.contains("CAD#") && !body.startsWith("EVENT: ");
  }

  @Override
  protected boolean parseAddrField(String field, Data data) {
    // Mistyped AT mistaken for ATT carrier name :(
    field = field.replace(" ATT ", " AT ");
    return super.parseAddrField(field, data);
  }

  @Override
  public String adjustMapCity(String city) {
    return convertCodes(city, CITY_MAP_TABLE);
  }

  private static final String[] MWORD_STREET_LIST = new String[]{
      "AMBER HILL",
      "AMELIA OLIVE BRANCH",
      "AMELIA PARK",
      "APPLE FARM",
      "ARNOLD PALMER",
      "BACH BUXTON",
      "BACH GROVE",
      "BACK RUN",
      "BARDWELL WEST",
      "BARRICK LOW",
      "BATAVIA MEADOWS",
      "BAY MEADOW",
      "BEAR CREEK",
      "BEECH GROVE",
      "BEES RUN",
      "BELFAST OWENSVILLE",
      "BERT REED MEMORIAL",
      "BETHEL CONCORD",
      "BETHEL HYGIENE",
      "BETHEL NEW RICHMOND",
      "BETHEL PARK",
      "BIG INDIAN",
      "BLACK WATCH",
      "BLUE RIDGE",
      "BLUE SKY PARK",
      "BOOTJACK CORNER",
      "BRANCH HILL GUINEA",
      "BRANCH HILL MIAMIVILLE",
      "BRANCH HILL-LOVELAND",
      "BRIGHT WATER",
      "BRISTOL LAKE",
      "BROOKS MALOTT",
      "BRUSHY FORK",
      "CAIN RUN",
      "CASTLE BAY",
      "CAUDILL EAST",
      "CEDAR LAKE",
      "CEDAR RIDGE",
      "CHARLES SNIDER",
      "CHARTER OAK",
      "CHESTNUT VIEW",
      "CLERMONT CENTER",
      "CLERMONT COLLEGE",
      "CLERMONT MEADOW",
      "CLERMONTVILLE LAUREL",
      "CLUB HOUSE",
      "COLONEL MOSBY",
      "COMMONS CIRCLE",
      "COUNTRY LAKE",
      "COUNTRY VIEW",
      "COUNTY PARK",
      "CRANE SCHOOLHOUSE",
      "CREEK KNOLL",
      "CROSS CREEK",
      "CUTTY SARK",
      "DELA PALMA",
      "DICK FLYNN",
      "DONNA JAY",
      "DRY RUN",
      "EAGLE RIDGE",
      "EAST BAUMAN",
      "EAST FILAGER",
      "EAST FORK HILLS",
      "EAST FORK",
      "EAST MEADOW",
      "EDENTON PLEASANT PLAIN",
      "EL REGO",
      "ELM CORNER",
      "ELSTON HOCKSTOCK",
      "FAGINS RUN",
      "FAIR OAK",
      "FALLEN TREE",
      "FALLING WOOD",
      "FAYETVILLE BLANCHESTER",
      "FELICITY CEDRON RURAL",
      "FOX HUNT",
      "FOX RIDGE",
      "FRANK WILLIS MEMORIAL",
      "FRANKLIN LAUREL",
      "FRANKLIN MEADOWS",
      "FREE SOIL",
      "FRUIT RIDGE",
      "GALLEY HILL",
      "GARRISON SPURLING",
      "GLANCY CORNER MARATHON",
      "GLEN ECHO",
      "GOLDEN AGE",
      "GOLDEN MEADOW",
      "GOODWIN SCHOOLHOUSE PT ISA",
      "GREEN TREE",
      "GREENBUSH WEST",
      "HAGEMANS CROSSING",
      "HAL COR",
      "HALF ACRE",
      "HALF HILL",
      "HAPPY HOLLOW",
      "HARRY HILL",
      "HAW TREE",
      "HEALTH PARTNERS",
      "HEATHER HILL",
      "HEDGE ROW",
      "HENNINGS MILL",
      "HERITAGE WOODS",
      "HICKORY PARK",
      "HICKORY THICKET",
      "HICKORY WOOD",
      "HICKORY WOODS",
      "HIDDEN GREEN",
      "HIGH OAK",
      "HILL STATION",
      "HOLLOW CREEK",
      "HOME WOOD",
      "HOPEWELL SPUR",
      "HOPPER HILL FARMS",
      "HOPPER HILL",
      "HOPPER RIDGE",
      "HUNTING CREEK",
      "IDLETT HILL",
      "INDIAN HILL",
      "INDIAN MOUND",
      "IRETON TREES",
      "IRWIN CEM ETERY",
      "IRWIN CEMETERY",
      "ISLAND TRAIL",
      "IVY FARM",
      "JACOB LIGHT",
      "JAMES E SAULS SR",
      "JENNY LIND",
      "JERRY LEE",
      "JESS THELMA",
      "JETT HILL",
      "JO ELLEN",
      "JONES FLORER",
      "KEEVER SCHOOLHOUSE",
      "KENNEDY FORD",
      "LAKE PINE",
      "LANTERN POST",
      "LAUREL FARMS",
      "LAUREL LINDALE",
      "LAUREL MOSCOW",
      "LAUREL POINT ISABEL",
      "LAUREL PT ISABEL",
      "LAURENS RIDGE",
      "LAYCOCK CUTOFF",
      "LEGEND OAKS",
      "LEGENDARY TRAILS",
      "LIBERTY WOODS",
      "LIGHTS POINTE",
      "LINDALE MT HOLLY",
      "LINDALE NICHOLSVILLE",
      "LINDEN CREEK",
      "LINK SIDE",
      "LITTLE CREEK",
      "LITTLE INDIAN",
      "LITTLE RIVER",
      "LOCUST CORNER",
      "LOCUST HILL",
      "LOCUST LAKE",
      "LONG DRIVE",
      "LONG LEAF",
      "LOST LAKE",
      "LOVELAND MIAMIVILLE",
      "LUCY RUN CEMETERY",
      "LUCY RUN",
      "MAPLE CREEK",
      "MARATHON EDENTON",
      "MEADOW GREEN",
      "MEADOW VIEW",
      "MERWIN TEN MILE",
      "MIL HAVEN",
      "MISS ROYAL PASS",
      "MONASSAS RUN",
      "MONTEREY MAPLE GROVE",
      "MOORE MARATHON",
      "MOSCOW CEMETERY",
      "MT HOLLY",
      "MT OLIVE POINT ISABEL",
      "MT OLIVE PT ISABEL",
      "MT PISGAH",
      "MT VERNON",
      "MT ZION",
      "NEVER REST",
      "NEVILLE PENN SCHOOLHOUSE",
      "NEWTONSVILLE HUTCHINSON",
      "NINE MILE TOBASCO",
      "NORFOLK PINE",
      "NORTH BAY",
      "NUMBER 5",
      "NUMBER 9",
      "O BANNONVILLE",
      "OAK BARK",
      "OAK CORNER",
      "OAK DALE",
      "OAK LAND",
      "OAK TREE",
      "OAKLAND FARM",
      "OAKLAND HILLS",
      "OLIVE BRANCH STONELICK",
      "ORCHARD LAKE",
      "OTTER CREEK",
      "PADDLE WHEEL",
      "PAR FORE",
      "PEACH ORCHARD",
      "PEBBLE BROOKE",
      "PIN OAK",
      "PINE BRIDGE",
      "PINE VALLEY",
      "PINE VIEW",
      "PLEASANT ACRES",
      "PLEASANT VIEW",
      "POND RUN",
      "QUAIL RIDGE",
      "QUARRY CREEK",
      "QUARTER HORSE",
      "RAPID RUN",
      "RED BUD",
      "REDBIRD MEADOWS",
      "RIVER VALLEY",
      "ROBIN HILL",
      "ROLLING HILLS",
      "ROLLING KNOLL",
      "ROTH RIDGE",
      "SALTAIR CROSSING",
      "SAND TRAP",
      "SANTA MARIA",
      "SAWGRASS RIDGE",
      "SHADOW HILL",
      "SHADOW LAWN",
      "SHADY GLEN",
      "SHADY HOLLOW",
      "SHALLOW CREEK",
      "SHARPS CUTOFF",
      "SHORT HILL",
      "SMITH LANDING",
      "SOUTH BANTAM",
      "SOUTH RIVERSIDE",
      "SOUTH TIMBER CREEK",
      "SOUTHERN HILLS",
      "SPRING GROVE",
      "ST ANDREWS",
      "ST LOUIS",
      "STANTON HALL",
      "STILL WATER",
      "STONE FOX",
      "STONELICK CREEK",
      "STONELICK WILLIAMS CORNER",
      "STONELICK WOODS",
      "STUMPY LANE",
      "SUGAR CAMP",
      "SULPHUR SPRINGS",
      "SULPHYR SPRINGS",
      "SWINGS CORNER PT ISABEL",
      "TALL OAKS",
      "TALL TREES",
      "TAYLOR LANE",
      "TECH VALLEY",
      "TECHNE CENTER",
      "TEN MILE",
      "THORNY RIDGE",
      "TIB DAY",
      "TODDS RUN FOSTER",
      "TRAVERSE CREEK",
      "TRI RIDGE",
      "TRIPLE 2 FARMS",
      "TWELVE MILE",
      "TWIN BRIDGES",
      "TWIN SPIRES",
      "VALLEY FORGE",
      "VINEYARD HILLS",
      "VINEYARD WOODS",
      "VISTA MEADOWS",
      "WARDS CORNER",
      "WATKINS HILL",
      "WES CURT",
      "WEST FORK RIDGE",
      "WEST HANNA",
      "WEST HOLLY",
      "WHISPERING TREES",
      "WHISPERING WILLOW",
      "WHITE OAK",
      "WILD ROSE",
      "WILL O EE",
      "WILLIAMSBURG BANTAM",
      "WILMINGTON WOODVILLE",
      "WILSON DUNHAM HILL",
      "WINTER HOLLY",
      "WOLFPEN PLEASANT HILL",
      "WOOD COVE",
      "WOODBURY GLEN",
      "WOODED RUN",
      "WOODLAND PARK"

  };

  private static final CodeSet CALL_LIST = new CodeSet(
      "ABDOMINAL PAIN/PROBLEMS",
      "ALLERGIES/ENVENOMATIONS",
      "ANIMAL BITES/ATTACKS",
      "APPLIANCE FIRE",
      "ASSAULT/SEXUAL ASSAULT",
      "BACK PAIN/NON,NO RECENT TRAUMA",
      "BOAT ACCIDENT",
      "BURNS (SCALDS)/EXPLOSION",
      "BREATHING PROBLEMS",
      "CARBON MONO/INHALATION/HAZMAT",
      "CARDIAC OR RESP ARREST/DEATH",
      "CHECK ON THE WELFARE",
      "CHEST PAIN (NON TRAUMATIC)",
      "CHIMNEY FIRE",
      "CHOKING",
      "CO ALARM",
      "CONVULSIONS/SEIZURES",
      "DIABETIC PROBLEMS",
      "DUMPSTER FIRE",
      "DUMPSTER FIRE W/EXPOSURE",
      "REQ CC FIRE INV TEAM",
      "ELECTRICAL FIRE",
      "ELECTRICAL FIRE INSIDE",
      "EMERGENCY TO PROPERTY",
      "FALLS",
      "FIELD FIRE",
      "FIRE ALARM",
      "FIRE ALARM TEST/WORK",
      "FIRE INFORMATION",
      "FIRE OR EMS TRANSFER",
      "FIRE RADIO PANIC ALARM ACTIVAT",
      "FIRE TEST TONE",
      "GAS LEAK",
      "GAS LEAK INSIDE",
      "GAS LEAK OUTSIDE",
      "HAZMAT ACCIDENT",
      "HEADACHE",
      "HEART PROBLEMS/A.I.C.D.",
      "HEMORRHAGE/LACERATIONS",
      "INV STRUCTURE FIRE.HA MM",
      "INVESTIGATE STRUCTURE FIRE",
      "MDC TEST",
      "MED GENERIC DO NOT DELETE",
      "MISCELLANEOUS FIRE REQUEST",
      "MUTUAL AID",
      "MVA, NO INJURIES",
      "OVERDOSE/POISONING (INGESTION)",
      "POSSIBLE OPEN BURN",
      "PREGNANCY/CHILDBIRTH/MISCARRIA",
      "PSYCH/ABNRML BEHVR/SUICIDE",
      "PUBLIC SAFETY INFORMATION",
      "RESCUE/ENTRAPMENT",
      "Return Phone: 5137322231",
      "SICK PERSON (SPECIFIC DIAG)",
      "SMOKE IN THE AREA",
      "SMOKE ODOR INSIDE",
      "STAB/GUNSHOT/PENETRATNG TRAUMA",
      "STROKE (CVA)",
      "STRUCT FIRE INV DPSS/HENRIQUES",
      "STRUCTURE FIRE",
      "STRUCTURE FIRE INV",
      "STRUCTURE FIRE W/ENTRAPMENT",
      "TEST CALL FOR FIRE",
      "TORNADO SIREN TEST/ACTIVATION",
      "TRAFF OR TRANSPT ACC/MVA W/INJ",
      "TRAFF OR TRANSPT INCIDENT",
      "TRANSF/INERFC/PALLIATIVE CARE",
      "TRAUMATIC INJURIES, SPECIFIC",
      "UNCONSCIOUS/FAINTING (NEAR)",
      "UNKNOWN PROBLEM (MAN DOWN)",
      "VEHICLE FIRE",
      "WIRES DOWN"
  );

  private static final String[] CITY_LIST = new String[]{
    "BATAVIA TOWNSHIP",
    "BATAVIA TWP",
    "FRANKLIN TOWNSHIP",
    "FRANKLIN TWP",
    "GOSHEN TOWNSHIP",
    "GOSHEN TWP",
    "JACKSON TOWNSHIP",
    "JACKSON TWP",
    "MIAMI TOWNSHIP",
    "MIAMI TWP",
    "MONROE TOWNSHIP",
    "MONROE TWP",
    "OHIO TOWNSHIP",
    "OHIO TWP",
    "PIERCE TOWNSHIP",
    "PIERCE TWP",
    "STONELICK TOWNSHIP",
    "STONELICK TWP",
    "TATE TOWNSHIP",
    "TATE TWP",
    "UNION TOWNSHIP",
    "UNION TWP",
    "WASHINGTON TOWNSHIP",
    "WASHINGTON TWP",
    "WAYNE TOWNSHIP",
    "WAYNE TWP",
    "WILLIAMSBURG TOWNSHIP",
    "WILLIAMSBURG TWP",

    "AFTON",
    "AMELIA",
    "BANTAM",
    "BATAVIA",
    "BELFAST",
    "BETHEL",
    "BLOWVILLE",
    "BRANCH HILL",
    "CHILO",
    "CLERMONTVILLE",
    "CLOVER",
    "DAY HEIGHTS",
    "EAST BATAVIA HEIGHTS",
    "EDENTON",
    "EPWORTH HEIGHTS",
    "FELICITY",
    "GLEN ESTE",
    "GOSHEN",
    "HAMLET",
    "HULINGTON",
    "LAUREL",
    "LERADO",
    "LINDALE",
    "LOCUST CORNER",
    "LOCUST LAKE",
    "LOVELAND",
    "MARATHON",
    "MIAMIVILLE",
    "MILFORD",
    "MODEST",
    "MONTEREY",
    "MONTEREY",
    "MOORES FORK",
    "MOSCOW",
    "MOUNT CARMEL",
    "MT CARMEL",
    "MOUNT HOLLY",
    "MT HOLLY",
    "MOUNT OLIVE",
    "MT OLIVE",
    "MOUNT PISGAH",
    "MT PISGAH",
    "MOUNT REPOSE",
    "MT REPOSE",
    "MULBERRY",
    "NEVILLE",
    "NEW PALESTINE",
    "NEW RICHMOND",
    "NEWTONSVILLE",
    "NICHOLSVILLE",
    "OLIVE BRANCH",
    "OWENSVILLE",
    "PALESTINE",
//    "PERINTON",          // not mapping
    "PERINTOWN",
    "POINT ISABEL",
    "POINT PLEASANT",
//    "PRINGLE CORNER",    // not mapping
    "RURAL",
    "SALTAIR",
//    "SHILOH",            // not mapping
    "SUMMERSIDE",
    "TOBASCO",
    "UTOPIA",
    "WIGGONSVILLE",
    "WILLIAMS CORNER",
    "WILLIAMSBURG",
    "WILLOWVILLE",
    "WITHAMSVILLE",
    "WOODVILLE",

    // Brown County
    "BROWN CO",
    "FAYETTEVILLE",

    "BRACKEN COUNTY",
    "BROWN COUNTY",
    "BRACKEN COUNTY",
    "CLINTON COUNTY",
    "HAMILTON COUNTY",
    "PENDLETON COUNTY",
    "WARREN COUNTY"
  };

  private static Set<String> KY_CITY_SET = new HashSet<String>(Arrays.asList(
      "BRACKEN COUNTY",
      "CAMPBELL COUNTY"
  ));

  private static final Properties CITY_MAP_TABLE = buildCodeTable(new String[]{
      "EAST BATAVIA HEIGHTS",     "BATAVIA",
      "LOCUST LAKE",              "AMELIA"
  });

  private static final Properties CITY_ABRV_TABLE = buildCodeTable(new String[]{
      "WMBURG",    "WILLIAMSBURG"
  });
}