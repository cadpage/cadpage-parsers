package net.anei.cadpage.parsers.CA;

import java.util.Properties;

import net.anei.cadpage.parsers.dispatch.DispatchA20Parser;

public class CAButteCountyCParser extends DispatchA20Parser {

  public CAButteCountyCParser() {
    super(CALL_CODES, "BUTTE COUNTY", "CA");
  }

  @Override
  public String getFilter() {
    return "RIMS2text@chicoca.gov,@active911.com";
  }

  private static final Properties CALL_CODES = buildCodeTable(new String[] {
      "TESTFIRE", "TEST call for FIRE",
      "Z1180",    "TRAFFIC ACCIDENT MAJOR INJURIES",
      "Z1183",    "Traffic Accident Unknown Details",
      "Z20001",   "HIT AND RUN FELONY",
      "ZACTIVE",  "ACTIVE SHOOTER",
      "ZALERT1",  "POSS AIRCRAFT PROBLEM",
      "ZALERT2",  "AIRCRAFT MALFUNCTION",
      "ZALERT3",  "AIRCRAFT CRASH",
      "ZATSUI",   "ATTEMPT SUICIDE",
      "ZBRPIPE",  "BROKEN WATER MAIN",
      "ZCOALARM", "FIRE CARBON MONOXIDE ALARM",
      "ZCONCOM",  "CONFIRMED COMM STRUCTURE ",
      "ZCONSTR",  "CONFIRMED STRUCTURE FIRE",
      "ZDUITC",   "DUI TRAFFIC COLLISON",
      "ZELEVAT",  "ELEVATOR EMERGENCY",
      "ZEXPLOA",  "EXPLOSION OUTSIDE AREA",
      "ZEXPLST",  "EXPLOSION STRUCTURE ",
      "ZEXTFIR",  "EXTINGUISHED FIRE",
      "ZFALARM",  "FIRE SMOKE ALARM",
      "ZFIREINF", "FIRE INFORMATION",
      "ZFOTHER",  "FIRE OTHER CODE 3 CALL",
      "ZFPLANE",  "AIRCRAFT FIRE",
      "ZFPUB",    "FIRE PUBLIC ASSIST",
      "ZGASLIN",  "GAS LINE / MAIN BREAK",
      "ZGASOUT",  "GAS ODOR OUTSIDE ",
      "ZGASSTR",  "GAS ODOR IN STRUCTURE",
      "ZHAZCON",  "HAZARDOUS CONDITION ",
      "ZHAZMA1",  "HAZMAT LEVEL 1",
      "ZHAZMA2",  "HAZMAT LEVEL 2",
      "ZHAZMA3",  "HAZMAT LEVEL 3",
      "ZILLBUR",  "ILLEGAL BURN",
      "ZMABRE",   "DIFFICULTY BREATHING",
      "ZMACHO",   "MEDICAL CHOKING ",
      "ZMACPR",   "MEDICAL CPR IN PROGRESS",
      "ZMACRIM",  "MEDICAL CRIMINAL VIOLENCE",
      "ZMAOD",    "MEDICAL OVERDOSE ",
      "ZMAOTH",   "MEDICAL OTHER",
      "ZMASTRO",  "MEDICAL STROKE",
      "ZMAUNC",   "MEDICAL UNCONSCIOUS",
      "ZMUTIN",   "MUTUAL AID WITHIN CUAFRA",
      "ZMUTOUT",  "MUTUAL AID OUTSIDE CUAFRA",
      "ZPDASST",  "FIRE ASSIST PD",
      "ZPOSSTR",  "POSSIBLE STRUCTURE FIRE",
      "ZRESTCH",  "RESCUE TECHNICAL",
      "ZRESWTR",  "RESCUE WATER",
      "ZRUNWAY",  "RUNWAY CHECK ",
      "ZSAR",     "SEARCH AND RESCUE UPPER PARK",
      "ZSMOKEO",  "SMOKE CHECK OUTSIDE",
      "ZSPILLL",  "SPILL LARGE",
      "ZSPILLS",  "SPILL SMALL",
      "ZSUI",     "SUICIDE ",
      "ZTCFIRE",  "TRAFFIC ACCIDENT VEH ON FIRE",
      "ZTCJAWS",  "TC EXTRICATION",
      "ZTCROLL",  "TC ROLLOVER",
      "ZTCTRAIN", "TC TRAIN",
      "ZTRAINC",  "CARGO TRAIN WRECK",
      "ZTRAINF",  "TRAIN FIRE",
      "ZTRANSF",  "TRANSFORMER FIRE",
      "ZUNK",     "FIRE UNKNOWN [C2C]",
      "ZUNKODO",  "UNKNOWN ODOR",
      "ZVEG",     "VEGETATION FIRE",
      "ZVEHL",    "VEHICLE LARGE",
      "ZVEHS",    "VEHICLE FIRE SMALL",
      "ZWIRE",    "WIRES DOWN"
  });
}
