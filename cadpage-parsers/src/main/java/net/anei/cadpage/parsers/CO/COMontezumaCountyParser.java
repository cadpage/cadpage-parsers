package net.anei.cadpage.parsers.CO;

import net.anei.cadpage.parsers.dispatch.DispatchA67Parser;

public class COMontezumaCountyParser extends DispatchA67Parser {
  
  public  COMontezumaCountyParser() {
    super(":", CITY_LIST, "MONTEZUMA COUNTY", "CO", A67_OPT_PLACE | A67_OPT_CROSS,
          "CORTEZ|DOLORES|LEW-ARR|MANCOS|MVP|PLV|RICO", ".*");
    removeWords("RR");   // RR is a unit, not a cross street
  }
  
  private static final String[] CITY_LIST = new String[]{
      "ARRIOLA",
      "CAHONE",
      "CORTEZ",
      "DOLORES",
      "LEWIS",
      "MANCOS",
      "MESA VERDE",
      "PLEASANT VIEW",
      "TOWAOC",
      "YELLOW JACKET",
      
      // Delores County
      "CAHONE",
      "DOVE CREEK",
      "RICO"
  };
}
