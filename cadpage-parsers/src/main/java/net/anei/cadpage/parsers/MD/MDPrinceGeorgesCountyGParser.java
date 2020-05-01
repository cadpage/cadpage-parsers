package net.anei.cadpage.parsers.MD;

import java.util.Properties;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.anei.cadpage.parsers.MsgInfo.Data;


/**
 * Prince Georges County, MD (variant E)
 */
public class MDPrinceGeorgesCountyGParser extends MDPrinceGeorgesCountyBaseParser {
  
  public MDPrinceGeorgesCountyGParser() {
    super("CALL ADDR! APT? PLACE/CS+? X! CH? BOX Units:UNIT% UNIT+ Remarks:INFO");
  }
  
  @Override
  public String getFilter() {
    return "@alert.co.pg.md.us,@c-msg.net,14100,12101,@everbridge.net,89361,87844";
  }
  
  private static final Pattern HTML_FILTER_PTN = Pattern.compile("\n {3,}<p> (.*?)</p>\n {3,}", Pattern.DOTALL);
  
  @Override
  protected boolean parseHtmlMsg(String subject, String body, Data data) {
    if (body.startsWith("<!doctype html>\n")) {
      Matcher match = HTML_FILTER_PTN.matcher(body);
      if (!match.find()) return false;
      body = match.group(1).trim();  
      body = body.replace("\n<br>", " ").replace("\n", " ").replace("<br>", " ");
    }

    return super.parseHtmlMsg(subject, body, data);
  }
  
  private static final Pattern ID_PTN = Pattern.compile("(PF\\d{14}): *");

  @Override
  public boolean parseMsg(String body, Data data) {
    
    Matcher match = ID_PTN.matcher(body);
    if (!match.lookingAt()) return false;
    data.strCallId = match.group(1);
    body = body.substring(match.end()).trim();
    
    body = body.replace(" Unit:", " Units:");
    body = body.replace(". Remarks:", ", Remarks:");
    if (!parseFields(body.split(","), data)) return false;
    
    fixCity(data);
    return true;
  }
  
  @Override
  public String getProgram() {
    return "ID " + super.getProgram().replace("BOX", "BOX CITY ST");
  }
  
  @Override
  public Field getField(String name) {
    if (name.equals("CALL")) return new MyCallField();
    if (name.equals("APT")) return new AptField("# *(.*)|([A-Z]?\\d{1,5}[A-Z]?)", true);
    if (name.equals("X")) return new MyCrossField();
    if (name.equals("CH")) return new ChannelField("(?:T?G?|FX)[A-F]\\d{1,2}", true);
    if (name.equals("BOX")) return new BoxField("\\d{4}[A-Z]{0,3}|MA[A-Z]{2}");
    if (name.equals("INFO")) return new MyInfoField();
    if (name.equals("UNIT")) return new MyUnitField();
    return super.getField(name);
  }
  
  private class MyCallField extends Field {
    @Override
    public void parse(String field, Data data) {
      String call = CALL_CODES.getProperty(field);
      if (call != null) {
        data.strCode = field;
        data.strCall = call;
      } else {
        data.strCall = field;
      }
    }
    
    @Override
    public String getFieldNames() {
      return "CODE CALL";
    }
  }
  
  // Cross field only exist if it has the correct keyword
  private class MyCrossField extends CrossField {
    @Override
    public boolean checkParse(String field, Data data) {
      if (field.equals("btwn")) return true;
      if (!field.startsWith("btwn ")) return false;
      field = field.substring(5).trim();
      field = stripFieldStart(field, "/");
      field = stripFieldEnd(field, "/");
      field = field.replace(" and ", " / ");
      super.parse(field, data);
      return true;
    }
    
    @Override
    public void parse(String field, Data data) {
      if (!checkParse(field, data)) abort();
    }
  }
  
  // Info field drops ProQA comments
  private class MyInfoField extends InfoField {
    @Override
    public void parse(String field, Data data) {
      if (field.contains("ProQA recommends dispatch")) return;
      field = stripFieldStart(field, "CC TEXT:");
      super.parse(field, data);
    }
  }
  
  // Unit fields join together with comma separators
  private class MyUnitField extends UnitField {
    @Override
    public void parse(String field, Data data) {
      if (field.equals("WI")) {
        if (!data.strCall.contains("(Working)")) data.strCall += " (Working)";
      } else {
        data.strUnit = append(data.strUnit, ",", field);
      }
    }
  }
  
  static void fixCity(Data data) {
    if (data.strCity.length() > 0) return;
    if (data.strBox.startsWith("MA")) {
      String city = MA_CITY_TABLE.getProperty(data.strBox);
      if (city != null) {
        int pt = city.lastIndexOf('/');
        if (pt >= 0) {
          data.strState = city.substring(pt+1);
          city = city.substring(0,pt);
        }
        data.strCity = city;
      }
    }
  }
  
  private static final Properties CALL_CODES = buildCodeTable(new String[]{
      "ACC W INJ",            "ACCIDENT WITH INJURIES",
      "ACTIVEA1",             "UNCONFIRMED ACTIVE ASSAILANT",
      "ACTIVEA2",             "CONFIRMED ACTIVE ASSAILANT",
      "ACTSHT",               "ACTIVE SHOOTER",
      "ALS",                  "A L S EMERGENCY",
      "ALS+",                 "MEDIC LOCAL ",
      "ALS0",                 "MEDIC LOCAL",
      "ALS1",                 "MEDIC LOCAL",
      "ALS2",                 "MEDIC LOCAL",
      "ALSM",                 "MEDIC LOCAL",
      "APARTMENT FIRE",       "APARTMENT FIRE",
      "APTF",                 "APARTMENT FIRE",
      "APTFR",                "APARTMENT FIRE REDUCED ASSIGNMENT",
      "APTG",                 "GAS LEAK IN APARTMENT",
      "APTT",                 "APARTMENT FIRE WITH ENTRAPMENT",
      "ASALT",                "ASSAULT",
      "ASALTA",               "ASSAULT",
      "ASPD",                 "ASSIST POLICE",
      "ASSAULT",              "ASSAULT",
      "AUTOF",                "AUTO FIRE",
      "AUTOFT",               "AUTO FIRE WITH ENTRAPMENT",
      "BARI",                 "BARRICADED SUBJECT",
      "BLS AMB",              "BLS AMB",
      "BLS+",                 "AMBULANCE LOCAL ",
      "BLS0",                 "AMBULANCE LOCAL",
      "BLS1",                 "AMBULANCE LOCAL",
      "BOMB",                 "BOMB INCIDENT",
      "BOMB0",                "BOMB- UNVERIFIED PACKAGE OR HAZARD",
      "BOMB1",                "BOMB/HAZARDOUS DEVICE INVESTIGATION",
      "BOMB2",                "SUSPECTED BOMB/HAZARDOUS DEVICE",
      "BOMB3",                "BOMB/HAZARDOUS DEVICE",
      "BOMT",                 "BOMB THREAT",
      "BRUSH FIRE",           "BRUSH FIRE",
      "BRUSH",                "BRUSH FIRE",
      "BRUSHE",               "BRUSH FIRE",
      "BTFIRE",               "BOAT FIRE",
      "BTINV",                "BOAT INVESTIGATION",
      "BUILDF",               "BUILDING FIRE",
      "BUILDFR",              "BUILDING FIRE REDUCED ASSIGNMENT",
      "BUILDG",               "BUILDING FIRE NATURAL GAS LEAK",
      "BUILDING FIRE",        "BUILDING FIRE",
      "BUILDING NAT GAS LK",  "BUILDING NAT GAS LK",
      "BUILDT",               "BUILDING FIRE WITH ENTRAPMENT",
      "BUSY",                 "BUSY",
      "CO ALARM",             "C O ALARM",
      "COALRM",               "C O ALARM",
      "COLAP1",               "TREE ON A HOUSE INVESTIGATION",
      "COLAP2",               "VEHICLE INTO STRUCTURE W/O INJURIES",
      "COLAP3",               "VEHICLE INTO STRUCTURE W/ INJURIES",
      "COLAP4",               "STRUCTURE ENTRAPMENT OVER WATER WITH INJURIES",
      "COLAPI",               "COLLAPSE INVESTIGATION",
      "COLAPO",               "TRENCH NOTIFICATION",
      "COLAPS",               "COLLAPSE SERVICE CALL",
      "COLEAK",               "C O LEAK WITH SICK PEOPLE",
      "COLLAPSE INVEST",      "COLLAPSE INVEST",
      "CONFSP",               "CONFINED SPACE INCIDENT",
      "CONFSP4",              "TRENCH/CONFINED SPACE/BUILDING COLLAPSE W/ ENTRAPMENT",
      "CPR",                  "C P R IN PROGRESS",
      "CUTT",                 "CUTTING",
      "DEP ACCI",             "DEPARTMENTAL ACCIDENT",
      "DEP",                  "DEPARTMENTAL ACCIDENT",
      "DEPARTMENTAL ACCI",    "DEPARTMENTAL ACCIDENT",
      "DEPFD",                "DEPARTMENTAL ACCIDENT",
      "DSTRB",                "JAIL DISTURBANCE",
      "DSTURB",               "JAIL DISTURBANCE",
      "ELEV",                 "STUCK ELEVATOR",
      "ELEVI",                "STUCK ELEVATOR WITH INJURY",
      "ELEVT",                "STUCK ELEVATOR WITH ENTRAPMENT",
      "ESCAL",                "ESCALATOR INCIDENT",
      "ESCALT",               "ESCALATOR INCIDENT WITH ENTRAPMENT",
      "ETASK",                "E M S TASK FORCE",
      "EVENT",                "SPECIAL EVENT",
      "EXPLOD",               "EXPLOSION",
      "EXPLOD5",              "EXPLOSION",
      "FALRM",                "FIRE ALARM",
      "FALRMA",               "FIRE ALARM",
      "FDRILL",               "FIRE DRILL",
      "FIRE ALARM AFA",       "FIRE ALARM",
      "FIRE DRILL",           "FIRE DRILL",
      "FIRE TEST CALL",       "FIRE TEST CALL",
      "FLOOD",                "FLOODING CONDITIONS",
      "FTASK",                "FIRE TASK FORCE",
      "FTEST",                "FIRE TEST CALL",
      "FUEL SPILL",           "FUEL SPILL",
      "FUEL",                 "FUEL SPILL",
      "FWATCH",               "FIRE WATCH",
      "GASLK1",               "OUTSIDE GAS LEAK",
      "GASLK2",               "OUTSIDE GAS LEAK WITH SICK PEOPLE",
      "GASLK3",               "BUILDING GAS LEAK",
      "GASLK4",               "BUILDING GAS LEAK WITH SICK PEOPLE",
      "HARES",                "HIGH ANGLE RESCUE",
      "HARES4",               "HIGH ANGLE RESCUE",
      "HAZBOX",               "HAZMAT BOX",
      "HAZINV",               "HAZMAT INVESTIGATION",
      "HAZLOC",               "HAZMAT LOCAL",
      "HAZMAT",               "HAZMAT",
      "HAZSER",               "HAZMAT SERVICE CALL",
      "HELPF",                "HELP FIRE-MEDIC LOCAL",
      "HELPP",                "HELP POLICE-MEDIC LOCAL",
      "HITT",                 "HIT AND RUN WITH INJURIES",
      "HMTASK",               "HAZMAT TASK FORCE",
      "HOUSE FIRE",           "HOUSE FIRE",
      "HOUSE NATGAS LEAK",    "HOUSE NATURAL GAS LEAK",
      "HOUSEF",               "HOUSE FIRE",
      "HOUSEFR",              "HOUSE FIRE REDUCED ASSIGNMENT",
      "HOUSEFTEST",           "HOUSE FIRE TEST",
      "HOUSEG",               "HOUSE NATURAL GAS",
      "HOUSET",               "HOUSE FIRE WITH ENTRAPMENT",
      "INDUSA",               "INDUSTRIAL FARM ACCIDENT",
      "INDUSTRIAL FARM ACCI", "INDUSTRIAL FARM ACCI",
      "INSPEC",               "INSPECTION",
      "INVEST ANY TYPE",      "INVEST ANY TYPE",
      "INVEST",               "INVESTIGATION ANY TYPE",
      "INVEST1",              "FIRE ALARM",
      "INVEST2",              "OUTSIDE FIRE",
      "INVEST3",              "AUTO FIRE",
      "INVEST4",              "C O LEAK WITH SICK PEOPLE",
      "INVEST5",              "LOCK OUT WITH FOOD ON STOVE",
      "LOC",                  "LOCK OUT",
      "LOCK IN OUT",          "LOCK IN OUT",
      "MALRM",                "MEDICAL ALARM",
      "MEDIC LOCAL",          "MEDIC LOCAL",
      "MEDICAL ALARM",        "MEDICAL ALARM",
      "METRO",                "METRO STATION TRAIN",
      "METROF",               "METRO TRAIN FIRE",
      "METROM",               "METRO COMMAND REQUEST",
      "METROS",               "METRO TRAIN SUSICIDE",
      "MOTOR",                "MOTORCYCLE ACCIDENT",
      "MOTORCYCLE ACCIDENT",  "MOTORCYCLE ACCIDENT",
      "MTASK",                "MASS CASUALTY TASK FORCE",
      "OD",                   "OVERDOSE",
      "OOS",                  "OUT OF SERVICE",
      "OUT",                  "OUT OF SERVICE",
      "OUTF",                 "OUTSIDE FIRE",
      "OUTFI",                "OUTSIDE FIRE WITH INJURY",
      "OUTG",                 "GAS LEAK OUTSIDE",
      "OUTSID1",              "OUTSIDE/BRUSH FIRE NON-RURAL",
      "OUTSID3",              "BRUSH FIRE/RURAL",
      "OUTSIDE FIRE",         "OUTSIDE FIRE",
      "OVERA",                "OVERDOSE A L S",
      "OVERB",                "OVERDOSE B L S",
      "OVERDOSE",             "OVERDOSE",
      "PALNE1",               "AIRCRAFT DOWN INVESTIGATION",
      "PED",                  "PEDESTRIAN STRUCK",
      "PEDESTRIAN STRUCK",    "PEDESTRIAN STRUCK",
      "PIA LIMITED ACCESS",   "HIGHWAY ACCIDENT LIMITED ACCESS",
      "PIA W ENTRAPMENT",     "PERSONAL INJURY ACCIDENT W/ ENTRAPMENT",
      "PIA",                  "PERSONAL INJURY ACCIDENT",
      "PIAH",                 "HIGHWAY ACCIDENT",
      "PIAT",                 "PERSONAL INJURY ACCIDENT W/ ENTRAPMENT",
      "PLANE",                "PLANE CRASH",
      "PLANE0",               "LOW FLYING PLANE INCIDENT",
      "PLANE1",               "PLANE CRASH - UNCONFIRMED",
      "PLANE2",               "PLANE CRASH",
      "PLANE3",               "PLANE CRASH IN WATER",
      "PLANE4",               "PLANE CRASH",
      "PLANEW",               "PLANE CRASH IN WATER",
      "POOL",                 "POOL EMERGENCY",
      "RA ",                  "ASSIST POLICE",
      "RA",                   "ASSIST POLICE",
      "RAP",                  "INJURED PERSON ASSAULT",
      "REDSKN",               "REDSKIN INCIDENT",
      "RESCUE1",              "PERSONAL INJURY ACCIDENT",
      "RESCUE2",              "PERSONAL INJURY ACCIDENT WITH ENTRAPMENT",
      "RESCUE3",              "HIGHWAY ACCIDENT",
      "RESCUE4",              "HIGHWAY ACCIDENT WITH ENTRAPMENT",
      "RESCUE5",              "WWB-HIGHWAY ACCIDENT LIMITED ACCESS",
      "RESCUE6",              "HIGHWAY ACCIDENT LIMITED ACCESS",
      "RESCUE7",              "PERSONAL INJURY ACCIDENT A L S",
      "RTASK",                "R I C TASK FORCE",
      "SERV",                 "SERVICE CALL",
      "SERV1",                "NON-EMERGENT SERVICE CALL",
      "SERV2",                "NON-EMERGENT LOCK OUT",
      "SERVI",                "SERVICE CALL WITH INJURY",
      "SHOT",                 "SHOOTING",
      "STREET ALARM",         "STREET ALARM",
      "STREET",               "STREET ALARM",
      "STREETR",              "STREET ALARM REDUCED ASSIGNMENT",
      "STREETTEST",           "STREET ALARM TEST",
      "STRUCF1",              "STREET ALARM REDUCED ASSIGNMENT",
      "STRUCF2",              "STREET ALARM",
      "STRUCF3",              "STREET ALARM WITH INJURIES",
      "STRUCF4",              "BOX ALARM-STRUCTURE FIRE",
      "STRUCF5",              "BOX ALARM-STRUCTURE FIRE WITH ENTRAPMENT",
      "STRUCF6",              "HIGH RISE BUILDING FIRE",
      "STRUCF7",              "HIGH RISE BUILDING FIRE WITH ENTRAPMENT",
      "SUI",                  "SUICIDE",
      "SUICIDE",              "SUICIDE",
      "TOWNHF",               "TOWNHOUSE FIRE",
      "TOWNHFR",              "TOWNHOUSE FIRE REDUCED ASSIGNMENT",
      "TOWNHG",               "GAS LEAK IN TOWNHOUSE",
      "TOWNHOUSE FIRE",       "TOWNHOUSE FIRE",
      "TOWNHT",               "TOWNHOUSE FIRE WITH ENTRAPMENT",
      "TRAIN",                "TRAIN INCIDENT",
      "TRAINS",               "TRAIN INCIDENT",
      "TRANS ",               "TRANSFER",
      "TRANS",                "ROUTINE TRANSPORT",
      "TRANSFER",             "TRANSFER",
      "TRT",                  "TECHNICAL RESCUE TEAM",
      "TXFR",                 "TRANSFER",
      "UAD",                  "UNAVAILBLE UNITS",
      "UAS",                  "UNAVAILBLE UNITS",
      "UAT",                  "UNAVAILBLE UNITS",
      "UAV",                  "UNAVAILBLE UNITS",
      "UNA",                  "UNAVAILBLE",
      "WASHD",                "WASH DOWN",
      "WATER",                "WATER RESCUE",
      "WATER0",               "NON-EMERGENT WATER INCIDENT",
      "WATER1",               "VEHICLE IN THE WATER INVESTIGATION",
      "WATER2",               "ANIMAL IN THE WATER INCIDENT",
      "WATER3",               "POOL INCIDENT-DROWNING",
      "WATER4",               "WATER RESCUE/NON-POOL INCIDENT",
      "WATER5",               "WATER INCIDENT- TECHNICAL INCIDENT",
      "WATER6",               "PEOPLE IN THE WATER INCIDENT",
      "WATER7",               "BOAT FIRE- WATER OR DOCK",
      "WFD",                  "WORKING FIRE DISPATCH",
      "WIREDN",               "WIRE DOWN",
      "WORKING CODE",         "WORKING CODE",
      "WTASKH",               "WATER SUPPLY TASK FORCE",
      "WTASKN",               "WATER SUPPLY TASK FORCE NON HYDRANT"
      
      
  });
  
  private static final Properties MA_CITY_TABLE = buildCodeTable(new String[]{
      "MAAA", "ANNE ARUNDEL COUNTY",
      "MACA", "CALVERT COUNTY",
      "MAAL", "ALEXANDRIA COUNTY/VA",
      "MACC", "CALVERT COUNTY",
      "MACH", "CHARLES COUNTY",
      "MADC", "DC",
      "MAFA", "FAIRFAX COUNTY/VA",
//      "MAHC", null,    // ambiguous
      "MAHO", "HOWARD COUNTY",
      "MAMO", "MONTGOMERY COUNTY",
      "MAMC", "MONTGOMERY COUNTY",
      "MASM", "ST MARYS COUNTY"

  });
}
