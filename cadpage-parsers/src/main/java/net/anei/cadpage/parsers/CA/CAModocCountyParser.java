package net.anei.cadpage.parsers.CA;

import net.anei.cadpage.parsers.dispatch.DispatchSPKParser;


public class CAModocCountyParser extends DispatchSPKParser {
  
  public CAModocCountyParser() {
    super("MODOC COUNTY", "CA");
  }
  
  @Override
  public String getFilter() {
    return "sheriffoffice@modocsheriff.us";
  }

}
