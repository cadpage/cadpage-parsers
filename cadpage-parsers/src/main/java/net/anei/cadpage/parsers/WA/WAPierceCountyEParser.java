package net.anei.cadpage.parsers.WA;

import java.text.SimpleDateFormat;
import java.util.Properties;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.anei.cadpage.parsers.FieldProgramParser;
import net.anei.cadpage.parsers.MsgInfo.Data;
import net.anei.cadpage.parsers.MsgInfo.MsgType;

/**
 * Pierce County, WA
 */
public class WAPierceCountyEParser extends FieldProgramParser {

  public WAPierceCountyEParser() {
    super(CITY_CODES, "PIERCE COUNTY", "WA",
          "( T:CALL! ST:CALL/D! P:PRI! L:PRI/D! Location:ADDR/S? DG:CH! Map:MAP Units:SKIP! Time:TIME! E#:ID? Lat:GPS1? Long:GPS2? Disp:UNIT! " +
          "| Type:CALL! SubType:CALL/D! Priority:PRI! Alarm_Level:PRI/D! Location:ADDR/S! DGroup:CH! Map_Page:MAP Units:SKIP! Time:TIME! EventNum:ID? Lat:GPS1? Long:GPS2? Disp:UNIT! ) END");
  }

  @Override
  public String getFilter() {
    return "Dispatcher@SouthSound911.org";
  }

  @Override
  public int getMapFlags() {
    return MAP_FLG_PREFER_GPS;
  }

  private static final Pattern GEN_ALERT_PTN = Pattern.compile("(?:(.*?) )?Original message from terminal \\S+  -  (\\d\\d?/\\d\\d?/\\d{4}) (\\d\\d?:\\d\\d:\\d\\d(?: [AP]M)?)  .* \\(\\d+\\):(?: +(.*))?", Pattern.DOTALL);
  private static final SimpleDateFormat TIME_FMT = new SimpleDateFormat("hh:mm:ss aa");

  @Override
  protected boolean parseMsg(String subject, String body, Data data) {
    if (!subject.equals("SouthSound911 Page Notification")) return false;

    Matcher match = GEN_ALERT_PTN.matcher(body);
    if (match.matches()) {
      setFieldList("DATE TIME INFO");
      data.msgType = MsgType.GEN_ALERT;
      data.strSupp = getOptGroup(match.group(1));
      data.strDate = match.group(2);
      String time = match.group(3);
      if (time.endsWith("M")) {
        setTime(TIME_FMT, time, data);
      } else {
        data.strTime = time;
      }
      data.strSupp = append(data.strSupp, "\n", getOptGroup(match.group(4)));
      return true;
    }

    body = body.replace("FireComm", "Firecomm");
    body = body.replaceAll("Lat::", "Lat:");
    if (!super.parseMsg(body, data)) return false;
    String call = CALL_CODES.getProperty(data.strCall);
    if (call != null) {
      data.strCode = data.strCall;
      data.strCall = call;
    }
    return true;
  }

  @Override
  public String getProgram() {
    return super.getProgram().replace("CALL", "CODE CALL");
  }

  @Override
  public Field getField(String name) {
    if (name.equals("ADDR")) return new MyAddressField();
    if (name.equals("TIME")) return new TimeField("\\d\\d:\\d\\d:\\d\\d", true);
    return super.getField(name);
  }

  private class MyAddressField extends AddressField {
    @Override
    public void parse(String field, Data data) {
      Parser p = new Parser(field);
      data.strPlace = p.getLastOptional("@");
      String alias = null;
      String apt = p.getLastOptional(':');
      if (apt.startsWith("alias ")) {
        alias = apt.substring(6).trim();
        apt = "";
      }
      apt = append(p.getLastOptional(','), "-", apt);
      super.parse(p.get(), data);
      data.strApt = append(data.strApt, "-", apt);
      if (alias != null) {
        data.strAddress = append(data.strAddress, " ", '('+alias+')');
      }
    }

    @Override
    public String getFieldNames() {
      return "ADDR CITY APT PLACE";
    }
  }

  @Override
  public String adjustMapAddress(String sAddress) {
    return WAPierceCountyParser.adjustMapAddressCommon(sAddress);
  }

  private static final Properties CALL_CODES = buildCodeTable(new String[]{
      "AFA-CO",                     "CARBON MONOXIDE ALARM",
      "AFA-COM",                    "COMMERCIAL FIRE ALARM",
      "AFA-COMBOX",                 "COMMERCIAL BOX ALARM",
      "AFA-COMSMK",                 "COMMERCIAL SMOKE ALARM",
      "AFA-COMWTR",                 "COMMERCIAL WATERFLOW ALARM",
      "AFA-ELEV",                   "ELEVATOR ALARM",
      "AFA-HL",                     "HIGH LIFE HAZARD FIRE ALARM",
      "AFA-HLBOX",                  "HIGH LIFE HAZARD BOX ALARM",
      "AFA-HLSMK",                  "HIGH LIFE HAZARD SMOKE ALARM",
      "AFA-HLWTR",                  "HIGH LIFE HAZARD WATERFLOW ALARM",
      "AFA-HR",                     "HIGH RISE FIRE ALARM",
      "AFA-HRBOX",                  "HIGH RISE BOX ALARM",
      "AFA-HRSMK",                  "HIGH RISE SMOKE ALARM",
      "AFA-HRWTR",                  "HIGH RISE WATERFLOW ALARM",
      "AFA-RES",                    "RESIDENTIAL FIRE ALARM",
      "AIRLIFT-default",            "FLIGHT MEDICAL ASSIST",
      "ALARM-default",              "BOX ALARM",
      "ALARM-INTRUS",               "INTRUSION",
      "ALARM-SUPER",                "SUPERVISORY",
      "ALARM-TAMPER",               "TAMPER",
      "ALERT1 - default",           "POSSIBLE EMERGENCY LANDING",
      "ALERT2 - default",           "EMERGENCY LANDING",
      "ALERT3 - default",           "PLANE CRASH",
      "ALLER-1",                    "ALS - ALLERGIC REACTION",
      "ALLER-2",                    "BLS - ALLERGIC REACTION",
      "ALLER-3",                    "BLS - ALLERGIC REACTION",
      "AP-1",                       "ALS - ABDOMINAL PAIN",
      "AP-2",                       "BLS - ABDOMINAL PAIN",
      "AP-3",                       "BLS - ABDOMINAL PAIN",
      "BLEED-1",                    "ALS - HEMORRAGE",
      "BLEED-2",                    "BLS - BLEEDING PROBLEM",
      "BLEED-3",                    "BLS - BLEEDING PROBLEM",
      "BP-1",                       "ALS - BACK PAIN",
      "BP-2",                       "BLS - BACK PAIN",
      "BP-3",                       "BLS - BACK PAIN",
      "BURN-1",                     "ALS - BURN PATIENT",
      "BURN-1MULT",                 "ALS - MULTIPLE BURN PATIENTS",
      "BURN-2",                     "BLS - BURN PATIENT",
      "BURN-2MULT",                 "BLS - MULTIPLE BURN PATIENTS",
      "BURN-3",                     "BLS - MINOR BURN PATIENT",
      "BURN-3MULT",                 "BLS - MULTIPLE MINOR BURN PATIENTS",
      "CHAP-default",               "CHAPLAIN",
      "CHEST 1",                    "ALS HEART PROBLEM",
      "CHEST - 1",                  "ALS - HEART PROBLEM",
      "CHEST 2",                    "BLS POSSIBLE HEART PROBLEM",
      "CHEST-default",              "ALS - HEART PROBLEM",
      "CODEBLUE-default",           "CODE BLUE",
      "CPR-default",                "CARDIAC ARREST",
      "CVA-1",                      "ALS - CVA",
      "CVA-3",                      "BLS MINOR - CVA",
      "CVA-default",                "CVA",
      "DEM-PC",                     "PIERCE COUNTY",
      "DEM-TFD",                    "TACOMA FIRE",
      "DIAB-1",                     "ALS - DIABETIC PROBLEM",
      "DIAB-2",                     "BLS - DIABETIC PROBLEM",
      "DOCK-default",               "DOCK FIRE",
      "ELEC-INSIDE",                "INSIDE ELECTRICAL PROBLEM",
      "ELEC-OUTSIDE",               "OUTSIDE ELECTRICAL PROBLEM",
      "ELEC-POLE",                  "ELECTRICIAL PROBLEM - POLE",
      "ELEC-VAULT",                 "ELECTRICAL PROBLEM - VAULT",
      "ELEC-WIRES",                 "WIRES DOWN",
      "EMER-default",               "UNIT EMERGENCY",
      "ENVIR-1",                    "ALS - EXPOSURE PATIENT",
      "ENVIR-1MULT",                "ALS - MULTIPLE EXPOSURE PATIENTS",
      "ENVIR-2",                    "BLS - EXPOSURE PATIENT",
      "ENVIR-2MULT",                "BLS - MULTIPLE EXPOSURE PATIENTS",
      "ENVIR-3",                    "BLS - EXPOSURE PATIENT",
      "ENVIR-3MULT",                "BLS - MULTIPLE EXPOSURE PATIENTS",
      "EVENT-BOAT",                 "FIRE BOAT",
      "EVENT-DEPT",                 "DEPARTMENT",
      "EVENT-MED",                  "MEDICAL",
      "EVENT-TRAINING",             "TRAINING",
      "EVENT-WATCH",                "FIRE WATCH",
      "EXPLO-default",              "EXPLOSION - NO FIRE",
      "FALL-1",                     "ALS - FALL PATIENT",
      "FALL-2",                     "BLS - FALL PATIENT",
      "FALL-3",                     "BLS - FALL PATIENT",
      "FAPPL-default",              "APPLIANCE FIRE",
      "FBARK",                      "SMALL BARK FIRE",
      "FBARK-default",              "SMALL BARK FIRE",
      "FBOAT-1LAKE",                "BOAT FIRE - LAKE",
      "FBOAT-1SOUND",               "SHIP FIRE - PUGET SOUND",
      "FBOAT-1TRLR",                "SHIP FIRE - ON TRAILER",
      "FBOAT-1TRLRX",               "SHIP FIRE - ON TRAILER W/ EXPOSURE",
      "FBOAT-2LAKE",                "BOAT FIRE - LAKE",
      "FBOAT-2SOUND",               "BOAT FIRE - PUGET SOUND",
      "FBOAT-2TRLR",                "BOAT FIRE - ON TRAILER",
      "FBOAT-2TRLRX",               "BOAT FIRE - ON TRAILER W/ EXPOSURE",
      "FBOAT-3LAKE",                "BOAT FIRE NOW OUT - LAKE",
      "FBOAT-3SOUND",               "BOAT FIRE NOW OUT - PUGET SOUND",
      "FBOAT-3TRLR",                "BOAT FIRE NOW OUT - ON TRAILER",
      "FBRUSH-1",                   "LARGE BRUSH FIRE",
      "FBRUSH-1X",                  "LARGE BRUSH FIRE W/ EXPOSURE",
      "FBRUSH-2",                   "BRUSH FIRE",
      "FBRUSH-2X",                  "BRUSH FIRE W/ EXPOSURE",
      "FBRUSH-3",                   "SMALL BRUSH FIRE",
      "FBRUSH-3X",                  "SMALL BRUSH FIRE W/ EXPOSURE",
      "FCHIM-default",              "CHIMNEY FIRE",
      "FCINFO-ADDRESS_VERIFY",      "ADDRESS VERIFICATION",
      "FCINFO-ALARM_PROB",          "ALARM SYSTEM PROBLEM",
      "FCINFO-CONTROL_BURN",        "CONTROLLED BURN",
      "FCINFO-default",             "INFO EVENT - FIRECOMM",
      "FCINFO-DRILL",               "FIRE DRILL NOTIFICATION",
      "FCINFO-EMER",                "RADIO EMER ACTIVATION",
      "FCINFO-PD_HANDLE",           "POLICE TO HANDLE",
      "FCINFO-ROAD_CLOSED",         "ROAD CLOSURE NOTIFICATION",
      "FCINFO-SMK_TEST",            "SMOKE TESTING",
      "FCOM-APT",                   "APARTMENT FIRE",
      "FCOM-APTT",                  "APARTMENT FIRE W/ENTRAPMENT",
      "FCOM-default",               "COMMERCIAL FIRE",
      "FCOM-HL",                    "HIGH LIFE HAZARD STRUCTURE FIRE",
      "FCOM-HLT",                   "HIGH LIFE HAZ STRUCT FIRE W/ENTRAP",
      "FCOM-HR",                    "HIGH RISE FIRE",
      "FCOM-HRT",                   "HIGH RISE FIRE W/ENTRAPMENT",
      "FCOM-MARINA",                "MARINA FIRE",
      "FCOM-ODOR",                  "SMOKE ODOR IN COMMERCIAL STRUCTURE",
      "FCOM-SMOKE",                 "SMK IN A COM STRUCT",
      "FCOM-T",                     "COMMERCIAL STRUCT FIRE W/ENTRAPMENT",
      "FCTEST-1",                   "TEST SUBTYPE",
      "FCTEST-default",             "FIRECOMM TEST EVENT",
      "FDOCK-default",              "DOCK FIRE",
      "FIRE BARK",                  "SMALL BARK FIRE",
      "FIRE POLE",                  "UTILITY POLE FIRE",
      "FIRE-DEBRIS",                "DEBRIS FIRE",
      "FIRE-default",               "FIRE",
      "FIRE-DUMPSTER",              "DUMPSTER FIRE",
      "FIRE-OUTHOUSE",              "OUTHOUSE FIRE",
      "FIRE-UNK",                   "UNKNOWN TYPE",
      "FIRE-X",                     "W/EXPOSURE",
      "FMARINA-default",            "MARINA FIRE",
      "FOUT-default",               "FIRE NOW OUT",
      "FPOLE",                      "UTILITY POLE FIRE",
      "FRES-default",               "RESIDENTIAL FIRE",
      "FRES-GAR",                   "GARAGE FIRE",
      "FRES-ODOR",                  "SMOKE ODOR IN RESIDENCE",
      "FRES-SMOKE",                 "SMK IN A RESD STRUCT",
      "FRES-T",                     "RESIDENTIAL FIRE W/ENTRAPMENT",
      "FUEL-1",                     "FUEL SPILL - LARGE",
      "FUEL-2",                     "FUEL SPILL",
      "FUEL-3",                     "FUEL SPILL - SMALL",
      "FUNK-default",               "UNKNOWN TYPE FIRE",
      "FVEH-1",                     "LARGE VEHICLE FIRE",
      "FVEH-1T",                    "LARGE VEHICLE FIRE W/ENTRAPMENT",
      "FVEH-1TANKER",               "TANKER TRUCK FIRE",
      "FVEH-1X",                    "LARGE VEHICLE FIRE W/EXPOSURE",
      "FVEH-2",                     "VEHICLE FIRE",
      "FVEH-2T",                    "VEHICLE FIRE W/ENTRAPMENT",
      "FVEH-2X",                    "VEHICLE FIRE W/EXPOSURE",
      "FVEH-3",                     "POSSIBLE VEHICLE FIRE",
      "FVEH-3T",                    "POSSIBLE VEHICLE FIRE W/ENTRAPMENT",
      "FVEH-3X",                    "POSSIBLE VEHICLE FIRE W/EXPOSURE",
      "GAS-1",                      "VAPOR LEAK - LARGE",
      "GAS-2",                      "VAPOR LEAK",
      "GAS-3",                      "POSSIBLE VAPOR LEAK",
      "GAS-ODOR",                   "ODOR OF NATURAL GAS",
      "HAZ-1",                      "MAJOR HAZMAT INCIDENT",
      "HAZ-1EMS",                   "MAJOR HAZMAT INCIDENT - EMS",
      "HAZ-1FIRE",                  "MAJOR HAZMAT INCIDENT - FIRE",
      "HAZ-2",                      "STANDARD HAZMAT INCIDENT",
      "HAZ-2EMS",                   "STANDARD HAZMAT INCIDENT - EMS",
      "HAZ-2FIRE",                  "STANDARD HAZMAT INCIDENT - FIRE",
      "HAZ-3",                      "POSSIBLE HAZMAT INCIDENT",
      "HAZ-HAZCON",                 "HAZMAT INVESTIGATION",
      "HEAD-1",                     "ALS - ALTERED LOC",
      "HEAD-3",                     "BLS - HEAD PAIN",
      "HIRL-default",               "HIGH INCIDENT RESPONSE LOAD",
      "INJ-1",                      "ALS - TRAUMA PATIENT",
      "INJ-1MULT",                  "ALS - MULTIPLE TRAUMA PATIENTS",
      "INJ-1T",                     "ALS - TRAUMA PATIENT W/ENTRAPTMENT",
      "INJ-2",                      "BLS - TRAUMA PATIENT",
      "INJ-2MULT",                  "BLS - MULTIPLE TRAUMA PATIENTS",
      "INJ-2T",                     "BLS - TRAUMA PATIENT W/ENTRAPMENT",
      "INJ-3",                      "BLS - MINOR TRAUMA PATIENT",
      "INJ-3MULT",                  "BLS - MULTIPLE MINOR TRAUMA PATIENTS",
      "INJ-3T",                     "BLS - TRAUMA PATIENT W/ENTRAPMENT",
      "INVEST-BURN",                "BURN COMPLAINT",
      "INVEST-FIRE",                "FIRE INVESTIGATION",
      "INVEST-ODOR",                "ODOR INVESTIGATION",
      "INVEST-SMOKE",               "SMOKE INVESTIGATION",
      "LAW-default",                "INCIDENT TRANSFERRED TO LAW",
      "MA-default",                 "MEDICAL ALARM",
      "MAYDAY-default",             "MAYDAY",
      "MCI-default",                "MULTIPLE CASUALTY INCIDENT",
      "MHTX-default",               "MENTAL HEALTH TRANSPORT",
      "MHTX-INVOL",                 "INVOLUNTARY COMMIT",
      "MHTX-VOL",                   "VOLUNTARY COMMIT",
      "MOVEUP-default",             "MOVEUP",
      "MUTUAL-1",                   "MUTUAL AID RESPONSE",
      "MUTUAL-2",                   "MUTUAL AID RESPONSE",
      "MUTUAL-MOVEUP",              "MOVEUP",
      "MUTUAL-MUT1",                "MUTUAL AID RESPONSE",
      "MUTUAL-MUT2",                "MUTUAL AID RESPONSE",
      "MUTUAL-MUTAGN",              "AGENCY ASSIST",
      "MVA-1",                      "ALS - MOTOR VEHICLE ACCIDENT",
      "MVA-1MULT",                  "ALS - MULT PT MOTOR VEHICLE ACCIDENT",
      "MVA-1T",                     "ALS - MOTOR VEHICLE ACCIDENT W/ENTRAP",
      "MVA-2",                      "BLS - MOTOR VEHICLE ACCIDENT",
      "MVA-2MULT",                  "BLS - MULT PT MOTOR VEHICLE ACCIDENT",
      "MVA-2T",                     "BLS - MOTOR VEHICLE ACCIDENT W/ ENTRAP",
      "MVA-3",                      "BLS - MOTOR VEHICLE ACCIDENT",
      "MVA-3MULT",                  "BLS - MULT PT MOTOR VEHICLE ACCIDENT",
      "MVA-3T",                     "BLS - MOTOR VEHICLE ACCIDENT W/ENTRAP",
      "OB-1",                       "ALS - OB EMERGENCY",
      "OB-2",                       "BLS - OB PROBLEM",
      "OB-3",                       "BLS - OB PROBLEM",
      "OD-1",                       "ALS - OVERDOSE",
      "OD-1VIOLENCE",               "ALS - OVERDOSE WITH VIOLENCE",
      "OD-2",                       "BLS - OVERDOSE",
      "OD-2VIOLENCE",               "BLS - OVERDOSE WITH VIOLENCE",
      "OD-3",                       "BLS - OVERDOSE",
      "OD-3VIOLENCE",               "BLS - OVERDOSE WITH VIOLENCE",
      "PCHIT-default",              "PIERCE COUNTY HAZ INCIDENT TEAM",
      "PCSORT-CONF_SPACE",          "CONFINED SPACE RESCUE",
      "PCSORT-ENTRAPMENT",          "ENTRAPMENT",
      "PCSORT-ROPE",                "HIGH/LOW ANGLE RESCUE",
      "PCSORT-STRUC_COLLAPSE",      "STRUCTURE COLLAPSE RESCUE",
      "PCSORT-TRENCH",              "TRENCH RESCUE",
      "PLANE-1",                    "LARGE PLANE CRASH",
      "PLANE-2",                    "PLANE CRASH",
      "PLANE-3",                    "POSSIBLE AIRPLANE PROBLEM",
      "PLANE-ALERT1",               "POSSIBLE EMERGENCY LANDING",
      "PLANE-ALERT2",               "EMERGENCY LANDING",
      "PLANE-ALERT3",               "CONFIRMED PLANE CRASH",
      "PLANE-MIL",                  "MILITARY PLANE CRASH",
      "PSYCH-1",                    "ALS - PSYCH PROBLEM",
      "PSYCH-1VIOLENCE",            "ALS - PSYCH PROBLEM W/VIOLENCE",
      "PSYCH-2",                    "BLS - PSYCH PROBLEM",
      "PSYCH-2VIOLENCE",            "BLS - PSYCH PROBLEM W/VIOLENCE",
      "PSYCH-3",                    "BLS - PSYCH PROBLEM",
      "PSYCH-3VIOLENCE",            "BLS - PSYCH PROBLEM W/VIOLENCE",
      "RESQ-COLLAPSE",              "STRUCTURE COLLAPSE",
      "RESQ-CONF",                  "CONFINED SPACE RESCUE",
      "RESQ-ENTRAPMENT",            "ENTRAPMENT",
      "RESQ-HEAVY",                 "HEAVY RESCUE",
      "RESQ-LOCK",                  "EMERGENCY LOCKOUT",
      "RESQ-ROPE",                  "HIGH/LOW ANGLE RESCUE",
      "RESQ-TRENCH",                "TRENCH RESCUE",
      "SICK-2",                     "BLS - PERSON ILL",
      "SICK-2U",                    "BLS - UNKN MEDICAL AID",
      "SICK-3",                     "BLS - PERSON ILL",
      "SICK-INFECTIOUS",            "PERSON ILL - SPECIAL RESPONSE",
      "SOB-1",                      "ALS - RESPIRATORY DISTRESS",
      "SOB-2",                      "BLS - DIFFICULTY BREATHING",
      "SOB-3",                      "BLS - DIFFICULTY BREATHING",
      "STRIKE-1",                   "SERVICE CALL",
      "STRIKE-ALS",                 "ALS STRIKE TEAM",
      "STRIKE-BLS",                 "BLS STRIKE TEAM",
      "STRIKE-default",             "FIRE STRIKE TEAM",
      "STRIKE-EMS",                 "EMS TASK FORCE",
      "STRIKE-ENGINE",              "ENGINE STRIKE TEAM",
      "STRIKE-INTERFACE",           "INTERFACE TASK FORCE",
      "STRIKE-LADDER",              "LADDER STRIKE TEAM",
      "STRIKE-OVERHEAD",            "OVERHEAD TEAM",
      "STRIKE-RURAL",               "RURAL TASK FORCE",
      "STRIKE-TENDER",              "TENDER STRIKE TEAM",
      "STRIKE-URBAN",               "URBAN TASK FORCE",
      "STRIKE-WILD_ENGINE",         "WILDLAND ENGINE STRIKE TEAM",
      "STRIKE-WILD_HAND",           "WILDLAND HAND TEAM",
      "STRIKE-WILD_TASK",           "WILDLAND TASK FORCE",
      "STRIKE-WILD_TENDER",         "WILDLAND TENDER STRIKE TEAM",
      "SVC-1",                      "ALS - SEIZURE",
      "SVC-2",                      "SERVICE CALL",
      "SVC-3",                      "MINOR SERVICE CALL",
      "SVC-ALARM",                  "ALARM RESET",
      "SVC-AMB",                    "AMBULANCE",
      "SVC-ANIMAL",                 "ANIMAL ASSIST",
      "SVC-BOAT",                   "BOAT SERVICE CALL",
      "SVC-CHAPLAIN",               "CHAPLAIN",
      "SVC-default",                "SERVICE CALL",
      "SVC-ELEV",                   "ELEVATOR RESCUE",
      "SVC-ESCL",                   "ESCALATOR RESCUE",
      "SVC-LAW",                    "ASSIST LAW ENFORCEMENT",
      "SVC-LOCK",                   "VEHICLE LOCKOUT",
      "SVC-PTA",                    "PATIENT ASSIST",
      "SZR 3",                      "BLS POSSIBLE SEIZURE",
      "SZR-2",                      "BLS - SEIZURE",
      "SZR-F1",                     "FREIGHT TRAIN DERAIL/FIRE",
      "TFCINFO-default",            "INFO EVENT - TACOMA FIRE",
      "TRAIN-F2",                   "FREIGHT TRAIN DERAIL/FIRE",
      "TRAIN-F3",                   "MINOR FREIGHT TRAIN INCIDENT",
      "TRAIN-P1",                   "PASSENGER TRAIN DERAIL/FIRE",
      "TRAIN-P2",                   "PASSENGER TRAIN DERAIL/FIRE",
      "TRAIN-P3",                   "MINOR PASSENGER TRAIN INCIDENT",
      "TRAIN-TRAUASSAULT",          "1 - ALS - ASSAULT PATIENT",
      "TRAIN-UNK",                  "UNKNOWN TYPE TRAIN INCIDENT",
      "TRAU-1MULT",                 "ALS - MULTIPLE ASSAULT PATIENTS",
      "TRAU-1VIOLENCE",             "ALS - TRAUMA W/VIOLENCE",
      "TRAU-2",                     "BLS - ASSAULT PATIENT",
      "TRAU-2MULT",                 "BLS - MULTIPLE ASSAULT PATIENTS",
      "TRAU-2VIOLENCE",             "BLS - TRAUMA W/VIOLENCE",
      "TRAU-3",                     "BLS - ASSAULT PATIENT MINOR INJURY",
      "TRAU-3MULT",                 "BLS - MULTIPLE MINOR ASSAULT PATIENTS",
      "TRAU-3VIOLENCE",             "BLS - MINOR TRAUMA W/VIOLENCE",
      "UNCON-1",                    "ALS - UNCONSCIOUS PERSON",
      "UNCON-2",                    "BLS - SYNCOPE",
      "UNCON-3",                    "BLS - NEAR SYNCOPE",
      "UNCON-3S",                   "BLS - POSSIBLE SIGNAL",
      "WSF-DETAIL",                 "DETAIL",
      "WSF-FOUND_PERSON",           "FOUND PERSON",
      "WSF-LOST_PERSON",            "LOST PERSON",
      "WSF-PD",                     "POLICE ISSUE",
      "WTR-default",                "WATER RESCUE",
      "WTR-ICE",                    "ICE RESCUE",
      "WTR-LAKE",                   "WATER RESCUE - LAKE",
      "WTR-LAKE_TAPPS",             "WATER RESCUE - LAKE TAPPS",
      "WTR-POOL",                   "WATER RESCUE - POOL",
      "WTR-PUGET_SOUND",            "WATER RESCUE - PUGET SOUND",
      "WTR-SWIFT",                  "WATER RESCUE - SWIFT"


  });

  private static final Properties CITY_CODES = buildCodeTable(new String[]{
      "AI AI","ANDERSON ISLAND",
      "AI",   "ANDERSON ISLAND",
      "AUB",  "AUBURN",
      "BON",  "BONNEY LAKE",
      "BUC",  "BUCKLEY",
      "CARB", "CARBONADO",
      "DUP",  "DUPONT",
      "EATN", "EATONVILLE",
      "EDG",  "EDGEWOOD",
      "FDW",  "FEDERAL WAY",
      "FI",   "FOX ISLAND",
      "FIF",  "FIFE",
      "FRC",  "FIRCREST",
      "GH",   "GIG HARBOR",
      "JBLM", "JOINT BASE",
      "KCO",  "KING COUNTY",
      "KIT",  "KITSAP COUNTY",
      "LCO",  "LEWIS COUNTY",
      "LKW",  "LAKEWOOD",
      "MI",   "MCNEIL ISLAND",
      "MIL",  "MILTON",
      "ORT",  "ORTING",
      "PAC",  "PACIFIC",
      "PCO",  "PIERCE COUNTY",
      "PUY",  "PUYALLUP",
      "RUS",  "RUSTON",
      "RY",   "ROY",
      "SPR",  "SOUTH PRARIE",
      "STCM", "STEILACOOM",
      "SUM",  "SUMNER",
      "UP",   "UNIVERSITY PLACE",
      "WILK", "WILKESON"

  });
}
