package net.anei.cadpage.parsers.KY;

import net.anei.cadpage.parsers.CodeSet;
import net.anei.cadpage.parsers.dispatch.DispatchB2Parser;

public class KYGravesCountyBParser extends DispatchB2Parser {
  
  public KYGravesCountyBParser() {
    super("911-CENTER:", CITY_LIST, "GRAVES COUNTY", "KY", B2_FORCE_CALL_CODE);
    setupCallList(CALL_LIST);
  }
  
  private static final String[] CITY_LIST = new String[]{

  //Cities
      
      "MAYFIELD",
      "WATER VALLEY",
      "WINGO",

  //Census-designated places

      "FANCY FARM",
      "FARMINGTON",
      "HICKORY",
      "LOWES",
      "PRYORSBURG",
      "SEDALIA",
      "SYMSONIA",

  //Other unincorporated communities

      "BELL CITY",
      "BOAZ",
      "CLEAR SPRINGS",
      "CUBA",
      "DOGWOOD",
      "DUBLIN",
      "DUKEDOM",
      "FAIRBANKS",
      "FELICIANA",
      "FOLSOMDALE",
      "GOLO",
      "KALER",
      "KANSAS",
      "LYNNVILLE",
      "MELBER",
      "NATCHEZ TRACE",
      "PILOT OAK",
      "POTTSVILLE",
      "SOUTH HIGHLAND",
      "STUBBLEFIELD",
      "TRI CITY",
      "VIOLA",
      "WEST VIOLA",
      "WESTPLAINS",
      "WHEEL"
   };
  
  private CodeSet CALL_LIST = new CodeSet(
      
      "911 HANGUP OR UNVERIFIED",
      "ACCIDENT WITH INJURIES",
      "ALL EMS/MEDICAL CALLS",
      "DOMESTIC ABUSE",
      "INFORMATION ONLY",
      "INTOXICATED PERSON",
      "OVRDOS>DRUG OVERDOSE",
      "SUSPICIOUS PERSON",
      "TRAFFIC HAZARD",
      "TRAFFIC STOP"
  );
}
