package net.anei.cadpage.parsers.OR;

import java.util.Properties;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.anei.cadpage.parsers.MsgInfo.Data;
import net.anei.cadpage.parsers.dispatch.DispatchOSSIParser;

public class ORLaneCountyAParser extends DispatchOSSIParser {
  
  public ORLaneCountyAParser() {
    super(CITY_CODES, "LANE COUNTY", "OR",
          "( STATUS ADDR CITY! PLACE? | UNIT CALL ADDR CITY DATETIME! MAP? CH? CODE? PLACE+? ) INFO/N+");
  }
  
  @Override
  public String getFilter() {
    return "CAD@ci.eugene.or.us";
  }

  @Override
  protected boolean parseMsg(String body, Data data) {
    Matcher match = FINAL_INTL_PTN.matcher(body);
    if (match.find()) body = body.substring(0,match.start());
    return super.parseMsg(body, data);
  }
  private static Pattern FINAL_INTL_PTN = Pattern.compile(" *;[A-Z]{3}$");

  @Override
  public Field getField(String name) {
    if (name.equals("STATUS")) return new CallField("UNDER CONTROL", true);
    if (name.equals("CALL")) return new MyCallField();
    if (name.equals("DATETIME")) return new DateTimeField("\\d\\d/\\d\\d/\\d{4} \\d\\d:\\d\\d:\\d\\d", true);
    if (name.equals("MAP")) return new MapField("(?!RM |SP)[A-Z]{1,2}(?:\\d{1,2}[A-Z]?| [A-Z0-9])|\\d{2}[A-Z]?|\\d{4}|\\d{3}[A-Z]|[A-Z]\\d{3}", true);
    if (name.equals("CH")) return new ChannelField("[A-Z]+ ?\\d+", true);
    if (name.equals("CODE")) return new MyCodeField();
    if (name.equals("PLACE")) return new MyPlaceField();
    if (name.equals("INFO")) return new MyInfoField();
    return super.getField(name);
  }
  
  private class MyCallField extends CallField {
    @Override
    public void parse(String field, Data data) {
      data.strCode = field;
      data.strCall = convertCodes(field, CALL_CODES);
    }
    
    @Override
    public String getFieldNames() {
      return "CODE CALL";
    }
  }
  
  private static final Pattern CODE_PTN = Pattern.compile("\\d{1,2}[A-Z]\\d{1,2}[A-Z]?", Pattern.CASE_INSENSITIVE);
  private class MyCodeField extends CodeField {
    public MyCodeField() {
      setPattern(CODE_PTN, true);
    }
  }
  
  private static final Pattern PLACE_APT_PTN = Pattern.compile("(?:APT|ROOM|RM|STE)/? *(.*)|(?:LOT|FLR|FLOOR|SP).*|[A-Z0-9]{1,4}");
  private class MyPlaceField extends PlaceField {
    
    @Override
    public boolean canFail() {
      return true;
    }
    
    @Override
    public boolean checkParse(String field, Data data) {
      Matcher match = PLACE_APT_PTN.matcher(field);
      if (match.matches()) {
        String apt = match.group(1);
        if (apt == null) apt = field;
        data.strApt = append(data.strApt, "-", apt);
        return true;
      }
      if (data.strPlace.length() > 0) return false;
      if (field.startsWith("Radio Channel:") || field.contains("[")) return false;
      if (CODE_PTN.matcher(field).matches()) return false;
      
      data.strPlace = append(data.strPlace, " - ", field);
      return true;
    }
    
    @Override
    public void parse(String field, Data data) {
      if (!checkParse(field, data)) abort();
    }
    
    @Override
    public String getFieldNames() {
      return "APT PLACE";
    }
  }

  private class MyInfoField extends InfoField {
    @Override
    public void parse(String field, Data data) {
      if (CODE_PTN.matcher(field).matches()) {
        data.strCode = field;
        return;
      }
      
      if (field.startsWith("Radio Channel:")) {
        field = field.substring(14).trim();
        if (field.equals(data.strChannel)) return;
        data.strChannel = append(data.strChannel, "/", field);
        return;
      }
      super.parse(field, data);
    }
    
    @Override
    public String getFieldNames() {
      return "CODE CH " + super.getFieldNames();
    }
  }
  
  private static final Properties CALL_CODES = buildCodeTable(new String[]{
      "ACAIRI", "ACCIDENT AIRCRAFT INJURY",
      "ACAIRN", "ACCIDENT AIRCRAFT NON INJURY",
      "ACATVI", "ACCIDENT ATV INJURY",
      "ACATVN", "ACCIDENT ATV NON INJURY",
      "ACBIK",  "ACCIDENT BIKE",
      "ACIND",  "ACCIDENT INDUSTRIAL",
      "ACMARI", "ACCIDENT MARINE",
      "ACMARN", "ACCIDENT MARINE NON INJURY",
      "ACRAD",  "RADIATION ACCIDENT",
      "ACTRN",  "ACCIDENT TRAIN INJURY",
      "ACTRPD", "TRAIN VS PED/BIKE CRASH",
      "ACTRVE", "TRAIN VS VEHICLE CRASH",
      "ACVEBK", "ACCIDENT VEHICLE BIKE",
      "ACVEPD", "VEHICLE/PEDESTRIAN CRASH",
      "AIRPBL", "LARGE AIRCRAFT PROBLEM",
      "AIRPBS", "SMALL AIRCRAFT PROBLEM",
      "ALMCO",  "CARBON MONOXIDE ALARM",
      "ALMFIR", "FIRE ALARM",
      "ALMMED", "MEDICAL ALARM",
      "ALMTST", "ALARM TEST",
      "ANIATK", "ANIMAL ATTACK/BITE",
      "ANIBIT", "ANIMAL BITE",
      "AREAC",  "ALLERGIC REACTION",
      "ASLTIN", "ASSAULT WITH INJURY",
      "ASSTPD", "ASSIST POLICE",
      "BIRTH",  "CHILD BIRTH",
      "BLEED",  "BLEEDING",
      "BOMTHR", "BOMB THREAT",
      "BRKWTM", "BROKEN WATER MAIN",
      "BURN",   "BURNS",
      "BURNIN", "INFORMATION BURNING",
      "CARDAR", "CARDIAC ARREST",
      "CHKPA",  "PATIENT EVALUATION",
      "CHKSMK", "AREA CHECK FOR SMOKE",
      "CHOKE",  "CHOKING",
      "CVA",    "CEREBROVASCULAR ACCIDENT",
      "DEADSU", "DECEASED SUBJECT",
      "DIAB",   "DIABETIC PROBLEM",
      "DIVERT", "HOSPITAL ON DIVERT",
      "DOGBIT", "DOG BITE",
      "DOWNLN", "DOWN LINE",
      "DROWN",  "DROWNING",
      "ELECPB", "ELECTRICAL PROBLEM",
      "ELECTR", "ELECTROCUTION",
      "ELEVEM", "ELEVATOR EMERGENCY",
      "ELEVNO", "ELEVATOR NON-EMERGENCY",
      "EMNON",  "EMERGENCY MEDICAL",
      "EXPLO",  "EXPLOSION",
      "EXPO",   "EXPOSURE",
      "FAINT",  "FAINTING",
      "FIRAPL", "APPLIANCE FIRE",
      "FIRAPT", "FIRE APARTMENT",
      "FIRBOT", "BOAT FIRE",
      "FIRBRK", "BARKDUST FIRE",
      "FIRBSH", "BRUSH FIRE",
      "FIRE",   "FIRE",
      "FIRELE", "ELECTRICAL FIRE",
      "FIRFLD", "FIELD FIRE",
      "FIRFLU", "FLUE FIRE",
      "FIRFOR", "FOREST FIRE",
      "FIRGAS", "GAS FIRE",
      "FIRGRS", "GRASS FIRE",
      "FIRHOU", "HOUSE FIRE",
      "FIRIND", "INDUSTRIAL COMMERCIAL FIRE",
      "FIRLIQ", "FLAMMABLE LIQUID FIRE",
      "FIRMIN", "MINOR STRUCTURE FIRE",
      "FIRMOH", "MOBILE HOME FIRE",
      "FIRSLH", "SLASH FIRE",
      "FIRSTR", "STRUCTURE FIRE",
      "FIRTEN", "TENT FIRE",
      "FIRTRN", "TRAIN FIRE",
      "FIRTSH", "TRASH BIN FIRE",
      "FIRUNK", "UNKNOWN TYPE FIRE",
      "FIRVEH", "VEHICLE FIRE",
      "FLOOD",  "FLOOD",
      "FRACT",  "FRACTURE",
      "GASHOU", "GAS LEAK,SMALL STRUCTURE",
      "GASMIN", "GAS LEAK, MINOR STRUCTURE",
      "GASOUT", "GAS LEAK, EXTERIOR",
      "GMNON",  "GENERAL MEDICAL",
      "GASSTR", "GAS LEAK, LARGE STRUCTURE",
      "GSW",    "GUNSHOT WOUND",
      "HAZMAT", "HAZARDOUS MATERIAL",
      "HAZSPL", "HAZARDOUS SPILL",
      "HEART",  "HEART PROBLEM",
      "HRINJ",  "HIT AND RUN INJURY",
      "ILGLBU", "ILLEGAL BURNING",
      "ILLSUB", "ILL SUBJECT",
      "INFO",   "INFORMATION- BOTH PD AND FD",
      "INFOFD", "INFORMATION - FIRE DEPT",
      "INJBCK", "BACK INJURY",
      "INJEYE", "EYE INJURY",
      "INJFAL", "FALL INJURY",
      "INJHD",  "HEAD INJURY",
      "INJNCK", "NECK INJURY",
      "INJSUB", "INJURED SUBJECT",
      "INJTRA", "TRAUMATIC INJURY",
      "LFTAST", "LIFT ASSIST",
      "LKGAS",  "NATURAL GAS LEAK",
      "LKHYD",  "LEAKING HYDRANT (PROBLEM)",
      "MA",     "MUTUAL AID",
      "MCTTST", "MCT TEST",
      "MDCTST", "MDC TEST",
      "MED",    "GENERAL MEDICAL FOR PROQA",
      "MEDA",   "ALPHA MEDICAL CALL OUTSIDE AG",
      "MEDB",   "BRAVO MEDICAL CALL OUTSIDE AG",
      "MEDC",   "CHARLIE MEDICAL OUTSIDE AGENCY",
      "MEDD",   "DELTA MEDICAL OUTSIDE AGENCY",
      "MEDE",   "ECHO MEDICAL OUTSIDE AGENCY",
      "MEDO",   "OMEGA MEDICAL OUTSIDE AGENCY",
      "MEDST",  "MEDICAL STANDBY",
      "MVAFTL", "MOTOR VEH ACC FATALITY",
      "MVAHAZ", "MOTOR VEHICLE ACC W/HAZMAT",
      "MVAINJ", "MOTOR VEH ACC INJURY",
      "MVANO",  "MOTOR VEH ACC NO INJURY",
      "MVAUNK", "MOTOR VEH ACC UNKNOWN INJ",
      "OAKFIR", "OAKRIDGE FIRE",
      "OAKMED", "OAKRIDGE MEDICAL",
      "OB",     "OBSTETRIC DISTRESS",
      "OD",     "OVERDOSE",
      "ODOR",   "ODOR INVESTIGATION",
      "OFCRDO", "OFFICER DOWN",
      "OUTPHO", "PHONE OUTAGE INFORMATION",
      "OVHT",   "OVERHEATED APPLIANCE",
      "PAABDO", "ABDOMINAL PAIN",
      "PABACK", "BACK PAIN",
      "PACHST", "CHEST PAIN",
      "PAHEAD", "HEADACHE",
      "POISON", "POISONING",
      "PUBAST", "PUBLIC ASSIST- FIRE/EMS",
      "RDCLOS", "ROAD CLOSURE INFORMATION",
      "REKBUR", "RECKLESS BURNING",
      "RESPAR", "RESPIRATORY ARREST",
      "RESPDI", "RESPIRATORY DISTRESS",
      "RRXPRO", "RAILROAD CROSSING PROBLEM",
      "RSCUOP", "RESCUE OPERATION",
      "SEIZ",   "SEIZURES",
      "SMOKE",  "SMOKE FROM A STRUCTURE",
      "SMOTST", "SMOKE TESTING",
      "STAB",   "STAB WOUND",
      "SUBJDO", "SUBJECT DOWN",
      "SUICID", "SUICIDE",
      "SUISUB", "SUICIDAL SUBJECT",
      "TRNALS", "ALS MEDICAL TRANSPORT",
      "TRNBLS", "BLS MEDICAL TRANSPORT",
      "TRNCCT", "CRITICAL CARE TRANSPORT",
      "TRNDSR", "DISORDERLY MEDICAL TRANSPORT",
      "TRNILS", "ILS MEDICAL TRANSPORT",
      "TRNMED", "TRANSPORT MEDICAL",
      "TRNNH",  "NURSING HOME TRANSPORT",
      "TRNOUT", "TRANSPORT OUT OF AREA",
      "TRNPRE", "TRANSPORT PREARRANGED",
      "TRNPRI", "TRANSPORT PRIORITY",
      "UNCPAT", "UNCONSCIOUS PATIENT",
      "VEHWAT", "VEHICLE IN WATER",
      "WASH",   "WASHDOWN",
      "WATRSC", "WATER RESCUE"

  });

  private static final Properties CITY_CODES = buildCodeTable(new String[]{
      "ALS", "ALSEA",
      "BLA", "BLACHLY",
      "BLU", "BLUE RIVER",
      "CAS", "CASCADOA",
      "CHE", "CHESHIRE",
      "COB", "COBURG",
      "COT", "COTTAGE GROVE",
      "CRE", "CRESWELL",
      "CRT", "CRESCENT LAKE",
      "DEA", "DEADWOOD",
      "DEX", "DEXTER",
      "DOR", "DORENA",
      "DRA", "DRAIN",
      "ELM", "ELMIRA",
      "EUG", "EUGENE",
      "FAL", "FALL CREEK",
      "FLO", "FLORENCE",
      "HAL", "HALSEY",
      "HAR", "HARRISBURG",
      "IDA", "IDANHA",
      "JAS", "JASPER",
      "JUN", "JUNCTION CITY",
      "LEA", "LEABURG",
      "LOR", "LORANE",
      "LOW", "LOWELL",
      "MAP", "MAPLETON",
      "MAR", "MARCOLA",
      "MCK", "MCKENZIE",
      "MON", "MONROE",
      "NOT", "NOTI",
      "OAK", "OAKRIDGE",
      "PLE", "PLEASANT HILL",
      "SIS", "SISTERS",
      "SPR", "SPRINGFIELD",
      "SWE", "SWEET HOME",
      "SWI", "SWISSHOME",
      "TID", "TIDEWATER",
      "VEN", "VENETA",
      "VID", "VIDA",
      "WAN", "WALTON",
      "WEF", "WESTFIR",
      "WEL", "WESTLAKE",
      "WES", "WESTLAKE",
      "YAC", "YACHATS"
  });
}
