package net.anei.cadpage.parsers.TN;

import net.anei.cadpage.parsers.dispatch.DispatchGeoconxParser;


public class TNCumberlandCountyParser extends DispatchGeoconxParser {
  
  public TNCumberlandCountyParser() {
    super("CUMBERLAND COUNTY", "TN");
    setupMultiWordStreets("MILLSTONE MNTN");
  }
  
  @Override
  public String getFilter() {
    return "@911email.net,@911email.org";
  }
}
