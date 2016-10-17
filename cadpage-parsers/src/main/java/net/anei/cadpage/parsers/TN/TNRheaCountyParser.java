package net.anei.cadpage.parsers.TN;

import net.anei.cadpage.parsers.dispatch.DispatchGeoconxParser;


public class TNRheaCountyParser extends DispatchGeoconxParser {
  
  public TNRheaCountyParser() {
    super("RHEA COUNTY", "TN");
  }
  
  @Override
  public String getFilter() {
    return "rheacotn@911email.net";
  }
}
