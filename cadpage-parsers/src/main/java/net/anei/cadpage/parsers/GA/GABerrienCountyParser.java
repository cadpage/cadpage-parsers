package net.anei.cadpage.parsers.GA;

import net.anei.cadpage.parsers.dispatch.DispatchA83Parser;

public class GABerrienCountyParser extends DispatchA83Parser {
  
  public GABerrienCountyParser() {
    super(CITY_LIST, "BERRIEN COUNTY", "GA", A83_REQ_SENDER);
  }
  
  @Override
  public String getFilter() {
    return "berrien.ga@ryzyliant.com";
  }
  
  private static final String[] CITY_LIST = new String[] {
      
      // Cities
      "NASHVILLE",
      "RAY CITY",
      
      // Towns
      "ALAPAHA",
      "ENIGMA",
      
      // Unincorporated communities
      "ALLENVILLE",
      "BANNOCKBURN",
      "COTTLE",
      "GLADYS",
      "GLORY",
      "NEW LOIS",
      "RIVERBEND",
      "WEBER",
      
      // Cook County
      "LENOX"
  };

}
