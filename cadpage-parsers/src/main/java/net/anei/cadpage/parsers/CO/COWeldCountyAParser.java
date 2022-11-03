package net.anei.cadpage.parsers.CO;

import java.util.Properties;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.anei.cadpage.parsers.FieldProgramParser;
import net.anei.cadpage.parsers.MsgInfo.Data;


/**
 * Weld County, CO
 */
public class COWeldCountyAParser extends FieldProgramParser {

  public COWeldCountyAParser() {
    this("WELD COUNTY", "CO");
  }
  
  protected COWeldCountyAParser(String defCity, String defState) {
    super(CITY_CODES, defCity, defState,
          "SKIP ( CALL D | CALL2 ) ADDR MAP UNIT! INFO+");
  }
  
  @Override
  public String getAliasCode() {
    return "COWeldCounty";
  }
  
  @Override
  public String getFilter() {
    return "wrc-hiplink@weldcorcc.com,3037046515@vzwpix.com,777,888,9300,@co.weld.co.us";
  }

  @Override
  protected boolean parseMsg(String body, Data data) {
    
    body = stripFieldStart(body, "Dispatch / ");
    body = stripFieldEnd(body, " UNSUBSCRIBE");
    int pt = body.indexOf("\nText STOP");
    if (pt >= 0)  body = body.substring(0,pt).trim();
    if (!parseFields(body.split("\n"), data)) return false;
    
    // OUT city code seems to mean generic out of state
    if (data.strCity.equals("OUT")) {
      data.strCity = data.defCity = data.defState = "";
    }
    return true;
  }
  
  @Override
  public Field getField(String name) {
    if (name.equals("CALL")) return new MyCallField(false);
    if (name.equals("D")) return new SkipField("D|2NDPG", true);
    if (name.equals("CALL2")) return new MyCallField(true);
    if (name.equals("ADDR")) return new MyAddressField();
    if (name.equals("INFO")) return new MyInfoField();
    return super.getField(name);
  }
  
  private class MyCallField extends CallField {
    
    private boolean strict;
    
    public MyCallField(boolean strict) {
      this.strict = strict;
    }
    
    @Override
    public void parse(String field, Data data) {
      String desc = CALL_CODES.getProperty(field);
      if (desc == null) {
        if (strict) abort();
        desc = field;
      }
      data.strCode = field;
      data.strCall = desc;
    }
    
    @Override
    public String getFieldNames() {
      return "CODE CALL";
    }
  }

  private static final Pattern ADDR_PL_CITY_PTN = Pattern.compile("([^,;]+?)(?:;(.*?))?(?:,([A-Z]{0,3}))?");
  private static final Pattern WCR_PTN = Pattern.compile("\\bWCR\\b", Pattern.CASE_INSENSITIVE);
  private static final Pattern APT_PTN = Pattern.compile("(?:APT|RM|ROOM) *(.*)|[A-Z]?\\d+[A-Z]?|LOT .*");
  private class MyAddressField extends AddressField {
    @Override
    public void parse(String field, Data data) {
      Matcher match = ADDR_PL_CITY_PTN.matcher(field);
      if (!match.matches()) abort();  // Can not happen!!
      
      String addr = match.group(1).trim();
      addr = WCR_PTN.matcher(addr).replaceAll("CR").replace('@', '&');
      super.parse(addr, data);

      String place = match.group(2);
      if (place != null) {
        place = place.trim();
        Matcher match2 = APT_PTN.matcher(place);
        if (match2.matches()) {
          String apt = match2.group(1);
          if (apt == null) apt = place;
          if (!apt.equals(data.strApt)) {
            data.strApt = append(data.strApt, "-", apt);
          }
        } else {
          data.strPlace = place;
        }
      }
      
      String city = match.group(3);
      if (city != null) data.strCity = convertCodes(city, CITY_CODES);
    }
    
    @Override
    public String getFieldNames() {
      return super.getFieldNames() + " APT PLACE CITY";
    }
  }
  
  private static final Pattern CODE_PTN = Pattern.compile("ProQA Medical Recommended Dispatch Level = (\\w+)");
  private class MyInfoField extends InfoField {
    @Override
    public void parse(String field, Data data) {
      Matcher match = CODE_PTN.matcher(field);
      if (match.matches()) {
        data.strCode = match.group(1);
      } else {
        super.parse(field, data);
      }
    }
    
    @Override
    public String getFieldNames() {
      return "CODE INFO";
    }
  }
  
  @Override
  public String adjustMapCity(String city) {
    return convertCodes(city, MAP_CITY_TABLE);
  }
  
  private static final Properties CALL_CODES = buildCodeTable(new String[]{
      "ABAN",            "ABANDONED VEHICLE",
      "AIR",             "AIRCRAFT EVENT",
      "ALARMA",          "ALARM ALL - LAW FIRE EMS",
      "ALARMB",          "ALARM BURGLARY",
      "ALARMFC",         "FIRE ALARM COMMERCIAL",
      "ALARMFR",         "FIRE ALARM RESIDENTIAL",
      "ALARMH",          "HOLD UP ALARM",
      "ALARMO",          "ALARM OTHER",
      "ANIMAL",          "ANIMAL COMPLAINT",
      "AOA",             "ASSIST OTHER AGENCY",
      "AOAALARMF",       "AOA FOR FIRE ALARM",
      "AOAMED",          "ASSIST MEDICAL",
      "APB",             "ALL POINTS BULLETIN",
      "ARSON",           "ARSON",
      "ASALJC",          "ASSAULT IN THE JAIL",
      "ASALTC",          "COLD ASSAULT",
      "ASALTI",          "ASSAULT IN PROGRESS",
      "ASSIST",          "CITIZEN ASSIST",
      "ATSUCI",          "ATTEMPT SUICIDE",
      "AUTPRC",          "AUTO PROWL COLD",
      "AUTPRI",          "AUTO PROWL IN PROGRESS",
      "AWATCH",          "AREA WATCH",
      "BACK PAIN",       "BACK PAIN",
      "BAR",             "BAR CHECK",
      "BDOG",            "BARKING DOG",
      "BITE",            "ANIMAL BITE INVESTIGATION",
      "BOLO",            "BOLO INFO",
      "BOMB",            "BOMB THREAT",
      "BURGC",           "BURGLARY COLD",
      "BURGI",           "BURGLARY IN PROGRESS",
      "CC",              "CITIZEN CONTACT",
      "CIVIL",           "CIVIL PROCESS",
      "CODE",            "CODE ENFORCEMENT",
      "CONAN",           "CONTAINED ANIMAL",
      "CONTB",           "CONTROLLED BURN",
      "CURFEW",          "CURFEW VIOLATION",
      "CVIN",            "CERTIFIED VIN CHECK",
      "CWB",             "CHECK WELL BEING",
      "DAL",             "DOG AT LARGE",
      "DEM",             "DELIVER EMERGENCY MESSAGE",
      "DETOX",           "DETOX TRANSPORT",
      "DISTI",           "DISTURBANCE",
      "DOM",             "DOMESTIC VIOLENCE",
      "DOOR",            "DOOR LOCK AND UNLOCK",
      "DRILL",           "FIRE DRILL",
      "DTRO",            "TRO COLD DELAYED",
      "EXPLO",           "EXPLOSION LAW FIRE EMS",
      "FALSE ALARM",     "FALSE ALARM-ALARM TRACKING",
      "FAMOFF",          "FAMILY OFFENSE",
      "FAOA",            "FIRE ASSIST OTHER AGENCY",
      "FASIST",          "FIRE ASSIST",
      "FIGHT",           "FIGHT",
      "FIGHTW",          "FIGHT WITH WEAPONS",
      "FIREGC",          "FIRE GROUND COVER",
      "FIRESC",          "COMMERCIAL STRUCTURE FIRE",
      "FIRESR",          "RESIDENTIAL STRUCTURE FIRE",
      "FIRET",           "TRASH FIRE",
      "FIREV",           "VEHICLE FIRE",
      "FORGE",           "FORGERY",
      "FP",              "FOOT PURSUIT",
      "FRAUD",           "FRAUD",
      "FUP",             "FOLLOW UP",
      "FUPA",            "FOLLOW UP ACO",
      "FUPF",            "FOLLOW UP FIRE",
      "FWORKS",          "FIREWORKS COMPLAINT",
      "HANGUP",          "911 HANGUP",
      "HARASS",          "HARASSMENT",
      "HAZMAT",          "HAZARDOUS MATERIALS INCIDENT",
      "HOME",            "HOME VISIT",
      "ICCS",            "ICCS",
      "INASLT",          "ASSAULT IN PROG WITH INJURY",
      "INJDOG",          "INJURED ANIMAL",
      "INVDTH",          "INVESTIGATED DEATH",
      "JUV",             "JUVENILE PROBLEM",
      "KIDC",            "COLD KIDNAPPING",
      "KIDI",            "KIDNAPPING IN PROGRESS",
      "LDMUSC",          "LOUD MUSIC",
      "LDPRTY",          "LOUD PARTY",
      "LIQ",             "LIQUOR VIOLATION",
      "LITTER",          "LITTERING COMPLAIN",
      "MEET",            "MEET",
      "MENACC",          "COLD MENACE",
      "MENACI",          "MENACE IN PROGRESS",
      "MESAGE",          "MESSAGE",
      "MISSAD",          "MISSING ADULT",
      "MISSCH",          "MISSING CHILD",
      "MUT",             "MUTUAL AID",
      "NARC",            "CONTROLLED SUBSTANCE CALL",
      "NHPHS",           "NEIGHBORHOOD HOT SPOT PATROL",
      "NOISE",           "NOISE COMPLAINT",
      "NOISEV",          "NOISY VEHICLE",
      "PARK",            "PARKING COMPLAINT",
      "PHONE",           "OBSCENE/NUSI PHONE CALLS",
      "POP",             "POP REPORT",
      "POPH",            "POP ANIMAL HOARDERS",
      "PROP",            "FOUND/LOST PROPERTY",
      "PTOW",            "PRIVATE TOW",
      "RAJ",             "RUNAWAY JUVENILE",
      "RAPE",            "RAPE/SEXUAL ASSAULT",
      "RAPEI",           "RAPE IN PROGRESS",
      "RAV",             "RUNAWAY VEHICLE",
      "REPO",            "REPOSSESSION",
      "ROB",             "ROBBERY",
      "ROBI",            "ARMED ROBBERY IN PROGRESS",
      "ROBSA",           "STRONG ARM ROBBERY",
      "ROVC",            "RESTRAINING ORDER VIOL COLD",
      "ROVI",            "RESTRAINING ORDER VIO IN PROGR",
      "RSTEAL",          "RECOVERED STOLEN VEHICLE",
      "SEX",             "SEX OFFENSE",
      "SEXI",            "SEX OFFENSE IN PROGRESS",
      "SHOOT",           "SHOOTING",
      "SHOP",            "SHOPLIFTER NO PROBLEMS",
      "SHOPP",           "SHOPLIFTER WITH PROBLEMS",
      "SHOT",            "SHOTS FIRED",
      "SI",              "SICK AND INJURED FIRE/EMS",
      "SIA",             "SICK INJURED AMBULANCE ONLY",
      "SIPF",            "SICK AND INJURED POLICE/FIRE",
      "SMKODR",          "SMOKE ODOR INVESTIGATION",
      "SNOW",            "SNOW REMOVAL COMPLAINT",
      "SNOW25",          "SNOW I 25",
      "SNOW76",          "SNOW I 76",
      "SNOWN",           "SNOW HY 34 NORTH",
      "SNOWS",           "SNOW HY 34 SOUTH",
      "STAB",            "STABBING",
      "STAND",           "FIRE/MEDICAL STAND",
      "STORM",           "STORM WARNING",
      "SUSA",            "SUSPICIOUS ACTIVITY",
      "SUSP",            "SUSPICIOUS PERSON",
      "SUSV",            "SUSPICIOUS VEHICLE",
      "SVEHC",           "STOLEN VEHICLE COLD",
      "SVEHI",           "STOLEN VEHICLE IN PROGRESS",
      "SXO",             "SEX OFFENDER REGISTRANT",
      "T",               "TRAFFIC STOP",
      "TA",              "TRAFFIC ACCIDENT NON INJURY",
      "TAC",             "TRAFFIC ACCIDENT - COLD",
      "TAHR",            "TRAFFIC ACCIDENT HIT AND RUN",
      "TAI",             "TRAFFIC ACCIDENT WITH INJURY",
      "TAU",             "TRAFFIC ACCIDENT UNKNOWN INJ",
      "TAUP",            "TRAF ACC UNK INJ POLICE",
      "TEST",            "TEST CALL",
      "THEFTC",          "COLD THEFT",
      "THEFTI",          "THEFT IN PROGRESS",
      "TP",              "TRAFFIC PURSUIT",
      "TRAFA",           "TRAFFIC ARREST",
      "TRAFC",           "TRAFFIC COMPLAINT",
      "TRAFH",           "TRAFFIC HAZARD",
      "TRESPC",          "TRESPASS COLD",
      "TRESPI",          "TRESPASS IN PROGRESS",
      "UNWANT",          "UNWANTED PERSON",
      "VANDC",           "VANDALISM COLD",
      "VANDI",           "VANDALISM IN PROGRESS",
      "VARDA",           "VARDA ALARM",
      "VDAL",            "DOG AT LARGE VICIOUS",
      "VEU",             "SPECIAL VICE ENFORCEMENT",
      "VIN",             "VIN CHECK",
      "WARR",            "WARRANT ARREST/ATTEMPT",
      "WATER",           "WATER COMPLAINT",
      "WEAPON",          "PERSON WITH A WEAPON",

  });
  
  private static final Properties CITY_CODES = buildCodeTable(new String[]{
      "ACO", "BRIGHTON",   // ???
      "ARO", "ARROWHEAD",
      "AUL", "AULT",
      "ARI", "ARISTOCRAT ACRES",
      "BER", "BERTHOUD",
      "BCO", "BOULDER COUNTY",
      "BOU", "BOULDER",
      "BRI", "BRIGHTON",
      "BRG", "BRIGGSDALE",
      "BUC", "RAYMER",   // ???
      "CAM", "CAMFIELD",
      "DAC", "DACONO",
      "DOS", "GREELEY",  // ???
      "EAT", "EATON",
      "ERI", "ERIE",
      "EVA", "EVANS",
      "EVS", "EVANSTON",
      "FC",  "FORT COLLINS",
      "FIR", "FIRESTONE",
      "FRE", "FREDERICK",
      "FTL", "FORT LUPTON",
      "GAL", "GALETON",
      "GAR", "GARDEN CITY",
      "GIC", "GILCREST",
      "GIL", "GILCREST",
      "GRE", "GREELEY",
      "GRO", "GROVER",
      "HER", "HEREFORD",
      "HIL", "HILL N PARK",
      "HUD", "HUDSON",
      "JOH", "JOHNSTOWN",
      "KEE", "KEENESBURG",
      "KER", "KERSEY",
      "LAF", "LAFAYETTE",
      "LAS", "LA SALLE",
      "LCO", "LARIMER COUNTY",
      "LOC", "LOCHBUIE",
      "LON", "LONGMONT",
      "LOV", "LOVELAND",
      "LUC", "LUCERNE",
      "MEA", "MEAD",
      "MIL", "MILLIKEN",
      "NEW", "NEW RAYMER",
      "NIW", "NIWOT",
      "OUT", "OUT",   // out of state???
      "PIE", "PIERCE",
      "PLA", "PLATTEVILLE",
      "SEV", "SEVERANCE",
      "STO", "STONEHAM",
      "WEL", "WELD COUNTY",
      "WIN", "WINDSOR"
  });
  
  private static final Properties MAP_CITY_TABLE = buildCodeTable(new String[]{
      "ARROWHEAD",    "GREELEY",
      "CAMFIELD",     "EATON",
      "HILL N PARK",  "GREELEY"
  });
}
