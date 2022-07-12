package net.anei.cadpage.parsers.KY;

import net.anei.cadpage.parsers.dispatch.DispatchSPKParser;

public class KYMorganCountyBParser extends DispatchSPKParser {
  
  public KYMorganCountyBParser() {
    super("MORGAN COUNTY", "KY");
  }
  
  @Override
  public String getFilter() {
    return "wlpd1@mrtc.com";
  }

}
