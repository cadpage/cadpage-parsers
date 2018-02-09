package net.anei.cadpage.parsers.LA;

import java.util.Properties;

import net.anei.cadpage.parsers.MsgInfo.Data;
import net.anei.cadpage.parsers.dispatch.DispatchA46Parser;

public class LATangipahoaParishAParser extends DispatchA46Parser {

  public LATangipahoaParishAParser() {
    super(CALL_CODES, "TANGIPAHOA PARISH", "LA");
  }

  @Override
  public String getFilter() {
    return "PTS@ptssolutions.com,tangipahoaparish911@pagingpts.com";
  }
  
  @Override
  protected boolean parseMsg(String subject, String body, Data data) {
    if (!super.parseMsg(subject, body, data)) return false;
    if (data.strSource.equals("911")) data.strSource = "";
    return true;
  }

  private static Properties CALL_CODES = buildCodeTable(new String[]{
      "AA",     "AUTOMATIC ALARM",
      "AAB",    "AUTO ALARM BURGLARY",
      "AAD",    "AUTOMATIC AID",
      "AAFCOMM","AUTO FIRE ALARM - COMMERCIAL",
      "AAFRESD","Automatic Fire Alarm Residential",
      "AAM",    "AUTO ALARM MEDICAL",
      "AC",     "ANIMAL COMPLAINT",
      "AE",     "AIRCRAFT EMERGENCY",
      "BA",     "BOATING ACCIDENT",
      "BI",     "BREAK IN",
      "BL",     "BLEEDING",
      "BP",     "BREATHING PROBLEMS",
      "BS",     "BREATHING STOPPED",
      "BT",     "BOMB THREAT",
      "CB",     "CONTROL BURN",
      "CC",     "CANCEL CALL",
      "CE",     "CHEMICAL EMERGENCY",
      "CO",     "COMPLAINT",
      "CP",     "CHEST PAINS",
      "CR",     "VEHICLE CRASH NO INJURIES",
      "CR/I",   "VEHICLE CRASH WITH INJURIES",
      "DEAD",   "DEAD",
      "DI",     "DIABETIC EMERGENCY",
      "DIS",    "DISTURBANCE",
      "DIS/W",  "DISTURBANCE WITH WEAPON",
      "DISTW",  "DISTURBANCE WITH WEAPON",
      "DR",     "DROWNING",
      "EMS",    "EMS",
      "ESC",    "ESCAPEE",
      "ETA",    "ETA",
      "FALL",   "FALL NO INJURY",
      "FALL/I", "FALL WITH INJURY",
      "FELL",   "FELL",
      "GF",     "GRASS FIRE",
      "GL",     "GAS LEAK",
      "GS",     "GUNSHOTS HEARD",
      "GW",     "GASOLINE WASH DOWN",
      "HP",     "HEART PROBLEM",
      "HUC",    "HANG UP CELL PHONE",
      "HUL",    "HANG UP LAND LINE",
      "HW",     "HIGH WATER",
      "INFO",   "INFORMATION",
      "INT",    "INTRUDER",
      "LA",     "LIFT ASSIST",
      "LP",     "LABOR PAINS",
      "MA",     "MUTUAL AID",
      "MP",     "MISSING PERSON",
      "NE",     "NON EMERGENCY",
      "OD",     "OVERDOSE",
      "OL",     "OPEN LINE",
      "OM",     "OTHER MISCELLANEOUS",
      "PL",     "POWER LINE",
      "PO",     "PASSED OUT",
      "RAPE",   "RAPE",
      "RD",     "RECKLESS DRIVER",
      "RH",     "ROAD HAZARD",
      "RO",     "RAILROAD OBSTRUCTION",
      "ROB",    "ROBBERY",
      "RP",     "REQUEST POLICE",
      "SA",     "SUICIDE ATTEMPT",
      "SE",     "SEIZURE",
      "SF",     "STRUCTURE FIRE",
      "SFCOMM", "STRUCTURE FIRE COMMERCIAL",
      "SFRESD", "STRUCTURE FIRE RESIDENTIAL",
      "SH",     "SHOOTING",
      "SHOP",   "SHOPLIFTING",
      "SI",     "SICK",
      "SM",     "STRANDED MOTORIST",
      "SP",     "SUSPICIOUS PERSON",
      "SPL",    "SUSPICIOUS PACKAGE/LETTER",
      "ST",     "STOLEN",
      "STA",    "STABBING",
      "STR",    "STROKE",
      "SUT",    "SUICIDE THREAT",
      "SV",     "SUSPICIOUS VEHICLE",
      "TA",     "TRAIN ACCIDENT",
      "TD",     "TRASH,DUMPSTER",
      "TRDN",   "TREE DOWN",
      "TEST",   "TEST",
      "TF",     "TRASH FIRE",
      "TORNADO","TORNADO",
      "UP",     "UNRESPONSIVE PERSON",
      "VF",     "VEHICLE FIRE",
      "VS",     "VEHICLE SINKING",
      "WC",     "WELFARE CONCERN",
      "WN",     "WRONG NUMBER",
      "WR",     "WATER RESCUE"
  });
}
