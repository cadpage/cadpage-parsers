package net.anei.cadpage.parsers.CA;

import java.util.Properties;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.anei.cadpage.parsers.CodeTable;
import net.anei.cadpage.parsers.MsgInfo.Data;
import net.anei.cadpage.parsers.MsgInfo.MsgType;
import net.anei.cadpage.parsers.MsgParser;
import net.anei.cadpage.parsers.StandardCodeTable;

/**
 * Sacramento County, CA
 */
public class CASacramentoCountyParser extends MsgParser {
  
  private static final Pattern MASTER = Pattern.compile("([A-Z0-9]+)/([A-Z0-9]+)/([A-Z0-9]+)/([A-Z0-9]*,[A-Z0-9]*)\\(([^,\\)]+),([A-Z]+)\\)\\((.*?)(?:\\).*)?");
  
  public CASacramentoCountyParser() {
    super("SACRAMENTO COUNTY", "CA");
    setFieldList("SRC CODE CALL MAP MAP ADDR APT CITY UNIT INFO");
    setupGpsLookupTable(GPS_LOOKUP_TABLE);
  }
  
  @Override
  public String getFilter() {
    return "@CAD.GOV";
  }
  
  @Override
  public int getMapFlags() {
    return MAP_FLG_SUPPR_LA;
  }
  
  @Override
  protected boolean parseMsg(String subject, String body, Data data) {
    
    if (!subject.equals("CAD PAGE-Do not reply")) return false;
    
    Matcher match = MASTER.matcher(body);
    if (!match.matches()) {
      data.msgType = MsgType.GEN_ALERT;
      data.strSupp = body;
      return true;
    }
    
    data.strSource = match.group(1);
    data.strCode = match.group(2);
    data.strCall = CALL_CODES.getCodeDescription(data.strCode);
    if (data.strCall == null) data.strCall = data.strCode;
    data.strMap = match.group(3);
    String map = match.group(4);
    if (!map.equals(",")) data.strMap = data.strMap + '-' + map;
    parseAddress(match.group(5).replace('.', ' ').trim(), data);
    data.strCity = convertCodes(match.group(6), CITY_CODES);
    data.strUnit = match.group(7).trim();
    return true;
  }
  
  @Override
  public String adjustMapCity(String city) {
    return convertCodes(city, MAP_CITY_TABLE);
  }
  
  private static final Properties GPS_LOOKUP_TABLE = buildCodeTable(new String[]{
      "7 ARP",      "38.600628,-121.506951",
      "21 ARP",     "38.600802,-121.509487",
      "53 ARP",     "38.605780,-121.500030",
      "95 ARP",     "38.604468,-121.492465",
      "101 ARP",    "38.605162,-121.492496",
      "191 ARP",    "38.606602,-121.481762",
      "201 ARP",    "38.605470,-121.479024",
      "281 ARP",    "38.599693,-121.471693",
      "301 ARP",    "38.597766,-121.469170",
      "350 ARP",    "38.590691,-121.465783",
      "361 ARP",    "38.595981,-121.455788",
      "401 ARP",    "38.595542,-121.450331",
      "501 ARP",    "38.586075,-121.440454",
      "505 ARP",    "38.585841,-121.440779",
      "529 ARP",    "38.588681,-121.436244",
      "601 ARP",    "38.584868,-121.423481",
      "605 ARP",    "38.585114,-121.423055",
      "700 ARP",    "38.573073,-121.423401",
      "701 ARP",    "38.573426,-121.420227",
      "791 ARP",    "38.562362,-121.419932",
      "801 ARP",    "38.562391,-121.419051",
      "815 ARP",    "38.561285,-121.418041",
      "830 ARP",    "38.559554,-121.416785",
      "901 ARP",    "38.561058,-121.407554",
      "925 ARP",    "38.564365,-121.395451",
      "950 ARP",    "38.561796,-121.397583",
      "1001 ARP",   "38.567806,-121.382208",
      "1051 ARP",   "38.569417,-121.373480",
      "1101 ARP",   "38.571111,-121.365401",
      "1151 ARP",   "38.574874,-121.357327",
      "1200 ARP",   "38.572123,-121.352872",
      "1201 ARP",   "38.577566,-121.349583",
      "1251 ARP",   "38.580174,-121.340707",
      "1285 ARP",   "38.581756,-121.333705",
      "1291 ARP",   "38.583680,-121.337530",
      "1301 ARP",   "38.579883,-121.331538",
      "1333 ARP",   "38.587518,-121.334453",
      "1351 ARP",   "38.591943,-121.334128",
      "1381 ARP",   "38.592320,-121.331130",
      "1392 ARP",   "38.593530,-121.330600",
      "1402 ARP",   "38.592462,-121.326778",
      "1422 ARP",   "38.590445,-121.323011",
      "1450 ARP",   "38.596572,-121.330990",
      "1480 ARP",   "38.594100,-121.325140",
      "1495 ARP",   "38.602221,-121.326727",
      "1502 ARP",   "38.599725,-121.321153",
      "1514 ARP",   "38.601220,-121.317368",
      "1530 ARP",   "38.602383,-121.312823",
      "1550 ARP",   "38.602650,-121.316600",
      "1551 ARP",   "38.606376,-121.313959",
      "1602 ARP",   "38.607830,-121.307384",
      "1604 ARP",   "38.607480,-121.307822",
      "1702 ARP",   "38.617161,-121.304306",
      "1722 ARP",   "38.619645,-121.303325",
      "1748 ARP",   "38.625970,-121.300970",
      "1750 ARP",   "38.625866,-121.295988",
      "1751 ARP",   "38.628119,-121.299104",
      "1772 ARP",   "38.623479,-121.292380",
      "1790 ARP",   "38.622660,-121.289870",
      "1802 ARP",   "38.621147,-121.289423",
      "1810 ARP",   "38.619019,-121.279023",
      "1820 ARP",   "38.620560,-121.285210",
      "1892 ARP",   "38.622905,-121.279023",
      "1902 ARP",   "38.625097,-121.276144",
      "1920 ARP",   "38.626557,-121.273688",
      "1980 ARP",   "38.632499,-121.270660",
      "1982 ARP",   "38.632840,-121.269447",
      "2002 ARP",   "38.634655,-121.266169",
      "2060 ARP",   "38.636214,-121.256263",
      "2102 ARP",   "38.634237,-121.249701",
      "2202 ARP",   "38.632136,-121.232797",
      "2270 ARP",   "38.635330,-121.225400",
      "2278 ARP",   "38.635502,-121.220630",
      "2286 ARP",   "38.634605,-121.217006",
      "2291 ARP",   "38.634463,-121.213417",
      "2301 ARP",   "38.636844,-121.223136",
      "2302 ARP",   "38.635465,-121.221878",
      "2311 ARP",   "38.639742,-121.222736",
      "2318 ARP",   "38.634712,-121.218281",
      "2320 ARP",   "38.636290,-121.219800",
      "2321 ARP",   "38.640454,-121.220946",
      "2325 ARP",   "38.639416,-121.218744",
      "2331 ARP",   "38.641793,-121.218694",
      "2351 ARP",   "38.641777,-121.211266",
      "2355 ARP",   "38.644836,-121.211417",
      "2361 ARP",   "38.642742,-121.214699",
      "2362 ARP",   "38.632843,-121.223710",
      "2390 ARP",   "38.637557,-121.208846",
      "2420 ARP",   "38.638098,-121.205981",
      "2450 ARP",   "38.671895,-121.187446",
      "2460 ARP",   "38.648966,-121.197789",
      "2478 ARP",   "38.648966,-121.190358",
      "2480 ARP",   "38.647346,-121.199020",
      "2484 ARP",   "38.647346,-121.188729",
      "2501 ARP",   "38.651723,-121.197463",
      "2520 ARP",   "38.643013,-121.196708",
      "2590 ARP",   "38.659462,-121.188786",
      "2596 ARP",   "38.660938,-121.660938",
      "2601 ARP",   "38.663736,-121.191617",
      "2602 ARP",   "38.663517,-121.192123",
      "2621 ARP",   "38.655630,-121.195379",
      "2660 ARP",   "38.646982,-121.193511",
      "2691 ARP",   "38.676137,-121.191792",
      "2701 ARP",   "38.677595,-121.192181",
      "2710 ARP",   "38.649273,-121.188149",
      "2740 ARP",   "38.651844,-121.192153",
      "2781 ARP",   "38.678951,-121.186625",
      "2791 ARP",   "38.679700,-121.183540",
      "2801 ARP",   "38.680857,-121.183779",
      "2802 ARP",   "38.677930,-121.179890",
      "2825 ARP",   "38.682037,-121.181944",
      "2830 ARP",   "38.680120,-121.177920",
      "2851 ARP",   "38.682094,-121.175023",
      "2890 ARP",   "38.684661,-121.169699",
      "2901 ARP",   "38.686204,-121.170686",
      "2910 ARP",   "38.660084,-121.190256",
      "2931 ARP",   "38.691683,-121.171863",
      "2951 ARP",   "38.707495,-121.158421",
      "2961 ARP",   "38.694779,-121.171219",
      "2971 ARP",   "38.698586,-121.698586",
      "2980 ARP",   "38.700388,-121.165693",
      "3001 ARP",   "38.707341,-121.156487",
      "3025 ARP",   "38.706769,-121.169437",
      "3090 ARP",   "38.670750,-121.189611"

  });
  
  private static CodeTable CALL_CODES = new StandardCodeTable(
      "1",    "ABDOMINAL PAIN",
      "10",   "CHEST PAIN",
      "11",   "CHOKING",
      "12",   "SEIZURE",
      "13",   "DIABETIC PROBLEM",
      "14",   "DROWNING, NEAR DROWNING",
      "15",   "ELECTROCUTION, LIGHTNING",
      "16",   "EYE INJURY",
      "17",   "FALL",
      "18",   "HEADACHE",
      "19",   "HEART PROBLEMS",
      "2",    "ALLERGIC REACTION, ENVENOMATION",
      "20",   "EXPOSURE, HYPERTHERMIA, HYPOTHERMIA",
      "21",   "HEMORRAGE, BLEEDING",
      "22",   "INACCESSIBLE ENTRAPMENT (NON-MVA)",
      "23",   "OVERDOSE",
      "24",   "GYNECOLOGICAL",
      "25",   "PSYCHIATRIC - 5150",
      "26",   "SICK PERSON",
      "27",   "STABBING, SHOOTING, OTHER PENETRATING TRAUMA",
      "28",   "STROKE, CVA, TIA",
      "29",   "MVA, TRAFFIC RELATED",
      "3",    "ANIMAL BITE",
      "30",   "TRAUMA, AMPUTATION, ETC.",
      "31",   "SYNCOPE",
      "32",   "UNKNOWN (MAN DOWN, ETC.)",
      "4",    "ASSAULT, SEXUAL ASSAULT",
      "5",    "BACK PAIN",
      "6",    "BREATHING DIFFICULTY",
      "7",    "BURN",
      "8",    "CARBON MONOXIDE, INHALATION, HAZMAT",
      "9",    "CARDIAC OR RESPIRATORY ARREST",
      "AC1",  "MINOR IN FLIGHT EMERGENCY",
      "AC2",  "MAJOR IN FLIGHT EMERGENCY",
      "AC3",  "AIRCRAFT ACCIDENT",
      "AN",   "ANIMAL RESCUE",
      "CS",   "COMMERCIAL STRUCTURE",
      "CS1",  "COMMERCIAL STRUCTURE FIRE",
      "CS2",  "COMMERCIAL STRUCTURE FIRE",
      "CS3",  "COMMERCIAL STRUCTURE FIRE",
      "CSA",  "COMMERCIAL STRUCTURE APT",
      "CSH",  "HOSPITAL",
      "CSP",  "CONFINED SPACE RESCUE",
      "CSS",  "SCHOOL",
      "DUMP", "DUMPSTER FIRE",
      "ELV",  "ELEVATOR RESCUE",
      "EMI",  "ELEC MOTOR INSIDE",
      "EMO",  "ELEC MOTOR OUTSIDE",
      "EXP",  "EXPLOSION",
      "FC",   "FIRECRACKER",
      "FEN",  "FENCE FIRE",
      "FGI",  "FLAM. GAS LEAK INSIDE",
      "FGO",  "FLAM GAS LEAK OUTSIDE",
      "FL",   "FLOODING",
      "G",    "GRASS",
      "GRF",  "GRASS -- RED FLAG",
      "GS",   "GRASS STRUCTURE",
      "HM1",  "HAZ MAT LEVEL 1",
      "HM2",  "HAZ MAT LEVEL 2",
      "HM3",  "HAZ MAT LEVEL 3",
      "IAC",  "INTERNAL ALARM COMMERCIAL",
      "IAR",  "INTERNAL ALARM RESIDENTIAL",
      "IB",   "ILLEGAL BURN",
      "LAW",  "LAW ENFORCEMENT ASSIST",
      "LO",   "LOCK OUT",
      "LQ1",  "LIQUID SPILL",
      "LQ2",  "LIQUID SPILL",
      "M2",   "MEDICAL AID CODE 2",
      "M3",   "MEDICAL AID",
      "MA",   "MEDICAL AID LEVEL A",
      "MB",   "MEDICAL AID LEVEL B",
      "MC",   "MEDICAL AID LEVEL C",
      "MCI",  "MULTI CASUALTY INC",
      "MD",   "MEDICAL AID LEVEL D",
      "OT",   "OTHER",
      "PA",   "PUBLIC ASSISTANCE",
      "RC",   "RAIL CAR",
      "S",    "STRUCTURE",
      "S1",   "OUTBUILDING FIRE",
      "S2",   "STRUCTURE FIRE",
      "S3",   "STRUCTURE FIRE",
      "SD",   "SPECIAL DUTY",
      "SPR",  "SPECIAL RESCUE",
      "TCR",  "TECHNICAL RESCUE",
      "TRA",  "TRASH FIRE",
      "TRANS","TRANSFORMER",
      "TREE", "TREE FIRE",
      "VCS",  "VEH INTO COMM BLDG",
      "VF",   "VEHICLE FIRE",
      "VFC",  "COMM VEHICLE FIRE",
      "VFP",  "VEH FIRE PKG GARAGE",
      "VFT",  "TANKER FIRE",
      "VR",   "VERTICAL RESCUE",
      "VRS",  "VEH INTO HOUSE",
      "WD",   "WIRES DOWN",
      "WR1",  "STILL WATER RESCUE",
      "WR2",  "FLOWING WATER RESCUE"
  );
  
  private static final Properties CITY_CODES = buildCodeTable(new String[]{
      "ANT", "ANTELOPE",
      "ARD", "ARDEN-ARCADE",
      "CAR", "CARMICHAEL",
      "CIT", "CITRUS HEIGHTS",
      "CLF", "SACRAMENTO",           // ????
      "COS", "COSUMNES",       
      "COU", "COURTLAND",
      "DEL", "DELTA",
      "EEG", "EAST ELK GROVE",
      "ELK", "ELK GROVE",
      "FAI", "FAIR OAKS",
      "FOL", "FOLSOM",
      "FRU", "FRUITRIDGE MANOR",
      "GAL", "GALT",
      "GCY", "GALT",
      "HER", "HERALD",
      "ISL", "ISLETON",
      "LAG", "LAGUNA",
      "MCC", "MCCLELLAN",
      "NAT", "NATOMAS",
      "NHI", "NORTH HIGHLANDS",
      "ORA", "ORANGEVALE",
      "PAC", "PACIFIC",
      "PLA", "PLACER",
      "PLG", "PLEASANT GROVE",
      "RCN", "RANCHO NORTH",
      "RCO", "RANCHO CORDOVA",
      "RCS", "RANCHO SOUTH",
      "RDF", "RIVER DELTA",
      "RI",  "RANDALL ISLAND",
      "RIO", "RIO LINDA",
      "RMU", "RANCHO MURIETA",
      "SAC", "SACRAMENTO",
      "SSC", "S SACRAMENTO",
      "VIN", "VINEYARD",
      "WAL", "WALNUT GROVE",
      "WIL", "WILTON",
      "WLT", "WILTON",
      "WSC", "WEST SACRAMENTO"
  });
  
  private static final Properties MAP_CITY_TABLE = buildCodeTable(new String[]{
      "COSUMNES",   "SLOUGHHOUSE",
      "PACIFIC",    "SACRAMENTO"
  });
}
