package net.anei.cadpage.parsers.KY;

import net.anei.cadpage.parsers.dispatch.DispatchSPKParser;



public class KYTaylorCountyParser extends DispatchSPKParser {
  
  public KYTaylorCountyParser() {
    super("TAYLOR COUNTY", "KY");
  }
  
  @Override
  public String getFilter() {
    return "noreply@interact911.com";
  }
}
