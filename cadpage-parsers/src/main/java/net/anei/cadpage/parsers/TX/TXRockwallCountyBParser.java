package net.anei.cadpage.parsers.TX;

import net.anei.cadpage.parsers.dispatch.DispatchA9Parser;


/**
 * Rockwall County, TX
 */
public class TXRockwallCountyBParser extends DispatchA9Parser {
   
  public TXRockwallCountyBParser() {
    super("ROCKWALL COUNTY", "TX");
  }
  
  @Override
  public String getFilter() {
    return "Fire_Records";
  }
  

}
