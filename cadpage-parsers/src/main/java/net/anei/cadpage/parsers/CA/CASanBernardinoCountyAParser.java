package net.anei.cadpage.parsers.CA;

import java.util.Properties;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.anei.cadpage.parsers.CodeTable;
import net.anei.cadpage.parsers.FieldProgramParser;
import net.anei.cadpage.parsers.MsgInfo.Data;
import net.anei.cadpage.parsers.MsgInfo.MsgType;
import net.anei.cadpage.parsers.ReverseCodeTable;

/**
 * San Bernardino County, CA
 */
public class CASanBernardinoCountyAParser extends FieldProgramParser {
  
  private static final Pattern GEN_ALT_UNIT_PTN = Pattern.compile("^([A-Z]+\\d+)[A-Z]?\\b");
  
  public CASanBernardinoCountyAParser() {
    super("SAN BERNARDINO COUNTY", "CA",
          "CALL! ADDR! CITY_EXT LOC_INFO:PLACE AGN_MAP:MAP! X_ST:X! UNIT COMMENTS:INFO LAT/LON:GPS");
  }
  
  @Override
  public String getFilter() {
    return "cad@confire.org";
  }
  
  @Override
  public int getMapFlags() {
    return MAP_FLG_PREFER_GPS | MAP_FLG_SUPPR_LA;
  }
  
  private static final Pattern AUTO_NOTIFICATION_PTN = Pattern.compile("AUTO NOTIFI?CATION(?: ONLY OF)?: *([-A-Z0-9]+) +DISPATCHED AT[: ]*(.*?)(?:, *([A-Z]{1,4}))?(?: (?:LAT|Lat): +(\\d*) +(?:LONG|Long): *(\\d*))?");

  @Override
  protected boolean parseMsg(String body, Data data) {
    
    // Parse special auto-notification alert
    Matcher match = AUTO_NOTIFICATION_PTN.matcher(body);
    if (match.matches()) {
      setFieldList("CALL UNIT ADDR APT CITY GPS");
      data.strCall = "AUTO ALERT";
      data.strUnit = match.group(1);
      parseAddress(match.group(2).trim(), data);
      String city =match.group(3);
      if (city != null) {
        String tmp = CITY_CODES.getCodeDescription(city, true);
        if (tmp != null) city = tmp;
        data.strCity = city;
      }
      setGPSLoc(convGPS(match.group(4))+','+convGPS(match.group(5)), data);
      return true;
    }
    
    if (body.startsWith("|")) body = body.substring(1).trim();
    body = body.replace(">XST:", ">X ST:");
    if (parseFields(body.split(">"), data)) {
      if (data.strPlace.equals("WESTERN ARIZONA REGIONAL") ||
          data.strAddress.equalsIgnoreCase("2735 Silver Creek Rd")) {
        data.strCity = "BULLHEAD CITY";
        data.strState = "AZ";
      }
      else if (data.strPlace.equals("VALLEY VIEW MEDICAL CENTER") ||
               data.strPlace.equals("VALLEY VIEW")) {
        data.strCity = "FORT MOJAVE";
        data.strState = "AZ";
      }
      return true;
    }
    
    // No go for regular dispatch page
    // If positive confirmation, this might be a general alert 
    if (!isPositiveId()) return false;
    
    // If body starts with something that looks like a unit
    // parse the unit field and report result as general alert
    match = GEN_ALT_UNIT_PTN.matcher(body);
    if (!match.find()) return false;
    data.initialize(this);
    setFieldList("CALL UNIT INFO");
    data.msgType = MsgType.GEN_ALERT;
    data.strUnit = match.group(1);
    data.strSupp = body;
    return true;
  }
  
  private String convGPS(String coord) {
    if (coord == null) return "";
    int pt = coord.length()-6;
    if (pt < 2) return "";
    return coord.substring(0,pt) + '.' + coord.substring(pt);
  }

  @Override
  public Field getField(String name) {
    if (name.equals("CALL")) return new MyCallField();
    if (name.equals("NUMBER")) return new AddressField("\\d+", true);
    if (name.equals("ADDR")) return new MyAddressField();
    if (name.equals("X")) return new MyCrossField();
    if (name.equals("CITY_EXT")) return new MyCityExtField();
    if (name.equals("MAP")) return new MyMapField();
    if (name.equals("INFO")) return new MyInfoField();
    if (name.equals("GPS")) return new MyGPSField();
    return super.getField(name);
  }
  
  private class MyCallField extends CallField {
    
    @Override
    public void parse(String field, Data data) {
      field = stripFieldEnd(field, "-");
      String key = field;
      String extra = "";
      int pt = key.indexOf(' ');
      if (pt >= 0) {
        key = field.substring(0,pt);
        if (key.endsWith("-")) key = key.substring(0,key.length()-1).trim();
        extra = field.substring(pt+1).trim();
        if (extra.startsWith("-")) extra = extra.substring(1).trim();
      }
      String desc = TYPE_CODES.getProperty(key);
      if (desc == null) {
        int pt2 = key.indexOf('-');
        if (pt2 >= 0) {
          String key2 = key.substring(0,pt2);
          desc = TYPE_CODES.getProperty(key2);
        }
      }
      if (desc != null) {
        if (desc.startsWith(extra)) field = key;
        field = field + " - " + desc;
      }
      super.parse(field, data);
    }
  }
  
  private static final Pattern BLANK_DASH_PTN = Pattern.compile("(.*) -(.*)");
  private class MyAddressField extends AddressField {
    
    @Override
    public void parse(String field, Data data) {
      
      CodeTable.Result res = CITY_CODES.getResult(field);
      if (res != null) {
        data.strCity = res.getDescription();
        field = res.getRemainder().trim();
      }

      field = stripFieldEnd(field,  "-");
      Matcher match = BLANK_DASH_PTN.matcher(field);
      if (match.matches()) {
        String tmp = match.group(1).trim();
        if (!NUMERIC.matcher(tmp).matches()) {
          field = tmp;
          data.strApt = match.group(2).trim();
        }
      }

      field = append(data.strAddress, " - ", field);
      data.strAddress = "";
      parseAddress(field, data);
    }
    
    @Override
    public String getFieldNames() {
      return "ADDR APT CITY ST";
    }
  }
  
  private class MyCrossField extends CrossField {
    @Override
    public void parse(String field, Data data) {
      field = field.replace("No X-Street", "");
      field = stripFieldStart(field, "/");
      field = stripFieldEnd(field, "/");
      super.parse(field, data);
    }
  }
  
  private class MyCityExtField extends Field {

    @Override
    public void parse(String field, Data data) {

      if (data.strCity.length() == 0) {
        CodeTable.Result res = CITY_CODES.getResult(field);
        if (res != null) {
          data.strCity = res.getDescription();
          field = res.getRemainder().trim();
          field = stripFieldEnd(field, "-");
        }
      }
      data.strPlace = append(data.strPlace, " - ", field);
    }

    @Override
    public String getFieldNames() {
      return "PLACE CITY";
    }
    
  }
  
  private class MyMapField extends MapField {
    @Override
    public void parse(String field, Data data) {
      field = field.replace("-TB MAP:", "- ");
      super.parse(field, data);
    }
  }
  
  private static final Pattern PRO_QA_PTN = Pattern.compile(" *\\[ProQA .*?\\] *");
  private static final Pattern CODE_PTN = Pattern.compile("\\bDispatch code: *(\\w+)\\b");
  private static final Pattern INFO_GPS_PTN = Pattern.compile("W(?:PH2|911) LAT:([-+]?[\\d\\.]+) LON:([-+]?[\\d\\.]+) .*");
  private static final String[] SKIP_MARKERS = new String[]{
    "External",
    "Automatic Case Number(s)",
    "A cellular re-bid has occurred", 
    "check the ANI/ALI Viewer",
    "WPH2 ",
    "W911 "
  };
  private class MyInfoField extends InfoField {
    @Override
    public void parse(String field, Data data) {
      
      field = PRO_QA_PTN.matcher(field).replaceAll(" ");
      field = field.replaceAll("  +", " ").trim();
      
      Matcher match = CODE_PTN.matcher(field);
      if (match.find()) {
        data.strCode = match.group(1);
        field = field.substring(0,match.start()) + " " + field.substring(match.end());
      }

      for (String fld : field.split(",")) {
        match = INFO_GPS_PTN.matcher(fld);
        if (match.matches()) {
          data.strGPSLoc = match.group(1) + ',' + match.group(2);
          continue;
        }
        boolean skip = false;
        String trimFld =  fld.trim();
        for (String skip_mark : SKIP_MARKERS) {
          if (trimFld.startsWith(skip_mark) || skip_mark.startsWith(trimFld)) {
            skip = true;
            break;
          }
        }
        if (skip) continue;
        
        data.strSupp = append(data.strSupp, ",", fld).trim();
      }
    }
    
    @Override
    public String getFieldNames() {
      return "CODE GPS INFO";
    }
  }
  
  private static final Pattern GPS_FIELD_PTN = Pattern.compile("(\\d+)(\\d{6}) */ *(\\d+)(\\d{6})");
  private class MyGPSField extends GPSField {
    @Override
    public void parse(String field, Data data) {
      Matcher match = GPS_FIELD_PTN.matcher(field);
      if (!match.matches()) return;
      field = match.group(1) + '.' + match.group(2) + ',' + match.group(3) + '.' + match.group(4);
      setGPSLoc(field, data);
    }
  }
  
  @Override
  public String adjustMapCity(String city) {
    return convertCodes(city.toUpperCase(), CITY_MAP_TABLE);
  }
  
  private static Properties CITY_MAP_TABLE = buildCodeTable(new String[]{
      "BEAR CREEK",             "BIGBEAR",
      "FREDALBA",               "RUNNINGSPRINGS",
      "IRON MOUNTAIN",          "RICE",
      "MORONGO",                "YUCCAVALLEY",
      "SILVERWOOD",             "HESPERIA",
      "SEARLES VALLEY",         "TRONA",
      "SPRING VALLEY LAKE",     "VICTORVILLE",
      "MARINE CORP AIRGROUND COMBAT CENTER", "29PALMS",
      "VALLEY OF ENCHANTMENT",  "CRESTLINE",
      "WONDER VALLEY",          "29PALMS",
      "YMO",                    "RIALTO"
  }); 
  
  private static final ReverseCodeTable CITY_CODES = new ReverseCodeTable(
      "ABY",  "AMBOY",
      "ADC",  "ADELANTO",
      "ADCC", "ADELANTO",
      "ANG",  "ANGELES OAKS",
      "APP",  "APPLE VALLEY",
      "APPC", "APPLE VALLEY",
      "ARB",  "ARROWBEAR",
      "ARG",  "ARGUS",
      "BAK",  "BAKER",
      "BAR",  "BARSTOW",
      "BARC", "BARSTOW",
      "BBC",  "BIG BEAR",
      "BBL",  "BIG BEAR LAKE",
      "BBLC", "BIG BEAR LAKE",
      "BCR",  "BEAR CREEK",
      "BDM",  "BALDY MESA",
      "BFL",  "BARTON FLATS",
      "BGR",  "BIG RIVER",
      "BLD",  "MT BALDY",
      "BLK",  "BLACK MEADOW LANDING",
      "BLO",  "BLOOMINGTON",
      "BLU",  "BLUE JAY",
      "CAJ",  "CAJON PASS",
      "CDZ",  "CADIZ",
      "CED",  "CEDAR GLEN",
      "CHHC", "CHINO HILLS",
      "CHI",  "CHINO",
      "CHIC", "CHINO",
      "CHL",  "CHINA LAKE",
      "CMA",  "CIMA",
      "COL",  "COLTON",
      "COLC", "COLTON",
      "CPP",  "CEDAR PINES PARK",
      "CRA",  "CRAFTON",
      "CRL",  "CRESTLINE",
      "CRM",  "CRESTMORE",
      "CUD",  "CUDDEBACK LAKE",
      "DAG",  "DAGGETT",
      "DEV",  "DEVORE",
      "EDW",  "EDWARDS AFB",
      "ELM",  "EL MIRAGE",
      "ERP",  "EARP",
      "ESX",  "ESSEX",
      "FAW",  "FAWNSKIN",
      "FFL",  "FOREST FALLS",
      "FON",  "FONTANA",
      "FONC", "FONTANA",
      "FRE",  "FREDALBA",
      "FTI",  "FORT IRWIN",
      "GRTC", "GRAND TERRACE",
      "GVL",  "GREEN VALLEY LAKE",
      "HAV",  "HAVASU",
      "HEL",  "HELENDALE",
      "HES",  "HESPERIA",
      "HESC", "HESPERIA",
      "HIGC", "HIGHLAND",
      "HNK",  "HINKLEY",
      "HOL",  "HOLCOMB VALLEY",
      "HRV",  "HARVARD",
      "IRN",  "IRON MOUNTAIN",
      "IVP",  "IVANPAH",
      "JNV",  "JOHNSON VALLEY",
      "JOT",  "JOSHUA TREE",
      "KEL",  "KELSO",
      "KRJ",  "KRAMER JUNCTION",
      "LAA",  "LAKE ARROWHEAD",
      "LAC",  "LA COUNTY",
      "LDR",  "LANDERS",
      "LEN",  "LENWOOD",
      "LOM",  "LOMA LINDA",
      "LOMC", "LOMA LINDA",
      "LUC",  "LUCERNE VALLEY",
      "LUD",  "LUDLOW",
      "LYC",  "LYTLE CREEK",
      "MEN",  "MENTONE",
      "MOR",  "MORONGO",
      "MTC",  "MONTCLAIR",
      "MTCC", "MONTCLAIR",
      "MTH",  "MOUNTAIN HOMEVILLAGE",
      "MTP",  "MOUNTAIN PASS",
      "MUS",  "MUSCOY",
      "NBD",  "NEWBERRY SPRINGS",
      "NED",  "NEEDLES",
      "NEDC", "NEEDLES",
      "OKG",  "OAK GLEN",
      "ONTC", "ONTARIO",
      "ORO",  "ORO GRANDE",
      "PHL",  "PHELAN",
      "PIN",  "PINON HILLS",
      "PIO",  "PIONEERTOWN",
      "PKD",  "PARKER DAM",
      "POM",  "POMONA",
      "POMC", "POMONA",
      "RCC",  "RANCHO CUCAMONGA",
      "RCCC", "RANCHO CUCAMONGA",
      "RED",  "REDLANDS",
      "REDC", "REDLANDS",
      "RIA",  "RIALTO",
      "RIAC", "RIALTO",
      "RIC",  "RICE",
      "RID",  "RIDGECREST",
      "RIM",  "RIM FOREST",
      "RMT",  "RED MOUNTAIN",
      "RRK",  "RIMROCK",
      "RSP",  "RUNNING SPRINGS",
      "SAH",  "SAN ANTONIO HEIGHTS",
      "SBO",  "SAN BERNARDINO",
      "SBOC", "SAN BERNARDINO",
      "SIL",  "SILVERWOOD",
      "SKY",  "SKY FOREST",
      "SUG",  "SUGARLOAF",
      "SVL",  "SPRING VALLEY LAKE",
      "SVY",  "SEARLES VALLEY",
      "TMD",  "MARINE CORP AIRGROUND COMBAT CENTER",
      "TNP",  "29 PALMS",
      "TNPC", "29 PALMS",
      "TPK",  "TWIN PEAKS",
      "TRN",  "TRONA",
      "UPDC", "UPLAND",
      "VDJ",  "VIDAL JUNCTION",
      "VDL",  "VIDAL",
      "VOE",  "VALLEY OF ENCHANTMENT",
      "VVC",  "VICTORVILLE ",
      "VVCC", "VICTORVILLE",
      "WON",  "WONDER VALLEY",
      "WWD",  "WRIGHTWOOD",
      "YER",  "YERMO",
      "YMO",  "YMO",
      "YUC",  "YUCAIPA",
      "YUCC", "YUCAIPA",
      "YVY",  "YUCCA VALLEY",
      "YVYC", "YUCCA VALLEY"
  );
  
  private static final Properties TYPE_CODES = buildCodeTable(new String[]{
      "AB",         "Animal Bite",
      "ABD",        "Abdominal Pain",
      "ABD-A1",     "Abdominal Pain-BLS",
      "ABD-C1",     "Abdominal Pain-suspected AAA",
      "ABD-C2",     "Abdominal Pain-known AAA",
      "ABD-C3",     "Abdominal Pain-fainting",
      "ABD-C4",     "Abdominal Pain-female fainting",
      "ABD-C5",     "Abdominal Pain-male +navel",
      "ABD-C6",     "Abdominal Pain-fem +navel",
      "ABD-D1",     "Abdominal Pain-not alert",
      "ALL",        "Allergic Reaction",
      "ALL-A1",     "Allergic Rx-itching/rash",
      "ALL-A2",     "Allergic Reaction-spider",
      "ALL-B1",     "Allergic Reaction-3rd party RP",
      "ALL-C1",     "Allergic Reaction+diff breathing",
      "ALL-C2",     "Allergic Reaction-Hx of reaction",
      "ALL-D1",     "Allergic Reaction-not alert",
      "ALL-D2",     "Allergic Reaction-diff speaking",
      "ALL-D3",     "Allergic Reaction-swarm attack",
      "ALL-D4",     "Allergic Reaction-snakebite",
      "ALL-E1",     "Allergic Reaction-not breathing",
      "ANML-A1",    "Animal Bite - non-dangerous body area",
      "ANML-A3",    "Animal Bite - superficial bite",
      "ANML-B1",    "Animal Bite - poss dangerous body area",
      "ANML-B2",    "Animal Bite - hemorraging",
      "ANML-B3",    "Animal Bite - 3rd hand info",
      "ANML-D1",    "Animal Bite - unconscious",
      "ANML-D2",    "Animal Bite - pt not alert",
      "ANML-D4",    "Animal Bite - dangerous body area",
      "ANML-D7",    "Animal Bite - multiple animal attack",
      "AO",         "Aircraft Crash off Airport",
      "AP",         "Aircraft Crash on Airport",
      "APH",        "SCLA response: High",
      "APL",        "SCLA response: Low",
      "APM",        "SCLA response: Medium",
      "AS",         "Aircraft Standby",
      "ASLT",       "Assault",
      "ASLT-A1",    "Assault-non dangerous body area",
      "ASLT A2",    "Assault A-2 - Non-Recent (>6hrs)",
      "ASLT-A2",    "Assault A-2 - Non-Recent (>6hrs)",
      "ASLT-B1",    "Assault-poss dangerous body area",
      "ASLT-B2",    "Assault-serious hemorrhage",
      "ASLT-B3",    "Assault-3rd hand info",
      "ASLT-D1",    "Assault-unconscious or cardiac arrest",
      "ASLT-D2",    "Assault-not alert",
      "ASLT-D3",    "Assault-chest or neck injury",
      "ASLT-D4",    "Assault-multiple victims",
      "AT",         "Ambulance Transport",
      "AT-A1",      "Ambulance Transp. Acuity I",
      "AT-A2",      "Ambulance Transp. Acuity II",
      "AT-A3",      "Ambulance Transp. Acuity III",
      "AT-C1",      "Amb. Transport pt. not alert",
      "AT-C3",      "Amb. Transport - hemorr./shock",
      "AT-C4",      "Amb Transport - poss acute MI",
      "AT-C5",      "Amb. Transport - acute pain",
      "AT-C6",      "Amb. Transport-code 3 requested",
      "AT-D1",      "Amb. Transport-cardiac or resp. arrest",
      "AT-D2",      "Amb. Transport-pt just resuscitated",
      "BACK-A1",    "Back Pain-non trauma",
      "BACK-A2",    "Back Pain-old trauma",
      "BACK-C1",    "Back Pain-suspected AAA",
      "BACK-C2",    "Back Pain-known AAA",
      "BACK-C3",    "Back Pain+fainting",
      "BACK-D1",    "Back Pain-not alert",
      "BIRTH",      "Childbirth",
      "BIRTH-A1",   "Childbirth-1st trimester hemm.",
      "BIRTH-B1",   "Childbirth-Labor >5 months",
      "BIRTH-B2",   "Childbirth-3rd hand RP",
      "BIRTH-C1",   "Childbirth-2nd trimester hemm.",
      "BIRTH-C2",   "Childbirth-serious hemorrhage",
      "BIRTH-C3",   "Childbirth-baby born",
      "BIRTH-D1",   "Childbirth-breech/cord",
      "BIRTH-D2",   "Childbirth-head visible",
      "BIRTH-D3",   "Childbirth-imminent",
      "BIRTH-D4",   "Childbirth-3rd trimester hemm.",
      "BIRTH-D5",   "Childbirth-high risk complic.",
      "BIRTH-D6",   "Childbirth-baby born w/complic.",
      "BIRTH-D7",   "Childbirth-baby born mother w/complic.",
      "BIRTH-O1",   "Childbirth-water broken",
      "BT",         "Bomb Threat",
      "BURN",       "Burn Victim",
      "BURN-A1",    "Burn Victim <18%",
      "BURN-A2",    "Burn Victim",
      "BURN-A3",    "Burn Victim-minor burn",
      "BURN-B1",    "Burn Victim",
      "BURN-B2",    "Burn Victim-3rd hand RP",
      "BURN-C1",    "Burn Victim-building fire",
      "BURN-C2",    "Burn Victim-diff breathing",
      "BURN-C3",    "Burn Victim >18%",
      "BURN-C4",    "Burn Victim-facial burns",
      "BURN-D1",    "Burns-multiple victims",
      "BURN-D2",    "Burn Victim-unconscious",
      "BURN-D3",    "Burn Victim-not alert",
      "BURN-D4",    "Burn Victim-diff speaking",
      "CA",         "Commercial Alarm",
      "CHOKE",      "Choking Victim",
      "CHOKE-A1",   "Subject no longer choking",
      "CHOKE-D1",   "Choking Victim-abn. Breathing",
      "CHOKE-D2",   "Choking Victim-not alert",
      "CHOKE-E1",   "Choking Victim-fully obstructed",
      "CI",         "Commercial Investigation",
      "CO",         "Carbon Monoxide Alarm",
      "CP",         "Chest Pain",
      "CP-A1",      "Chest Pain norm. breathing",
      "CP-C1",      "Chest Pain abn. breathing",
      "CP-C2",      "Chest Pain cardiac Hx",
      "CP-C3",      "Chest Pain-Cocaine Involved",
      "CP-C4",      "Chest Pain w/ reps >35",
      "CP-D1",      "Chest Pain pt not alert",
      "CP-D2",      "Chest Pain w/ diff speaking",
      "CP-D3",      "Chest Pain pt changing color",
      "CP-D4",      "Chest Pain skin clammy",
      "CPR",        "Cardiac Arrest",
      "CPR-B1",     "Cardiac Arrest-obvious death",
      "CPR-D1",     "Cardiac Arrest-ineffective breathing",
      "CPR-D2",     "Obvious or expected death",
      "CPR-E1",     "Cardiac Arrest-not breathing",
      "CPR-E2",     "Cardiac Arrest-agonal reps.",
      "CPR-E3",     "Cardiac Arrest-hanging",
      "CPR-E4",     "Cardiac Arrest-strangulation",
      "CPR-E5",     "Cardiac Arrest-suffocation",
      "CPR-E6",     "Cardiac Arrest-underwater",
      "CPR-O1",     "Expected death or DNR",
      "CVA",        "Stroke Victim",
      "CVA-A1",     "Stroke Victim-breathing < 35",
      "CVA-B1",     "Stroke Victim-3rd party RP",
      "CVA-C1",     "Stroke Victim-not alert",
      "CVA-C2",     "Stroke Victim-abn. Breathing",
      "CVA-C3",     "Stroke Victim-speech problems",
      "CVA-C4",     "Stroke Victim-numbness",
      "CVA-C5",     "Stroke Victim w/ vision prob.",
      "CVA-C6",     "Stroke Victim-sudden headache",
      "CVA-C7",     "Stroke Victim-Hx of stroke",
      "CVA-C8",     "Stroke Victim-breathing >35",
      "CVA-C12",    "Stroke - Unknown Status",
      "DIA",        "Diabetic Problem",
      "DIA-A1",     "Diabetic Problem-pt. alert",
      "DIA-C1",     "Diabetic Prob. - normal breathing",
      "DIA-C2",     "Diabetic Problem - abn. behavior",
      "DIA-C3",     "Diabetic Problem - abn. breathing",
      "DIA-D1",     "Diabetic pt. unconscious",
      "DRWN",       "Possible Drowning",
      "DRWN-A1",    "Possible Drowning - pt alert",
      "DRWN-B1",    "Poss Drowning - now breathing normal",
      "DRWN-B2",    "Poss Drowning - 3rd hand RP",
      "DRWN-C1",    "PossDrowning-alert.abnormalbreathing",
      "DRWN-D1",    "Poss Drowning - unconscious",
      "DRWN-D2",    "Poss Drowning - not alert",
      "DRWN-D3",    "Poss Drowning w/ neck injury",
      "DRWN-D4",    "Poss Drowning - SCUBA accident",
      "ELECT-C1",   "Poss Electrocution - pt alert",
      "ELECT-D1",   "Poss Electrocution - unconscious",
      "ELECT-D2",   "Electrocution - still energized",
      "ELECT-D3",   "Electrocution - power still LIVE",
      "ELECT-D4",   "Electrocution - fall > 30 ft.",
      "ELECT-D5",   "Electrocution w/ long fall",
      "ELECT-D6",   "Poss Electrocution - not alert",
      "ELECT-D7",   "Poss Electrocution - abn. breathing",
      "ELECT-D8",   "Poss Electrocution - 3rd hand RP",
      "ELECT-E1",   "Electrocution - not breathing",
      "ELEV-MA",    "Elevator Rescue with injury",
      "ELEV-RQ",    "Elevator Rescue no injury",
      "EYE",        "Eye Problems",
      "EYE-A1",     "Moderate eye injury",
      "EYE-A2",     "Minor eye injury",
      "EYE-A3",     "Medical eye problem",
      "EYE-B1",     "Severe eye injury",
      "EYE-D1",     "Eye Problem - pt not alert",
      "FALL",       "Fall Victim",
      "FALL-A1",    "Fall - non dangerous body area",
      "FALL-A2",    "Fall Victim - non recent",
      "FALL-A3",    "Fall Victim - public assist",
      "FALL-B1",    "Fall Victim - poss dangerous body area",
      "FALL-B2",    "Fall Victim - serious hemorrhage",
      "FALL-B3",    "Fall Victim - 3rd party RP",
      "FALL-D1",    "Fall Victim > 30 feet",
      "FALL-D2",    "Fall - unconscious or arrest",
      "FALL-D3",    "Fall Victim - not alert",
      "FALL-D4",    "Fall w/ chest injury + diff breathing",
      "FALL-D5",    "Fall Victim - long fall",
      "FAR",        "Fire Alarm with Reset",
      "FC",         "Structure Fire: Commercial",
      "FD",         "Dumpster Fire",
      "FG",         "Vegetation Fire",
      "FH",         "Hay Fire",
      "FI",         "Improvement Fire",
      "FR",         "Refuse Fire",
      "FS",         "Structure Fire: Residential",
      "FT",         "Motorhome/Truck/Bus Fire",
      "FTF",        "Freeway Truck/Bus Fire",
      "FU",         "Unknown Type Fire",
      "FV",         "Vehicle Fire",
      "FVF",        "Veh Fire on Freeway",
      "FWI",        "Fireworks Investigation",
      "GAS",        "Ambulance Standby",
      "GAT",        "Alarm Testing",
      "GBP",        "Burn Permit",
      "GCC",        "County Comm Incident",
      "GEH",        "Env Health Incident",
      "GEM",        "Emergency Medical Incident",
      "GHZ",        "Hazardous Materials Incident",
      "GMH",        "Medical Helicopter Incident",
      "GMI",        "Miscellaneous Incident",
      "GMU",        "Out-of-System Mutual Aid",
      "GPH",        "Public Health Incident",
      "GSW",        "Gunshot Wound",
      "GSW-A1",     "Gunshot + 6 hrs no symptoms",
      "GSW-B1",     "Gunshot Wound >  6 hrs",
      "GSW-B2",     "Gunshot peripheral wound",
      "GSW-B3",     "Gunshot Wound - hemorrhaging",
      "GSW-B4",     "Gunshot Wound - 3rd party RP",
      "GSW-B5",     "Gunshot - obvious death",
      "GSW-D1",     "Gunshot wound pt. unconscious",
      "GSW-D2",     "Gunshot Wound pt. not alert",
      "GSW-D3",     "Gunshot - central wounds",
      "GSW-D4",     "Gunshot - multiple wounds",
      "GSW-D5",     "Gunshot - multiple victims",
      "HA",         "Headache",
      "HCE",        "Heat or Cold Exposure",
      "HCE-A1",     "Heat or Cold Exp - pt alert",
      "HCE-B1",     "Heat or Cold Exp - skin color change",
      "HCE-B2",     "Heat or Cold Exp. - 3rd hand RP",
      "HCE-C1",     "Heat or Cold Exp. - heart attack",
      "HCE-D1",     "Heat or Cold Exposure - not alert",
      "HCE-D2",     "Heat or Cold Exposure - mult victims",
      "HEAD-A1",    "Headache - Breathing Normally",
      "HEAD-B1",    "Headache - Unknown status",
      "HEAD-C1",    "Headache - Not Alert",
      "HEAD-C2",    "Headache - Abnormal Breathing",
      "HEAD-C3",    "Headache - Speech Problems",
      "HEAD-C4",    "Headache - Sudden severe",
      "HEAD-C5",    "Headache - Numbness",
      "HEAD-C6",    "Headache - Paralysis",
      "HEAD-C7",    "Headache + ALOC",
      "HEART-A1",   "Heart rate < 50 or > 130",
      "HEART-A2",   "Chest pain w/ no symptoms",
      "HEART-C1",   "Heart prob + firing of AICD",
      "HEART-C2",   "Heart prob + diff breathing",
      "HEART-C3",   "Heart prob + CP > 35 minutes",
      "HEART-C4",   "Heart prob + cardiac Hx",
      "HEART-C5",   "Heart problems + cocaine",
      "HEART-C6",   "Heart rate < 50 or > 130",
      "HEART-C7",   "Heart problem 3rd hand RP",
      "HEART-D1",   "Heart prob. - pt not alert",
      "HEART-D2",   "Heart problems + diff speaking",
      "HEART-D3",   "Heart problems + changing color",
      "HEART-D4",   "Heart prob - skin clammy",
      "HEART-D5",   "Heart prob + defibrillator",
      "HL",         "Hemorrhage/Laceration",
      "HL-A1",      "Not dangerous hemorrhage",
      "HL-A2",      "Minor hemorrhage",
      "HL-B1",      "Poss dangerous hemorrhage",
      "HL-B2",      "Serious hemorrhage",
      "HL-B3",      "Bleeding disorder",
      "HL-B4",      "Hemorrhage + blood thinner",
      "HL-C1",      "Hemorrhage thru tubes",
      "HL-C2",      "Hemorrhage of dialysis fistula",
      "HL-D1",      "Hemorrhage/Lac. unconscious",
      "HL-D2",      "Hemorrhage - not alert",
      "HL-D3",      "Dangerous hemorrhage",
      "HL-D4",      "Hemorrhage - abn. breathing",
      "HP",         "Heart problems",
      "HZ",         "Hazardous Materials",
      "IN",         "Inside Investigation",
      "INH",        "Carbon Monoxide/Inh/Haz",
      "INH-B1",     "CO-INH-HazMat - pt alert",
      "INH-C1",     "CO-INH-HazMat - pt alert + diff breathing",
      "INH-D1",     "CO-INH-HazMat - pt unconscious",
      "INH-D2",     "CO-INH-HazMat - pt not alert",
      "INH-D3",     "CO-INH-HazMat - diff speaking",
      "INH-D4",     "CO-INH-HazMat - mult. victims",
      "INH-D5",     "CO-INH-HazMat - 3rd hand RP",
      "INH-O1",     "CO detector - no symptoms",
      "MA",         "Medical Aid",
      "MCI",        "Multi-Casualty Incident",
      "MOVUP",      "Moveup and Cover",
      "MU",         "Mutual Aid Requested",
      "NO-EMD",     "Medical problem",
      "OD",         "Overdose",
      "OD-B1",      "Overdose no symptoms",
      "OD-C1",      "Overdose pt not alert",
      "OD-C2",      "Overdose abn. Breathing",
      "OD-C3",      "Overdose antidepressants",
      "OD-C4",      "Overdose cocaine/meth.",
      "OD-C5",      "Overdose narcotics/heroin",
      "OD-C6",      "Overdose - acid or lye",
      "OD-C7",      "Overdose 3rd hand RP",
      "OD-C8",      "Overdose - poison ctrl contact",
      "OD-D1",      "Overdose pt unconscious",
      "OD-D2",      "Overdose skin chng color",
      "OD-O1",      "Poison Control no symptoms",
      "OE",         "Other Emergency",
      "OI",         "Outside Investigation",
      "PS",         "Public Service",
      "PSY",        "Psychiatric/Abn Behavior",
      "PSY-A1",     "Psychiatric non suicidal",
      "PSY-A2",     "Psychiatric - poss 5150",
      "PSY-B1",     "Psychiatric - serious hemorr.",
      "PSY-B2",     "Psychiatric - minor hemorrhage",
      "PSY-B3",     "Psychiatric - threatening suicide",
      "PSY-B4",     "Psychiatric - threatening to jump",
      "PSY-B5",     "Psychiatric near hanging or suffoc.",
      "PSY-B6",     "Psychiatric - 3rd party RP",
      "PSY-D1",     "Psychiatric  - not alert",
      "PSY-D2",     "Psychiatric - dangerous hemorr.",
      "RA",         "Residential Alarm",
      "RQ",         "Rescue",
      "RQ-A1",      "Pt no longer trapped - no injury",
      "RQ-B1",      "Rescue - no longer trapped",
      "RQ-B2",      "Rescue - peripheral entrapment",
      "RQ-B3",      "Rescue - 3rd hand RP",
      "RQ-D1",      "Rescue - machinery",
      "RQ-D2",      "Rescue - trench",
      "RQ-D3",      "Rescue - str. collapse",
      "RQ-D4",      "Conf. space rescue",
      "RQ-D5",      "Rescue - steep terrain",
      "RQ-D6",      "Rescue - mudslide/avalanche",
      "RR",         "Railroad derailment or fire",
      "RS",         "Specialized Rescue",
      "SICK",       "Sick Person",
      "SICK-A1",    "Sick - no priority symptoms",
      "SICK-A2",    "Sick - non-priority complaints",
      "SICK-B1",    "Sick Person - 3rd hand RP",
      "SICK-C1",    "Sick Person - ALOC",
      "SICK-C2",    "Sick Person - abn. breathing",
      "SICK-C3",    "Sick Person - sickle cell",
      "SICK-D1",    "Sick Person - not alert",
      "SICK-O1",    "Sick - confirm calltype w CCC",
      "SICK-O2",    "Sick - confirm calltype w CCC",
      "SOB",        "Shortness of Breath",
      "SOB-C1",     "Abnormal Breathing",
      "SOB-D1",     "Breathing Prob + not alert",
      "SOB-D2",     "Breathing Prob + diff speaking",
      "SOB-D3",     "Breathing Prob + changing color",
      "SOB-D4",     "Breathing Prob + clammy",
      "SOB-E1",     "Ineffective Breathing",
      "SP",         "Fuel Spill",
      "STAB",       "Stabbing",
      "STAB-A1",    "Stabbing + 6 hrs",
      "STAB-B1",    "Stabbing + 6 hrs",
      "STAB-B2",    "Stabbing - peripheral wound",
      "STAB-B3",    "Stabbing - serious hemorrhage",
      "STAB-B4",    "Stabbing - 3rd hand RP",
      "STAB-B5",    "Stabbing - obvious death",
      "STAB-D1",    "Stabbing - unconscious or arrest",
      "STAB-D2",    "Stabbing - pt not alert",
      "STAB-D3",    "Stabbing - central wounds",
      "STAB-D4",    "Stabbing - multiple wounds",
      "STAB-D5",    "Stabbing - multiple victims",
      "STEMI",      "STEMI Transport",
      "SWTR",       "Swift Water Rescue",
      "SZ",         "Seizures",
      "SZ-A1",      "Seizures - not seizing now",
      "SZ-A2",      "Seizures - focal / pt alert",
      "SZ-A3",      "Impending seizure",
      "SZ-A5",      "Impending seizure (aura)",
      "SZ-B1",      "Seizures - effective breathing",
      "SZ-C1",      "Seizures - focal / not alert",
      "SZ-C2",      "Seizures - pregnancy",
      "SZ-C3",      "Seizures - diabetic",
      "SZ-C4",      "/No longer seizing, is breathing",
      "SZ-D1",      "Seizures - not breathing",
      "SZ-D2",      "Seizures - multiple",
      "SZ-D3",      "Seizures - agonal reps",
      "SZ-D4",      "Seizures - effective breathing",
      "TD",         "Tree Down",
      "TE",         "TC with Extrication",
      "TEF",        "Freeway TC w/Extrication",
      "TEST",       "Test Incident",
      "TF",         "TC with Fire",
      "TFF",        "Freeway TC w/Fire",
      "TFR",        "Tactical Fire Response",
      "TI",         "TC with Injuries",
      "TIF",        "Freeway TC w/Injuries",
      "TO",         "TC w Vehicle Over the Side",
      "TOF",        "Freeway TC Over the side",
      "TRAUMA",     "Traumatic Injuries",
      "TRAUMA-A1",  "Traumatic Inj. - non dangerous body area",
      "TRAUMA-A2",  "Traumatic Inj. + 6 hours",
      "TRAUMA-B1",  "Traumatic Inj. - poss dangerous body part",
      "TRAUMA-B2",  "Traumatic Inj. - serious hemorr.",
      "TRAUMA-D1",  "Traumatic Inj. - unconscious",
      "TRAUMA-D2",  "Traumatic Inj. - not alert",
      "TRAUMA-D3",  "Traumatic chest inj. + diff breathing",
      "TU",         "TC with Unknown Injuries",
      "TUF",        "Freeway TC Unk Injuries",
      "UNC",        "Unconscious Person",
      "UNC-A1",     "Fainting now alert-no cardiac Hx",
      "UNC-A2",     "Fainting now alert + cardiac Hx",
      "UNC-A3",     "Fainting now alert-no cardiac Hx",
      "UNC-C1",     "Pt alert with abnormal breathing",
      "UNC-C2",     "Fainting now alert + cardiac Hx",
      "UNC-C3",     "Female 12-50 w/ abdom. Pain",
      "UNC-D1",     "Unconscious - agonal reps",
      "UNC-D2",     "Unconscious - pt breathing",
      "UNC-D3",     "Unconscious - not alert",
      "UNC-D4",     "Unconscious - changing color",
      "UNC-E1",     "Unconscious - ineffective breathing",
      "UNKM",       "Unknown Medical Problem",
      "UNKM-B1",    "Unknown prob - pt alert",
      "UNKM-B2",    "Medical Alarm Activation",
      "UNKM-B3",    "Unkn prob - 3rd hand caller",
      "UNKM-B4",    "Unkn prob - language barrier",
      "UNKM-D1",    "Unkn prob - life status questionable",
      "WS",         "Water Salvage",
      "ZAP",        "Outside Electrical Incident"

  });
}
