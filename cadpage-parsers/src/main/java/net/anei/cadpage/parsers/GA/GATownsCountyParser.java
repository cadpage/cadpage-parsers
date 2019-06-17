package net.anei.cadpage.parsers.GA;

import net.anei.cadpage.parsers.CodeSet;
import net.anei.cadpage.parsers.dispatch.DispatchB2Parser;

public class GATownsCountyParser extends DispatchB2Parser {
  
  public GATownsCountyParser() {
    super("TOWNS911:", CITY_LIST, "TOWNS COUNTY", "GA");
    setupCallList(CALL_LIST);
    setupMultiWordStreets(MWORD_STREET_LIST);
  }
  
  public String getFilter() {
    return "TOWNS911@TownsCountyGA.com";
  }
  
  private static final String[] MWORD_STREET_LIST = new String[]{
      "EAGLE MTN",
      "HIDDEN FIELDS",
      "RIVER BANK",
      "SUNNY MEADOWS"
  };
  
  private static final CodeSet CALL_LIST = new CodeSet(
      "CHEST PAIN",
      "DIFFICULTY BREATHING",
      "FALL",
      "LANDING ZONE",
      "LOW BLOOD PRESSURE",
      "MEDICAL ALERT",
      "MOTOR VEHICLE ACCIDENT",
      "SMOKE",
      "TEST FOR TESTING",
      "UNRESPONSIVE",
      "WEAKNESS"
  );
  
  private static final String[] CITY_LIST = new String[]{
      
      //Incorporated cities
      "HIAWASSEE",
      "YOUNG HARRIS",

      // Census-designated place
      "TATE CITY",

      // Other unincorporated communities
      "ALEXANDERS MILL",
      "BELL CREEK",
      "BUGSCUFFLE",
      "FODDER CREEK",
      "HOG CREEK",
      "PLOTT TOWN",
      "UPPER HIGHTOWER"
  };

}
