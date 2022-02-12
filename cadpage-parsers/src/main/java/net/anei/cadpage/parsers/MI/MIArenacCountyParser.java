package net.anei.cadpage.parsers.MI;

import java.util.Properties;

import net.anei.cadpage.parsers.MsgInfo.Data;
import net.anei.cadpage.parsers.SplitMsgOptions;
import net.anei.cadpage.parsers.SplitMsgOptionsCustom;
import net.anei.cadpage.parsers.dispatch.DispatchEmergitechParser;

public class MIArenacCountyParser extends DispatchEmergitechParser {

  public MIArenacCountyParser() {
    super("911:", CITY_LIST, "ARENAC COUNTY", "MI");
  }

  @Override
  public String getFilter() {
    return "911@arenaccountygov.com";
  }

  @Override
  public SplitMsgOptions getActive911SplitMsgOptions() {
    return new SplitMsgOptionsCustom();
  }

  @Override
  protected boolean parseMsg(String subject, String body, Data data) {
    if (!super.parseMsg(subject, body, data)) return false;
    data.strCode = data.strCall;
    data.strCall = convertCodes(data.strCode, CALL_CODES);
    return true;
  }

  @Override
  public String getProgram() {
    return super.getProgram().replace("CALL", "CODE CALL");
  }

  private static final Properties CALL_CODES = buildCodeTable(new String[] {
      "030",  "IMMIGRATION",
      "091",  "HOMICIDE",
      "101",  "KIDNAPPING - ABDUCTION",
      "110",  "SEX CRIMES",
      "120",  "ROBBERY",
      "131",  "ASSAULT",
      "133",  "INTIMIDATION/STALKING/PPO/BOND",
      "200",  "ARSON",
      "210",  "EXTORTION",
      "221",  "BREAKING & ENTERING",
      "230",  "LARCENY - ALL",
      "240",  "MOTOR VEHICLE THEFT",
      "250",  "FORGERY & COUNTERFEITING",
      "266",  "FRAUD",
      "270",  "EMBEZZLEMENT",
      "280",  "STOLEN PROPERTY",
      "290",  "DESTRUCTION OF PROPERTY",
      "351",  "DRUG VIOLATIONS",
      "381",  "FAMILY ABUSE & NEGLECT",
      "391",  "GAMBLING",
      "411",  "LIQUOR LAW VIOLATIONS",
      "420",  "DRUNK & DISORDERLY",
      "480",  "OBSTRUCT POLICE",
      "490",  "ESCAPE / FLIGHT",
      "510",  "BRIBERY",
      "522",  "EXPLOSIVES",
      "523",  "WEAPONS VIOLATIONS",
      "532",  "PUBLIC PEACE",
      "542",  "DRUNK DRIVING",
      "543",  "TRAFFIC OFFENSES",
      "550",  "HEALTH, ANIMAL, TOBACCO",
      "571",  "TRESPASSING, CRIMINAL",
      "580",  "SMUGGLING",
      "620",  "CONSERVATION",
      "924",  "CIVIL CUSTODIES",
      "931",  "PIA/PDA - ACCIDENTS",
      "940",  "BANK ALARMS",
      "942",  "ALARMS, OTHER",
      "951",  "FIRE DEPT REQUESTS",
      "971",  "ACCIDENT - AIRCRAFT",
      "972",  "ACCIDENT - HUNTING",
      "973",  "ACCIDENT - SHOOTING",
      "974",  "ACCIDENT - BOATING",
      "975",  "ACCIDENT - OTHER WATER",
      "976",  "ACCIDENT - ALL OTHER",
      "981",  "BOAT COMPLAINTS & INSPECTIONS",
      "986",  "CIVIL COMPLAINTS",
      "987",  "SUSPICIOUS SITUATIONS",
      "988",  "LOST/FOUND PROPERTY",
      "991",  "SUICIDE & ATTEMPTS",
      "992",  "NATURAL DEATH",
      "993",  "MISSING PERSON",
      "994",  "NATURAL DISASTER",
      "998",  "GENERAL ASSIST/INMATE TRANSFER",
      "ABAN", "ABANDONED VEHICLES",
      "AMB",  "AMBULANCE REQUEST",
      "DUP",  "DUPLICATE CALL",
      "TEST", "TEST RECORD - NO CALL",
      "TRS",  "TRAFFIC STOP"
  });

  private static final String[] CITY_LIST = new String[]{

    // Cities
    "AU GRES",
    "AU GRES CITY",
    "AUGRES",
    "AUGRES CITY",
    "OMER",
    "OMER CITY",
    "STANDISH",
    "STANDISH CITY",

    // Villages
    "STERLING",
    "TURNER",
    "TWINING",

    // Unincorporated communities
    "ALGER",
    "DELANO",
    "MAPLE RIDGE",
    "MELITA",
    "PINE RIVER",

    // Townships
    "ADAMS TWP",
    "ARENAC TWP",
    "AU GRES TWP",
    "AUGRES TWP",
    "CLAYTON TWP",
    "DEEP RIVER TWP",
    "LINCOLN TWP",
    "MASON TWP",
    "MOFFATT TWP",
    "SIMS TWP",
    "STANDISH TWP",
    "TURNER TWP",
    "WHITNEY TWP"
  };
}
