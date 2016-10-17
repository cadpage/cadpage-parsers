package net.anei.cadpage.parsers.TN;

import net.anei.cadpage.parsers.dispatch.DispatchGeoconxParser;


public class TNCarterCountyParser extends DispatchGeoconxParser {
  
  public TNCarterCountyParser() {
    super("CARTER COUNTY", "TN");
  }
  
  @Override
  public String getFilter() {
    return "dispatch@911email.net";
  } 
}
