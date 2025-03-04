package net.anei.cadpage.parsers.MD;

import java.util.Properties;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.anei.cadpage.parsers.MsgInfo.Data;
import net.anei.cadpage.parsers.MsgInfo.MsgType;


/**
 * Prince Georges County, MD (variant G)
 */
public class MDPrinceGeorgesCountyGParser extends MDPrinceGeorgesCountyBaseParser {

  public MDPrinceGeorgesCountyGParser() {
    super("CALL ADDR! APT? PLACE/CS+? X! CH? BOX Units:UNIT% UNIT+ Remarks:INFO");
  }

  @Override
  public String getFilter() {
    return "@alert.co.pg.md.us,@c-msg.net,14100,12101,@everbridge.net,87844,88911,8936";
  }

  private static final Pattern HTML_FILTER_PTN = Pattern.compile("(?:\n|<br>) {3,}<p> (.*?)</p>(?:\n|<br>) {3,}", Pattern.DOTALL);

  @Override
  protected boolean parseHtmlMsg(String subject, String body, Data data) {
    boolean keep = false;
    if (body.startsWith("<!doctype html>")) {
      Matcher match = HTML_FILTER_PTN.matcher(body);
      if (!match.find()) return false;
      body = match.group(1).trim();
      body = body.replace("\n<br>", " ").replace("\n", " ").replace("<br>", " ");
      keep = true;
    }

    if (super.parseHtmlMsg(subject, body, data)) return true;

    if (!keep) return false;
    setFieldList("INFO");
    data.msgType = MsgType.GEN_ALERT;
    data.strSupp = body;
    return true;
  }

  private static final Pattern ID_PTN = Pattern.compile("(PF\\d{14}): *");

  @Override
  public boolean parseMsg(String body, Data data) {

    body = stripFieldStart(body, "Dispatch\n");

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

  @Override
  public String adjustMapAddress(String addr) {
    return MDPrinceGeorgesCountyParser.adjustMapAddressCommon(addr);
  }

  private static final Properties CALL_CODES = buildCodeTable(new String[]{

      "2ALRM",                          "2ND ALARM FIRE",
      "3ALRM",                          "3RD ALARM FIRE",
      "4ALRM",                          "4TH ALARM FIRE",
      "5ALARM",                         "5TH ALARM FIRE",
      "ABDUCC",                         "AMBULANCE LOCAL - INJURED PERSON WITH POLICE-BLS",
      "ABDUCTION COMBINED",             "AMBULANCE LOCAL - INJURED PERSON WITH POLICE-BLS",
      "ABUSEC",                         "AMBULANCE LOCAL - INJURED PERSON WITH POLICE-BLS",
      "ACC W INJ",                      "RESCUE LOCAL - PERSONAL INJURY ACCIDENT-BLS",
      "ACCDC",                          "RESCUE LOCAL - DEPARTMENTAL ACCIDENT-ALS",
      "ACCFDC",                         "RESCUE LOCAL - DEPARTMENTAL ACCIDENT FIRE DEPARTMENT-ALS",
      "ACCHC",                          "RESCUE LOCAL - PERSONAL INJURY ACCIDENT HIGHWAY-BLS",
      "ACCIC",                          "RESCUE LOCAL - INDUSTRIAL ACCIDENT-ALS",
      "ACCMC",                          "RESCUE LOCAL - PERSONAL INJURY ACCIDENT-MOTORCYCLE-ALS",
      "ACCPC",                          "RESCUE LOCAL - PERSONAL INJURY ACCIDENT-PEDESTRIAN-ALS",
      "ACCSC",                          "RESCUE LOCAL - PERSONAL INJURY ACCIDENT-BLS",
      "ACTIVE ASSAILANT / SHOOTER",     "MEDIC LOCAL - ACTIVE SHOOTER WITH POLICE-ALS",
      "ACTIVE ASSAILANT 1",             "MEDIC LOCAL - ACTIVE SHOOTER WITH POLICE-ALS",
      "ACTIVE ASSAILANT 2",             "MEDIC LOCAL - ACTIVE SHOOTER WITH POLICE-ALS",
      "ACTIVEA1",                       "MEDIC LOCAL - ACTIVE SHOOTER WITH POLICE-ALS",
      "ACTIVEA2",                       "MEDIC LOCAL - ACTIVE SHOOTER WITH POLICE-ALS",
      "ACTSHT",                         "MEDIC LOCAL - ACTIVE SHOOTER WITH POLICE-ALS",
      "AIRCRAFT CRASH",                 "RESCUE LOCAL - PLANE CRASH-ALS",
      "AIRCRAFT IN WATER",              "RESCUE LOCAL - PLANE CRASH IN WATER-ALS",
      "ALS COMBINED",                   "MEDIC LOCAL - ALS1 WITH POLICE",
      "ALS",                            "MEDIC LOCAL - ALS1",
      "ALS+",                           "MEDIC LOCAL - ALS+",
      "ALS0",                           "MEDIC LOCAL - ALS0",
      "ALS1",                           "MEDIC LOCAL - ALS1",
      "ALS2",                           "MEDIC LOCAL - ALS2",
      "ALSC",                           "MEDIC LOCAL - ALS1 WITH POLICE",
      "ALSM",                           "MEDIC LOCAL",
      "ANIMAL COMPLAINT COMBINED",      "AMBULANCE LOCAL - ANIMAL BITE WITH POLICE-BLS",
      "ANIMLC",                         "AMBULANCE LOCAL - ANIMAL BITE WITH POLICE-BLS",
      "APARTMENT FIRE",                 "BOX ALARM - STRUCTURE FIRE",
      "APT FIRE REDUCED",               "STREET ALARM",
      "APT FIRE W TRAPPED",             "BOX ALARM - STRUCTURE FIRE WITH REPORTED ENTRAPMENT-ALS",
      "APT NATURAL GAS LK",             "STREET ALARM - INSIDE GAS LEAK",
      "APTF",                           "BOX ALARM - STRUCTURE FIRE",
      "APTFR",                          "BOX ALARM-STRUCTURE FIRE-REDUCED ASSIGNMENT",
      "APTFR",                          "STREET ALARM",
      "APTG",                           "STREET ALARM - INSIDE GAS LEAK",
      "APTT",                           "BOX ALARM - STRUCTURE FIRE WITH REPORTED ENTRAPMENT-ALS",
      "ASALT",                          "AMBULANCE LOCAL - INJURED PERSON ASSAULT WITH POLICE-BLS",
      "ASALTA",                         "MEDIC LOCAL - INJURED PERSON ASSAULT WITH POLICE-ALS",
      "ASLTAC",                         "MEDIC LOCAL - INJURED PERSON ASSAULT WITH POLICE-ALS",
      "ASLTBC",                         "AMBULANCE LOCAL - INJURED PERSON ASSAULT WITH POLICE-BLS",
      "ASLTC",                          "AMBULANCE LOCAL - INJURED PERSON ASSAULT WITH POLICE-BLS",
      "ASPD",                           "LOCAL ALARM - ASSIST POLICE",
      "ASSAULT COMBINED",               "AMBULANCE LOCAL - INJURED PERSON ASSAULT WITH POLICE-BLS",
      "ASSAULT",                        "AMBULANCE LOCAL - INJURED PERSON ASSAULT WITH POLICE-BLS",
      "ASSIST POLICE",                  "LOCAL ALARM - ASSIST POLICE",
      "ATT SUICIDE COMBINED",           "AMBULANCE LOCAL -  SUICIDE ATTEMPT WITH POLICE-BLS",
      "AUTO FIRE W TRAPPED",            "RESCUE LOCAL - AUTO FIRE WITH REPORTED ENTRAPMENT-ALS",
      "AUTO FIRE",                      "LOCAL ALARM - AUTO FIRE",
      "AUTOF",                          "LOCAL ALARM - AUTO FIRE",
      "AUTOFT",                         "RESCUE LOCAL - AUTO FIRE WITH REPORTED ENTRAPMENT-ALS",
      "BARI",                           "MEDIC LOCAL - STANDBY WITH POLICE-ALS",
      "BARRIC",                         "MEDIC LOCAL - STANDBY WITH POLICE-ALS",
      "BARRICADE COMBINED",             "MEDIC LOCAL - STANDBY WITH POLICE-ALS",
      "BARRICADE",                      "MEDIC LOCAL - STANDBY WITH POLICE-ALS",
      "BLS AMB COMBINED",               "AMBULANCE LOCAL",
      "BLS AMB",                        "AMBULANCE LOCAL - BLS",
      "BLS COMBINED",                   "AMBULANCE LOCAL - BLS1 WITH POLICE",
      "BLS",                            "AMBULANCE LOCAL - BLS1",
      "BLS+",                           "AMBULANCE LOCAL - BLS+",
      "BLS0",                           "AMBULANCE LOCAL - BLS0",
      "BLS1",                           "AMBULANCE LOCAL - BLS1",
      "BLSC",                           "AMBULANCE LOCAL - BLS1 WITH POLICE",
      "BOAT FIRE",                      "LOCAL ALARM - BOAT FIRE",
      "BOMB",                           "BOMB INCIDENT",
      "BOMB0",                          "BOMB INCIDENT",
      "BOMB1",                          "BOMB/HAZARDOUS DEVICE INVESTIGATION",
      "BOMB2",                          "BOMB/HAZARDOUS DEVICE SUSPECTED",
      "BOMB3",                          "BOMB/HAZARDOUS DEVICE",
      "BOMT",                           "BOMB THREAT",
      "BRUSH FIRE ENHANCED",            "LOCAL ALARM - BRUSH FIRE-ENHANCED ASSIGNMENT",
      "BRUSH FIRE",                     "LOCAL ALARM - BRUSH FIRE",
      "BRUSH",                          "BRUSH FIRE",
      "BRUSH",                          "LOCAL ALARM - BRUSH FIRE",
      "BRUSHE",                         "LOCAL ALARM - BRUSH FIRE-ENHANCED ASSIGNMENT",
      "BTFIRE",                         "LOCAL ALARM - BOAT FIRE",
      "BTINV",                          "LOCAL ALARM - BOAT INVESTIGATION",
      "BUILDF",                         "BOX ALARM - STRUCTURE FIRE",
      "BUILDFR",                        "STREET ALARM",
      "BUILDG",                         "STREET ALARM - INSIDE GAS LEAK",
      "BUILDING FIRE   REDU",           "STREET ALARM",
      "BUILDING FIRE W TRAP",           "BOX ALARM - STRUCTURE FIRE WITH REPORTED ENTRAPMENT-ALS",
      "BUILDING FIRE",                  "BOX ALARM - STRUCTURE FIRE",
      "BUILDING NAT GAS LK",            "STREET ALARM - INSIDE GAS LEAK",
      "BUILDT",                         "BOX ALARM - STRUCTURE FIRE WITH REPORTED ENTRAPMENT-ALS",
      "BUSY",                           "BUSY",
      "CARJACKING REPORT COMBINED",     "AMBULANCE LOCAL - INJURED PERSON ASSAULT WITH POLICE-BLS",
      "CARJKC",                         "AMBULANCE LOCAL - INJURED PERSON ASSAULT WITH POLICE-BLS",
      "CAVE IN COMBINED",               "RESCUE LOCAL - CONFINE SPACE OR TRENCH RESCUE-ALS",
      "CAVEC",                          "RESCUE LOCAL - CONFINE SPACE-TRENCH RESCUE-ALS",
      "CHECK WELFARE COMBINED",         "AMBULANCE LOCAL - CHECK WELFARE WITH POLICE-BLS",
      "CIT ROBBERY COMBINED",           "AMBULANCE LOCAL - INJURED PERSON ASSAULT WITH POLICE-BLS",
      "CKWELC",                         "AMBULANCE LOCAL - CHECK WELFARE WITH POLICE-BLS",
      "CO ALARM",                       "LOCAL ALARM - CO ALARM",
      "CO LEAK W  SICK PEOP",           "STREET ALARM - CO LEAK WITH SICK PEOPLE-ALS",
      "COALRM",                         "LOCAL ALARM - CO ALARM",
      "COLAP0",                         "LOCAL ALARM - COLLAPSE SERVICE CALL",
      "COLAP1",                         "LOCAL ALARM - TREE ON HOUSE INVESTIGATION",
      "COLAP2",                         "RESCUE LOCAL - COLLAPSE INVESTIGATION-BLS",
      "COLAP3",                         "RESCUE LOCAL - COLLAPSE-ALS",
      "COLAP4",                         "RESCUE LOCAL - COLLAPSE OVER WATER-ALS",
      "COLAP5",                         "RESCUE LOCAL - CONFINE SPACE OR TRENCH RESCUE-ALS",
      "COLAPI",                         "RESCUE LOCAL - COLLAPSE INVESTIGATION-BLS",
      "COLAPS",                         "RESCUE LOCAL - CONFINE SPACE OR TRENCH RESCUE-ALS",
      "COLEAK",                         "STREET ALARM - CO LEAK WITH SICK PEOPLE-ALS",
      "COLLAPSE INVEST",                "RESCUE LOCAL - COLLAPSE INVESTIGATION-BLS",
      "COLLAPSE",                       "RESCUE LOCAL - CONFINE SPACE OR TRENCH RESCUE-ALS",
      "CONFINED SPACE RESCU",           "RESCUE LOCAL - CONFINE SPACE OR TRENCH RESCUE-ALS",
      "CONFSP",                         "RESCUE LOCAL - CONFINE SPACE OR TRENCH RESCUE-ALS",
      "CPR COMBINED",                   "MEDIC LOCAL - CARDIAC ARREST-ALS2",
      "CPR",                            "MEDIC LOCAL - CARDIAC ARREST-ALS2",
      "CPRC",                           "MEDIC LOCAL - CARDIAC ARREST WITH POLICE-ALS2",
      "CROSS BURNING COMBINED",         "LOCAL ALARM -OUTSIDE FIRE WITH POLICE",
      "CROSSC",                         "LOCAL ALARM - OUTSIDE FIRE WITH POLICE",
      "CUTC",                           "MEDIC LOCAL - CUTTING WITH POLICE-ALS",
      "CUTT",                           "MEDIC LOCAL - CUTTING WITH POLICE-ALS",
      "CUTTING COMBINED",               "MEDIC LOCAL - CUTTING WITH POLICE-ALS",
      "CUTTING STABBING",               "MEDIC LOCAL - CUTTING WITH POLICE-ALS",
      "CVA ABUSE COMBINED",             "AMBULANCE LOCAL - INJURED PERSON WITH POLICE-BLS",
      "DEATH REPORT COMBINED",          "AMBULANCE LOCAL - DOA WITH POLICE-BLS",
      "DEATHC",                         "AMBULANCE LOCAL - DOA WITH POLICE-BLS",
      "DEP ACCI",                       "DEPARTMENTAL ACCIDENT",
      "DEP",                            "RESCUE LOCAL - DEPARTMENTAL ACCIDENT-ALS",
      "DEPARTMENTAL ACCI",              "RESCUE LOCAL - DEPARTMENTAL ACCIDENT-ALS",
      "DEPFD",                          "RESCUE LOCAL - DEPARTMENTAL ACCIDENT-FIRE DEPARTMENT-ALS",
      "DEPT ACCIDENT FD COMBINED",      "RESCUE LOCAL - DEPARTMENTAL ACCIDENT FIRE DEPARTMENT-ALS",
      "DEPT ACCIDENT PD COMBINED",      "RESCUE LOCAL - DEPARTMENTAL ACCIDENT- POLICE-ALS",
      "DEVICE/PACKAGE - BOMB0 RESP",    "DEVICE/PACKAGE - BOMB0 RESP",
      "DEVICE/PACKAGE - BOMB1 RESP",    "DEVICE/PACKAGE - BOMB1 RESP",
      "DEVICE/PACKAGE - BOMB2 RESP",    "DEVICE/PACKAGE - BOMB2 RESP",
      "DEVICE/PACKAGE - BOMB3 RESP",    "DEVICE/PACKAGE - BOMB3 RESP",
      "DEVICE/PKG/THREAT COMBINED",     "DEVICE/PKG/THREAT WITH POLICE",
      "DOA COMBINED",                   "AMBULANCE LOCAL - DOA WITH POLICE-BLS",
      "DOAC",                           "AMBULANCE LOCAL - DOA WITH POLICE-BLS",
      "DOMESC",                         "AMBULANCE LOCAL - INJURED PERSON ASSAULT WITH POLICE-BLS",
      "DOMESTIC COMBINED",              "AMBULANCE LOCAL - INJURED PERSON ASSAULT WITH POLICE-BLS",
      "DROWNC",                         "MEDIC LOCAL - DROWNING WITH POLICE-ALS",
      "DROWNING COMBINED",              "MEDIC LOCAL - DROWNING WITH POLICE-ALS",
      "DSTRB",                          "JAIL DISTURBANCE",
      "DSTURB",                         "MEDIC LOCAL - JAIL DISTURBANCE WITH POLICE-ALS",
      "ELEV",                           "LOCAL ALARM - STUCK ELEVATOR",
      "ELEVATOR ENTRAPMENT",            "RESCUE LOCAL - STUCK ELEVATOR WITH ENTRAPMENT-ALS",
      "ELEVATOR INJURIES",              "RESCUE LOCAL - STUCK ELEVATOR WITH INJURED PERSON-BLS",
      "ELEVI",                          "RESCUE LOCAL - STUCK ELEVATOR WITH INJURED PERSON-BLS",
      "ELEVT",                          "RESCUE LOCAL - STUCK ELEVATOR WITH ENTRAPMENT-ALS",
      "EMS TASK FORCE",                 "MEDIC LOCAL - EMS TASK FORCE",
      "ESCAL",                          "LOCAL ALARM - ESCALATOR INCIDENT",
      "ESCALATOR ENTRAPMENT",           "RESCUE LOCAL - ESCALATOR INCIDENT WITH ENTRAPMENT-ALS",
      "ESCALATOR INCIDENT",             "LOCAL ALARM - ESCALATOR INCIDENT",
      "ESCALT",                         "RESCUE LOCAL - ESCALATOR INCIDENT WITH ENTRAPMENT-ALS",
      "ETASK",                          "MEDIC LOCAL - EMS TASK FORCE",
      "EVENT",                          "SPECIAL EVENT",
      "EXPLOC",                         "BOX ALARM - STUCTURE FIRE WITH EXPLOSION",
      "EXPLOD",                         "BOX ALARM - STUCTURE FIRE WITH EXPLOSION",
      "EXPLOD5",                        "BOX ALARM - STUCTURE FIRE WITH EXPLOSION",
      "EXPLOSION COMBINED",             "BOX ALARM - STUCTURE FIRE WITH EXPLOSION WITH POLICE",
      "EXPLOSION",                      "BOX ALARM - STUCTURE FIRE WITH EXPLOSION",
      "FALRM",                          "LOCAL ALARM - FIRE ALARM",
      "FALRMA",                         "LOCAL ALARM - FIRE AND MEDICAL ALARM WITH POLICE-BLS",
      "FDRILL",                         "FIRE DRILL",
      "FIGHT COMBINED",                 "AMBULANCE LOCAL - INJURED PERSON ASSAULT WITH POLICE-BLS",
      "FIGHTC",                         "AMBULANCE LOCAL - INJURED PERSON ASSAULT WITH POLICE-BLS",
      "FIRE ALARM AFA",                 "LOCAL ALARM - FIRE ALARM",
      "FIRE DRILL",                     "FIRE DRILL",
      "FIRE TASK FORCE",                "FIRE TASK FORCE",
      "FIRE TEST CALL",                 "FIRE TEST CALL",
      "FIRE WATCH",                     "FIRE WATCH",
      "FLOOD",                          "LOCAL ALARM - INSIDE FLOODING CONDITIONS",
      "FLOODING CONDITIONS",            "LOCAL ALARM - INSIDE FLOODING CONDITIONS",
      "FTASK",                          "FIRE TASK FORCE",
      "FTEST",                          "FIRE TEST CALL",
      "FUEL SPILL",                     "LOCAL ALARM - FUEL SPILL",
      "FUEL",                           "LOCAL ALARM - FUEL SPILL",
      "FWATCH",                         "FIRE WATCH",
      "GASLK1",                         "LOCAL ALARM - OUTSIDE GAS LEAK",
      "GASLK2",                         "LOCAL ALARM - OUTSIDE GAS LEAK WITH SICK PEOPLE-BLS",
      "GASLK3",                         "STREET ALARM - INSIDE GAS LEAK",
      "GASLK4",                         "STREET ALARM - INSIDE GAS LEAK WITH SICK PEOPLE-ALS",
      "HARES",                          "RESCUE LOCAL - HIGH ANGLE RESCUE OR OVERLAND RESCUE-ALS",
      "HARES4",                         "RESCUE LOCAL - CONFIRMED HIGH ANGLE OR OVERLAND RESCUE-ALS",
      "HARESC",                         "RESCUE LOCAL - HIGH ANGLE RESCUE OR OVERLAND RESCUE WITH POLICE-ALS",
      "HAZBOX",                         "HAZMAT BOX ALARM",
      "HAZINV",                         "HAZMAT LOCAL - HAZMAT INVESTIGATION",
      "HAZLOC",                         "HAZMAT LOCAL ALARM",
      "HAZMAT BOX",                     "HAZMAT BOX ALARM",
      "HAZMAT CALL",                    "HAZMAT LOCAL - HAZMAT CALL",
      "HAZMAT INVESTIGATION",           "LOCAL ALARM - HAZMAT INVESTIGATION",
      "HAZMAT LOCAL",                   "HAZMAT LOCAL ALARM",
      "HAZMAT SERVICE CALL",            "HAZMAT LOCAL - HAZMAT SERVICE CALL",
      "HAZMAT TASK FORCE",              "HAZMAT TASK FORCE",
      "HAZMAT",                         "HAZMAT LOCAL - HAZMAT CALL",
      "HAZSER",                         "HAZMAT LOCAL - HAZMAT SERVICE CALL",
      "HELPC",                          "MEDIC LOCAL - SIGNAL 13 WITH POLICE-ALS",
      "HELPF",                          "HELP FIRE-MEDIC LOCAL",
      "HELPP",                          "MEDIC LOCAL - SIGNAL 13 WITH POLICE-ALS",
      "HIGH ANGLE RESCUE COMBINED",     "RESCUE LOCAL - HIGH ANGLE RESCUE OR OVERLAND RESCUE WITH POLICE-ALS",
      "HIGH ANGLE RESCUE",              "RESCUE LOCAL - HIGH ANGLE RESCUE OR OVERLAND RESCUE-ALS",
      "HIGHWAY ACCIDENT COMBINED",      "RESCUE LOCAL - PERSONAL INJURY ACCIDENT HIGHWAY-BLS",
      "HIT AND RUN W INJURIES",         "RESCUE LOCAL - PERSONAL INJURY ACCIDENT-BLS",
      "HIT AND RUN W/INJURY COMBINED",  "RESCUE LOCAL - PERSONAL INJURY ACCIDENT-BLS",
      "HITIC",                          "RESCUE LOCAL - PERSONAL INJURY ACCIDENT-BLS",
      "HITT",                           "RESCUE LOCAL - PERSONAL INJURY ACCIDENT-BLS",
      "HMTASK",                         "HAZMAT TASK FORCE",
      "HOUSE FIRE   REDUCED",           "STREET ALARM",
      "HOUSE FIRE W TRAPPED",           "BOX ALARM - STRUCTURE FIRE WITH REPORTED ENTRAPMENT-ALS",
      "HOUSE FIRE",                     "BOX ALARM - STRUCTURE FIRE",
      "HOUSE NATGAS LEAK",              "STREET ALARM - INSIDE GAS LEAK",
      "HOUSEF TEST",                    "HOUSE FIRE TEST",
      "HOUSEF",                         "BOX ALARM - STRUCTURE FIRE",
      "HOUSEFR",                        "STREET ALARM",
      "HOUSEFTEST",                     "BOX ALARM-STRUCTURE FIRE-TEST",
      "HOUSEG",                         "STREET ALARM - INSIDE GAS LEAK",
      "HOUSET",                         "BOX ALARM - STRUCTURE FIRE WITH REPORTED ENTRAPMENT-ALS",
      "INDUSA",                         "RESCUE LOCAL - INDUSTRIAL ACCIDENT-ALS",
      "INDUSTRIAL ACCIDENT COMBINED",   "RESCUE LOCAL - INDUSTRIAL ACCIDENT WITH POLICE-ALS",
      "INDUSTRIAL FARM ACCI",           "RESCUE LOCAL - INDUSTRIAL ACCIDENT-ALS",
      "INSPEC",                         "INSPECTION",
      "INSPECTION",                     "INSPECTION",
      "INVEST 3 COMBINED",              "LOCAL ALARM - INVESTIGATION 3 WITH POLICE",
      "INVEST ANY TYPE",                "LOCAL ALARM - INVESTIGATION 1",
      "INVEST",                         "LOCAL ALARM - INVESTIGATION 1",
      "INVEST1",                        "LOCAL ALARM - INVESTIGATION 1",
      "INVEST2",                        "LOCAL ALARM - INVESTIGATION 2",
      "INVEST3",                        "LOCAL ALARM - INVESTIGATION 3",
      "INVEST3C",                       "LOCAL ALARM - INVESTIGATION 3 WITH POLICE",
      "INVEST4",                        "LOCAL ALARM - C O LEAK WITH SICK PEOPLE-ALS",
      "INVEST5",                        "LOCAL ALARM - LOCKOUT WITH FOOD ON STOVE WITH POLICE",
      "JAIL DISTURBANCE",               "MEDIC LOCAL - JAIL DISTURBANCE WITH POLICE-ALS",
      "LOC",                            "LOCAL ALARM - LOCKOUT",
      "LOCK IN OUT",                    "LOCAL ALARM - LOCKOUT",
      "LOCK OUT COMBINED",              "LOCAL ALARM - LOCKOUT WITH POLICE",
      "LOCK OUT",                       "LOCAL ALARM - LOCKOUT",
      "LOCK OUT/IN COMBINED",           "LOCAL ALARM - LOCKOUT WITH POLICE",
      "LOCKC",                          "LOCAL ALARM - LOCKOUT WITH POLICE",
      "MALRM",                          "AMBULANCE LOCAL - MEDICAL ALARM-BLS",
      "MASS CASUALTY T F",              "MASS CASUALTY TASK FORCE",
      "MATASK",                         "MUTUAL AID TASK FORCE",
      "MEDIC LOCAL",                    "MEDIC LOCAL - ALS1",
      "MEDICAL ALARM",                  "AMBULANCE LOCAL - MEDICAL ALARM-BLS",
      "METRO COMMAND RQST",             "LOCAL ALARM - METRO COMMAND REQUEST",
      "METRO PED/STRUCK",               "RESCUE LOCAL - PEDESTRIAN STRUCK BY A METRO TRAIN WITH POLICE-ALS",
      "METRO STATION TRAIN",            "BOX ALARM - METRO TRAIN DERAILMENT OR FIRE",
      "METRO TRAIN FIRE",               "BOX ALARM - METRO TRAIN DERAILMENT OR FIRE",
      "METRO",                          "BOX ALARM - METRO TRAIN DERAILMENT OR FIRE",
      "METROF",                         "BOX ALARM - METRO TRAIN DERAILMENT OR FIRE",
      "METROM",                         "LOCAL ALARM - METRO COMMAND REQUEST",
      "METROS",                         "RESCUE LOCAL - PEDESTRIAN STRUCK BY A METRO TRAIN WITH POLICE-ALS",
      "MOTOR",                          "RESCUE LOCAL - PERSONAL INJURY ACCIDENT-ALS",
      "MOTORCYCLE ACCIDENT COMBINED",   "RESCUE LOCAL - PERSONAL INJURY ACCIDENT WITH POLICE-ALS",
      "MOTORCYCLE ACCIDENT",            "RESCUE LOCAL - PERSONAL INJURY ACCIDENT-ALS",
      "MTASK",                          "MASS CASUALTY TASK FORCE",
      "NON EMERG SERVICE",              "LOCAL ALARM - SERVICE CALL 1",
      "OD",                             "AMBULANCE LOCAL - OVERDOSE WITH POLICE-BLS",
      "ODAC",                           "MEDIC LOCAL - OVERDOSE WITH POLICE-ALS",
      "ODBC",                           "AMBULANCE LOCAL - OVERDOSE WITH POLICE-BLS",
      "OOS",                            "OUT OF SERVICE",
      "OUT OF SERVICE",                 "OUT OF SERVICE",
      "OUT",                            "OUT OF SERVICE",
      "OUTF",                           "LOCAL ALARM - OUTSIDE FIRE",
      "OUTFI",                          "LOCAL ALARM - OUTSIDE FIRE WITH INJURIES-ALS",
      "OUTG",                           "LOCAL ALARM - OUTSIDE GAS LEAK",
      "OUTSID1",                        "LOCAL ALARM - BRUSH OR TRASH OUTSIDE FIRE",
      "OUTSID2",                        "LOCAL ALARM - BRUSH FIRE ENHANCED ASSIGNMENT",
      "OUTSID3",                        "LOCAL ALARM - BRUSH FIRE HIGH HAZARD CONDITIONS",
      "OUTSID4",                        "LOCAL ALARM - OUTSIDE FIRE WITH INJURIES-ALS",
      "OUTSIDE FIRE W INJ",             "LOCAL ALARM - OUTSIDE FIRE WITH INJURIES-ALS",
      "OUTSIDE FIRE",                   "LOCAL ALARM - OUTSIDE FIRE",
      "OUTSIDE GAS LEAK",               "LOCAL ALARM - OUTSIDE GAS LEAK",
      "OVERA",                          "MEDIC LOCAL - OVERDOSE WITH POLICE-ALS",
      "OVERB",                          "AMBULANCE LOCAL - OVERDOSE WITH POLICE-BLS",
      "OVERDC",                         "AMBULANCE LOCAL - OVERDOSE WITH POLICE-BLS",
      "OVERDOSE ALS COMBINED",          "MEDIC LOCAL - OVERDOSE WITH POLICE-ALS",
      "OVERDOSE BLS COMBINED",          "AMBULANCE LOCAL - OVERDOSE WITH POLICE-BLS",
      "OVERDOSE COMBINED",              "AMBULANCE LOCAL - OVERDOSE WITH POLICE-BLS",
      "OVERDOSE",                       "AMBULANCE LOCAL - OVERDOSE WITH POLICE-BLS",
      "PED",                            "RESCUE LOCAL - PERSONAL INJURY ACCIDENT PEDESTRIAN-ALS",
      "PEDESTRIAN STRUCK COMBINED",     "RESCUE LOCAL - PERSONAL INJURY ACCIDENT PEDESTRIAN  WITH POLICE-ALS",
      "PEDESTRIAN STRUCK",              "RESCUE LOCAL - PERSONAL INJURY ACCIDENT PEDESTRIAN-ALS",
      "PERSONAL INJURY ACCIDENT",       "RESCUE LOCAL - PERSONAL INJURY ACCIDENT-BLS",
      "PIA LIMITED ACCESS",             "RESCUE LOCAL - PERSONAL INJURY ACCIDENT HIGHWAY-BLS",
      "PIA W ENTRAPMENT",               "RESCUE LOCAL - PERSONAL INJURY ACCIDENT WITH ENTRAPMENT-ALS",
      "PIA",                            "RESCUE LOCAL - PERSONAL INJURY ACCIDENT-BLS",
      "PIAH",                           "RESCUE LOCAL - PERSONAL INJURY ACCIDENT HIGHWAY-BLS",
      "PIAT",                           "RESCUE LOCAL - PERSONAL INJURY ACCIDENT WITH ENTRAPMENT-ALS",
      "PLANE CRASH COMBINED",           "RESCUE LOCAL - PLANE CRASH WITH POLICE-ALS",
      "PLANE",                          "RESCUE LOCAL - PLANE CRASH-ALS",
      "PLANE0",                         "LOCAL ALARM - REPORT OF A LOW FLYING AIRCRAFT",
      "PLANE1",                         "RESCUE LOCAL - PLANE CRASH INVESTIGATION-BLS",
      "PLANE2",                         "RESCUE LOCAL - PLANE CRASH SMALL AIRCRAFT-ALS",
      "PLANE3",                         "RESCUE LOCAL - PLANE CRASH IN WATER-ALS",
      "PLANE4",                         "RESCUE LOCAL - PLANE CRASH LARGE AIRCRAFT-ALS",
      "PLANE5",                         "RESCUE LOCAL - PLANE CRASH-ALS",
      "PLANE6",                         "RESCUE LOCAL - PLANE CRASH-ALS",
      "PLANEC",                         "RESCUE LOCAL - PLANE CRASH WITH POLICE-ALS",
      "PLANEW",                         "RESCUE LOCAL - PLANE CRASH IN WATER-ALS",
      "POOL",                           "RESCUE LOCAL - WATER RESCUE POOL DROWNING-ALS",
      "RA",                             "AMBULANCE LOCAL - INJURED PERSON ASSAULT WITH POLICE-BLS",
      "RAP",                            "AMBULANCE LOCAL - INJURED PERSON ASSAULT WITH POLICE-BLS",
      "RAPE",                           "AMBULANCE LOCAL - INJURED PERSON ASSAULT WITH POLICE-BLS",
      "RAPEC",                          "AMBULANCE LOCAL - INJURED PERSON ASSAULT WITH POLICE-BLS",
      "REDSKN",                         "REDSKIN INCIDENT",
      "REQUEST ASSISTANCE",             "REQUEST ASSISTANCE",
      "RESCUE1",                        "RESCUE LOCAL - PERSONAL INJURY ACCIDENT-BLS",
      "RESCUE2",                        "RESCUE LOCAL - PERSONAL INJURY ACCIDENT WITH ENTRAPMENT-ALS",
      "RESCUE3",                        "RESCUE LOCAL - PERSONAL INJURY ACCIDENT HIGHWAY-BLS",
      "RESCUE4",                        "RESCUE LOCAL - PERSONAL INJURY ACCIDENT HIGHWAY WITH ENTRAPMENT-ALS",
      "RESCUE5",                        "RESCUE LOCAL - PERSONAL INJURY ACCIDENT WILSON BRIDGE-BLS",
      "RESCUE6",                        "RESCUE LOCAL - PERSONAL INJURY ACCIDENT WILSON BRIDGE WITH ENTRAPMENT-ALS",
      "RESCUE7",                        "RESCUE LOCAL - PERSONAL INJURY ACCIDENT-ALS",
      "RIC TASK FORCE",                 "RAPID INTERVENTION TASK FORCE",
      "RICTSK",                         "RIC TASK FORCE",
      "ROBBC",                          "AMBULANCE LOCAL - INJURED PERSON ASSAULT WITH POLICE-BLS",
      "ROBBERY COMBINED",               "AMBULANCE LOCAL - INJURED PERSON ASSAULT WITH POLICE-BLS",
      "ROBCITC",                        "AMBULANCE LOCAL - INJURED PERSON ASSAULT WITH POLICE-BLS",
      "ROBT/AC",                        "AMBULANCE LOCAL - INJURED PERSON ASSAULT WITH POLICE-BLS",
      "ROUTINE AMB TX",                 "AMBULANCE LOCAL - ROUTINE TRANSPORT-BLS",
      "RTASK",                          "RIC TASK FORCE",
      "S9DC",                           "RESCUE LOCAL - SHERIFF DEPARTMENTAL ACCIDENT-ALS",
      "SACCDC",                         "RESCUE LOCAL - SHERIFF DEPARTMENTAL ACCIDENT-ALS",
      "SACCSC",                         "RESCUE LOCAL - SHERIFF DEPARTMENTAL ACCIDENT-BLS",
      "SERV",                           "LOCAL ALARM - SERVICE CALL 1",
      "SERV1",                          "LOCAL ALARM - SERVICE CALL 1",
      "SERV2",                          "LOCAL ALARM - SERVICE CALL 2 WITH POLICE",
      "SERV3",                          "LOCAL ALARM - HELICOPTER STANDBY",
      "SERVI",                          "AMBULANCE LOCAL - SERVICE CALL WITH INJURED PERSON-BLS",
      "SERVICE CALL",                   "LOCAL ALARM - SERVICE CALL 1",
      "SERVICE W  INJ SICK",            "AMBULANCE LOCAL - SERVICE CALL WITH INJURED PERSON-BLS",
      "SEXUAL ASSAULT COMBINED",        "AMBULANCE LOCAL - INJURED PERSON ASSAULT WITH POLICE-BLS",
      "SFIRETEST",                      "STREET FIRE TEST",
      "SHELPC",                         "MEDIC LOCAL - SIGNAL 13 WITH SHERIFF-ALS",
      "SHERIFF ACCIDENT COMBINED",      "RESCUE LOCAL - SHERIFF DEPARTMENTAL ACCIDENT-ALS",
      "SHERIFF DEPT ACCIDENT COMBINED", "RESCUE LOCAL - SHERIFF DEPARTMENTAL ACCIDENT-ALS",
      "SHERIFF HELP COMBINED",          "MEDIC LOCAL - SIGNAL 13 WITH SHERIFF-ALS",
      "SHOOTC",                         "MEDIC LOCAL - SHOOTING WITH POLICE-ALS",
      "SHOOTING COMBINED",              "MEDIC LOCAL - SHOOTING WITH POLICE-ALS",
      "SHOOTING",                       "MEDIC LOCAL - SHOOTING WITH POLICE-ALS",
      "SHOT",                           "MEDIC LOCAL - SHOOTING WITH POLICE-ALS",
      "SIGNAL 13 COMBINED",             "MEDIC LOCAL - SIGNAL 13 WITH POLICE-ALS",
      "STADIUM EVENT",                  "STADIUM EVENT",
      "STREET ALARM",                   "STREET ALARM",
      "STREET ALRM   REDUCE",           "LOCAL ALARM - REDUCED STREET ALARM",
      "STREET",                         "STREET ALARM",
      "STREETR",                        "LOCAL ALARM - REDUCED STREET ALARM",
      "STRUCF0",                        "LOCAL ALARM - INVESTIGATION",
      "STRUCF1",                        "LOCAL ALARM - INSIDE INVESTIGATION",
      "STRUCF2",                        "STREET ALARM",
      "STRUCF3",                        "STREET ALARM WITH INJURIES-ALS",
      "STRUCF4",                        "BOX ALARM - STRUCTURE FIRE",
      "STRUCF5",                        "BOX ALARM - STRUCTURE FIRE WITH REPORTED ENTRAPMENT-ALS",
      "STRUCF6",                        "BOX ALARM - HIGH RISE STRUCTURE FIRE",
      "STRUCF7",                        "BOX ALARM - HIGH RISE STRUCTURE FIRE WITH REPORTED-ENTRAPMENT ALS",
      "STUCK ELEVATOR",                 "LOCAL ALARM - STUCK ELEVATOR",
      "SUI",                            "AMBULANCE LOCAL - SUICIDE WITH POLICE-BLS",
      "SUICIC",                         "AMBULANCE LOCAL - SUICIDE WITH POLICE-BLS",
      "SUICIDE",                        "AMBULANCE LOCAL - SUICIDE WITH POLICE-BLS",
      "TA ROBBERY COMBINED",            "AMBULANCE LOCAL - INJURED PERSON ASSAULT WITH POLICE-BLS",
      "TECHNICAL RESCUE T F",           "TECHNICAL RESCUE T F",
      "TEST DO NOT DISPATCH",           "TEST DO NOT DISPATCH",
      "TEXT REQUEST FOR EMER SERV",     "TEXT REQUEST FOR EMER SERV",
      "TOWNHF",                         "BOX ALARM - STRUCTURE FIRE",
      "TOWNHFR",                        "STREET ALARM",
      "TOWNHG",                         "STREET ALARM - INSIDE GAS LEAK",
      "TOWNHOUSE FIRE W  TR",           "BOX ALARM - STRUCTURE FIRE WITH REPORTED ENTRAPMENT-ALS",
      "TOWNHOUSE FIRE",                 "BOX ALARM - STRUCTURE FIRE",
      "TOWNHOUSE NATGAS LK",            "STREET ALARM - INSIDE GAS LEAK",
      "TOWNHSE FIRE   REDUC",           "STREET ALARM",
      "TOWNHT",                         "BOX ALARM - STRUCTURE FIRE WITH REPORTED ENTRAPMENT-ALS",
      "TRAIN EMERGENCY COMBINED",       "BOX ALARM - TRAIN DERAILMENT OR FIRE WITH POLICE-ALS",
      "TRAIN EMERGENCY",                "BOX ALARM - TRAIN DERAILMENT OR FIRE-ALS",
      "TRAIN PED/STRUCK",               "RESCUE LOCAL - PEDESTRIAN STRUCK BY A TRAIN WITH POLICE-ALS",
      "TRAIN",                          "BOX ALARM - TRAIN DERAILMENT OR FIRE-ALS",
      "TRAINC",                         "BOX ALARM - TRAIN DERAILMENT OR FIRE WITH POLICE-ALS",
      "TRAINS",                         "RESCUE LOCAL - PEDESTRIAN STRUCK BY A TRAIN WITH POLICE-ALS",
      "TRANS",                          "AMBULANCE LOCAL - ROUTINE TRANSPORT-BLS",
      "TRANSFER",                       "TRANSFER",
      "TRT",                            "TECHNICAL RESCUE TASK FORCE",
      "TST CALL DONT DISP",             "TEST DO NOT DISPATCH",
      "TXFR",                           "TRANSFER",
      "UAD",                            "UNAVAILBLE UNITS",
      "UAS",                            "UNAVAILBLE UNITS",
      "UAT",                            "UNAVAILBLE UNITS",
      "UAV",                            "UNAVAILBLE UNITS",
      "UNA",                            "UNAVAILBLE",
      "UNAVAILABLE FD UNITS",           "UNAVAILABLE FD UNITS",
      "UNAVAILABLE",                    "UNAVAILABLE",
      "UNKNOWN C2C CALLTYPE",           "UNKNOWN C2C CALLTYPE",
      "VEHICLE ACCIDENT COMBINED",      "RESCUE LOCAL - PERSONAL INJURY ACCIDENT-BLS",
      "W S TASK FORCE N HYD",           "WATER SUPPLY TASK FORCE NON-HYDRANT AREA",
      "W S TASK FORCE",                 "WATER SUPPLY TASK FORCE",
      "WASH DOWN",                      "LOCAL ALARM-WASHDOWN",
      "WASHD",                          "LOCAL ALARM - WASHDOWN",
      "WATER RESCUE INVEST",            "LOCAL ALARM - BOAT INVESTIGATION",
      "WATER RESCUE",                   "RESCUE LOCAL - WATER RESCUE",
      "WATER",                          "RESCUE LOCAL - WATER RESCUE",
      "WATER0",                         "LOCAL ALARM - WATER RESCUE INVESTIGATION",
      "WATER1",                         "LOCAL ALARM - WATER RESCUE VEHICLE IN WATER-NO INJURIES",
      "WATER2",                         "LOCAL ALARM - WATER RESCUE ANIMAL IN WATER",
      "WATER3",                         "MEDIC LOCAL - WATER RESCUE POOL DROWNING-ALS",
      "WATER4",                         "RESCUE LOCAL - WATER RESCUE OPEN WATER DROWNING-ALS",
      "WATER5",                         "RESCUE LOCAL - WATER RESCUE ICE OR SWIFT WATER-ALS",
      "WATER6",                         "RESCUE LOCAL - WATER RESCUE BOAT IN DISTRESS-ALS",
      "WATER7",                         "LOCAL ALARM - BOAT FIRE",
      "WFD",                            "WORKING FIRE DISPATCH",
      "WI",                             "WORKING INCIDENT",
      "WIREC",                          "LOCAL ALARM - ELECTRICAL HAZARD OR WIRES DOWN WITH POLICE",
      "WIREDN",                         "LOCAL ALARM - ELECTRICAL HAZARD OR WIRES DOWN",
      "WIRES DOWN COMBINED",            "LOCAL ALARM - ELECTRICAL HAZARD OR WIRES DOWN WITH POLICE",
      "WIRES DOWN",                     "LOCAL ALARM - ELECTRICAL HAZARD OR WIRES DOWN",
      "WORKING CODE",                   "MEDIC LOCAL - CARDIAC ARREST-ALS2",
      "WORKING FIRE DISP",              "WORKING FIRE DISPATCH",
      "WSTKN",                          "WATER SUPPLY TASK FORCE-NON HYDRANT",
      "WSTSK",                          "WATER SUPPLY TASK FORCE",
      "WTASKH",                         "WATER SUPPLY TASK FORCE",
      "WTASKN",                         "WATER SUPPLY TASK FORCE NON-HYDRANT AREA",


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
