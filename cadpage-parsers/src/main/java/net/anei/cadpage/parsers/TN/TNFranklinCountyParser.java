package net.anei.cadpage.parsers.TN;

import net.anei.cadpage.parsers.dispatch.DispatchA65Parser;

public class TNFranklinCountyParser extends DispatchA65Parser {
  
  public TNFranklinCountyParser() {
    super(CITY_LIST, "FRANKLIN COUNTY", "TN");
  }
  
  @Override
  public String getFilter() {
    return "franklincotn@911email.net";
  }
  
  private static final String[] CITY_LIST = new String[]{

      //Cities
      
      "COWAN",
      "DECHERD",
      "TULLAHOMA",
      "WINCHESTER",

      //Towns
      
      "ESTILL SPRINGS",
      "HUNTLAND",
      "MONTEAGLE",

      // Census-designated place

      "SEWANEE",

      //Unincorporated communities

      "BEECH HILL",
      "BELVIDERE",
      "BROADVIEW",
      "MIDWAY",
      "SHADY GROVE",
      "SHERWOOD"
  };
}
