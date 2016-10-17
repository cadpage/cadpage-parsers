package net.anei.cadpage.parsers.VA;

import net.anei.cadpage.parsers.dispatch.DispatchSouthernParser;



public class VAWashingtonCountyParser extends DispatchSouthernParser {
  
  public VAWashingtonCountyParser() {
    super(CITY_LIST, "WASHINGTON COUNTY", "VA", DSFLAG_DISPATCH_ID | DSFLAG_LEAD_PLACE);
  }

  @Override
  public String getFilter() {
    return "@washcova.com";
  }
  
  private static final String[] CITY_LIST = new String[]{
    "ABINGDON",
    "DAMASCUS",
    "GLADE SPRING",
    "MEADOWVIEW",
    "SALTVILLE",
    "EMORY-MEADOWVIEW",
    "MENDOTA",
    
    "BRISTOL"
  }; 
}
