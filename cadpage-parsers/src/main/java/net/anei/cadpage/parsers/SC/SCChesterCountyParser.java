package net.anei.cadpage.parsers.SC;

import net.anei.cadpage.parsers.MsgInfo.Data;
import net.anei.cadpage.parsers.dispatch.DispatchBParser;



public class SCChesterCountyParser extends DispatchBParser {
 
  public SCChesterCountyParser() {
    super(CITY_CODES, "CHESTER COUNTY", "SC");
  }
  
  @Override
  public String getFilter() {
    return "CHESTER_911@Truvista.net";
  }
  
  @Override
  protected boolean isPageMsg(String body) {
    return true;
  }
  
  @Override
  protected boolean parseMsg(String body, Data data) {
    
    // See if this is one of our pages
    if (! body.startsWith("CHESTER_911:")) return false;
    body = body.substring(12).trim();
    int pt = body.indexOf('>');
    if (pt < 0) return false;
    data.strCode = body.substring(0,pt).trim();
    
    // Call superclass parser
    body = body.replace('@', '&');
    if (!super.parseMsg(body, data)) return false;
    return true;
  }
  
  private static final String[] CITY_CODES = new String[]{
    
    // Cities
    "CHESTER",

    // Towns
    "FORT LAWN",
    "GREAT FALLS",
    "LOWRYS",
    "RICHBURG",

    // Other populated places
    "BLACKSTOCK",
    "EDGEMOOR",
    "EUREKA MILL",
    "GAYLE MILL",
    "LANDO",
    "LEEDS",
    "WILKSBURG"
  };
}
