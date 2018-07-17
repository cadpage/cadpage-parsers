package net.anei.cadpage.parsers.MI;

import net.anei.cadpage.parsers.dispatch.DispatchSPKParser;

public class MIGratiotCountyBParser extends DispatchSPKParser {
  
  public MIGratiotCountyBParser() {
    super("GRATIOT COUNTY", "MI");
  }
  
  @Override
  public String getFilter() {
    return "GratiotEMD@GratiotMI.com";
  }

}
