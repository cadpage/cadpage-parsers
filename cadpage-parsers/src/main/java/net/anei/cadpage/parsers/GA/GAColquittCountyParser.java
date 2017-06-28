package net.anei.cadpage.parsers.GA;

import net.anei.cadpage.parsers.CodeSet;
import net.anei.cadpage.parsers.dispatch.DispatchB2Parser;


public class GAColquittCountyParser extends DispatchB2Parser {

  public GAColquittCountyParser() {
    super("CC911:", CITY_LIST, "COLQUITT COUNTY", "GA");
    setupCallList(CALL_LIST);
    setupMultiWordStreets(MWORD_STREET_LIST);
  }
  
  @Override
  public String getFilter() {
    return "dpierce62@gmail.com";
  }
  
  private static String[] MWORD_STREET_LIST = new String[]{
      "DONA TURNER",
      "FUNSTON DOERUN",
      "FUNSTON SIGSBEE",
      "JAMES KING",
      "MIKE HORNE",
      "MT SINAI",
      "SAM SELLS",
      "SWIFT CANTEEN"
  };

  private static final String[] CITY_LIST = new String[]{
      
      //cities
      
      "BERLIN",
      "DOERUN",
      "ELLENTON",
      "FUNSTON",
      "MOULTRIE",
      "NORMAN PARK",
      "RIVERSIDE",
      
      //communities

      "AUTREYVILLE",
      "BARBERS",
      "BAY",
      "BAYBORO",
      "CENTER HILL",
      "COOL SPRINGS",
      "CROSLAND",
      "GANOR",
      "HARTSFIELD",
      "KIRKWOOD",
      "MARBLE",
      "MINNESOTA",
      "MURPHY",
      "NEW ELM",
      "PINEBORO",
      "SCHELEY",
      "SIGSBEE",
      "SUNSET",
      "TERRACE",
      "TICKNOR",
      "WELDON"
     };
  
  private static final CodeSet CALL_LIST = new CodeSet(
      "ACCIDENT/INJURIES",
      "ACCIDENT/NO REPORTED INJURIES",
      "ANY FIRE",
      "FIRE ALARM",
      "GAS LEAK / DIESEL SPILL",
      "GAS ODOR SMELL IN RESIDENT",
      "GRASS FIRE",
      "MEDICAL ALARM",
      "MEDICAL CALL",
      "SMOKE SMELL",
      "STRUCTURE FIRE",
      "SUICIDE/ATTEMPT",
      "SUSPICIOUS PERSON/VEHICLE",
      "VEHICLE FIRE"
  );

}

