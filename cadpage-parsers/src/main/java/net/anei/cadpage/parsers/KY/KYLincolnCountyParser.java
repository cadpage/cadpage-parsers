package net.anei.cadpage.parsers.KY;

import net.anei.cadpage.parsers.dispatch.DispatchSPKParser;

public class KYLincolnCountyParser extends DispatchSPKParser {
  
  public KYLincolnCountyParser() {
    super("LINCOLN COUNTY", "KY");
  }
  
  @Override
  public String getFilter() {
    return "noreply@interact911.com";
  }
  
}
