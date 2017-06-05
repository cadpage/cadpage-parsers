package net.anei.cadpage.parsers.TN;

import net.anei.cadpage.parsers.dispatch.DispatchGeoconxParser;


public class TNRheaCountyAParser extends DispatchGeoconxParser {
  
  public TNRheaCountyAParser() {
    super("RHEA COUNTY", "TN");
  }
  
  @Override
  public String getFilter() {
    return "rheacotn@911email.net,dispatch@911email.org";
  }
}
