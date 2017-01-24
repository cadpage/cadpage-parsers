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
    return "PTS@ptssolutions.com";
  }
  
  @Override
  protected boolean parseMsg(String subject, String body, Data data) {
    subject = stripFieldStart(subject, "911 -");
    return super.parseMsg(subject, body, data);
  }

  private static Properties CALL_CODES = buildCodeTable(new String[]{
      "AA",     "AUTOMATIC ALARM",
      "AAD",    "AUTOMATIC AID",
      "AC",     "ANIMAL COMPLAINT",
      "AE",     "AIRCRAFT EMERGENCY",
      "BA",     "BOATING ACCIDENT",
      "BL",     "BLEEDING",
      "BP",     "BREATHING  PROBLEMS",
      "BS",     "BREATHING STOPPED",
      "BT",     "BOMB THREAT",
      "CB",     "CONTROL BURN",
      "CC",     "CANCEL CALL",
      "CE",     "CHEMICAL EMERGENCY",
      "CP",     "CHEST PAINS",
      "CR",     "VEHICLE CRASH/NO INJ",
      "CR/I",   "VEHICLE CRASH W/INJU",
      "DEAD",   "DEAD",
      "DI",     "DIABETIC",
      "DIS",    "DISTURBANCE",
      "DISTW",  "DISTURBANCE W/WEAPON",
      "DR",     "DROWNING",
      "EMS",    "EMS",
      "FALL",   "FALL",
      "FALL/I", "FALL WITH INJURY",
      "FELL",   "FELL",
      "GF",     "GRASS FIRE",
      "GL",     "GAS LEAK",
      "GW",     "GAS METER/WASH DOWN",
      "HP",     "HEART PROBLEMS",
      "LA",     "LIFT ASSISTANCE",
      "LP",     "LABOR PAINS",
      "MA",     "MUTUAL AID",
      "NE",     "NON EMERGENCY",
      "OD",     "OVER DOSE",
      "OM",     "OTHER, MISC",
      "PL",     "POWERUNE",
      "PO",     "PASSED OUT",
      "SA",     "SUICIDE ATIEMPT",
      "SE",     "SEIZURE",
      "SF",     "STRUCTURE FIRE",
      "SH",     "SHOOTING",
      "SI",     "SICK",
      "STA",    "STABBING",
      "STR",    "STROKE",
      "SUT",    "SUICIDE THREAT",
      "TA",     "TRAIN ACCIDENT",
      "TD",     "TRASH, DUMPSTER",
      "TRDN",   "TREE DOWN",
      "UP",     "UNRESPONSIVE PERSON",
      "VF",     "VEHICLE FIRE",
      "WR",     "WRECK"
  });
}
