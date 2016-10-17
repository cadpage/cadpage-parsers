package net.anei.cadpage.parsers.SC;

import net.anei.cadpage.parsers.MsgInfo.Data;
import net.anei.cadpage.parsers.dispatch.DispatchBParser;



public class SCFlorenceCountyParser extends DispatchBParser {
 
  public SCFlorenceCountyParser() {
    super(1, CITY_CODES, "FLORENCE COUNTY", "SC");
  }
  
  @Override
  protected boolean isPageMsg(String body) {
    return true;
  }
  
  @Override
  protected boolean parseMsg(String body, Data data) {
    
    // See if this is one of our pages
    if (! body.startsWith("FLORENCE CO 911:") && !body.startsWith("FLORENCE_CO_911")) return false;
    body = body.substring(16);
    int pt = body.indexOf('>');
    if (pt >= 0) data.strCode = body.substring(0,pt).trim();
    
    // Call superclass parser
    return super.parseMsg(body, data) && data.strCallId.length() > 0;
  }
  
  private static final String[] CITY_CODES = new String[]{
    "COWARD",
    "EFFINGHAM",
    "FLORENCE",
    "JOHNSONVILLE",
    "LAKE CITY",
    "MARS BLUFF",
    "OLANTA",
    "PAMPLICO",
    "QUINBY",
    "SCRANTON",
    "TIMMONSVILLE"
  };
}
