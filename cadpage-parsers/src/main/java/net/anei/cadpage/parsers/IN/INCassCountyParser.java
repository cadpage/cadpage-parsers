package net.anei.cadpage.parsers.IN;

import net.anei.cadpage.parsers.CodeSet;
import net.anei.cadpage.parsers.dispatch.DispatchA29Parser;

public class INCassCountyParser extends DispatchA29Parser {

  public INCassCountyParser() {
    super(CITY_LIST, "STARKE COUNTY", "IN");
    setupCallList(CALL_LIST);
  }
  
  @Override
  public String getFilter() {
    return "e911.pagegate@co.cass.in.us";
  }

  private static final CodeSet CALL_LIST = new CodeSet(
      
      "ALARM",
      "ACCIDENT (INJURIES)",
      "FIRE ALARM",
      "FIRE FIELD",
      "GAS SPILL",
      "INTOXICATED PERSON",
      "MEDICAL",
      "TRAFFIC STOP"
      
  );
  
  private static final String[] CITY_LIST = new String[]{
      
      //CITY
      
          "LOGANSPORT",

      //TOWNS

          "GALVESTON",
          "ONWARD",
          "ROYAL CENTER",
          "WALTON",
          "TWELVE MILE",
          "LUCERNE",
          "YOUNG AMERICA",

      //CENSUS-DESIGNATED PLACE

          "GRISSOM AFB",

      //UNINCORPORATED PLACES

          "ADAMSBORO",
          "ANOKA",
          "CLYMERS",
          "DEACON",
          "DUNKIRK",
          "GEORGETOWN",
          "HOOVER",
          "KENNETH",
          "LAKE CICOTT",
          "LEWISBURG",
          "LINCOLN",
          "LUCERNE",
          "METEA",
          "MIAMI BEND",
          "MOUNT PLEASANT",
          "NEW WAVERLY",
          "POTAWATOMI POINT",
          "TWELVE MILE",
          "YOUNG AMERICA",

      //EXTINCT TOWNS

          "CIRCLEVILLE",
          "TABERVILLE",

      //TOWNSHIPS

          "ADAMS",
          "BETHLEHEM",
          "BOONE",
          "CLAY",
          "CLINTON",
          "DEER CREEK",
          "EEL",
          "HARRISON",
          "JACKSON",
          "JEFFERSON",
          "MIAMI",
          "NOBLE",
          "TIPTON",
          "WASHINGTON"

  
  };
}
