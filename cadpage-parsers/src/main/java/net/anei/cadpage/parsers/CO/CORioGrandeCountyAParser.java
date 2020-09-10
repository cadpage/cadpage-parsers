package net.anei.cadpage.parsers.CO;

import net.anei.cadpage.parsers.dispatch.DispatchPremierOneParser;

public class CORioGrandeCountyAParser extends DispatchPremierOneParser {
  
  public CORioGrandeCountyAParser() {
    super("RIO GRANDE COUNTY", "CO");
  }
  
  @Override
  public String getFilter() {
    return "@state.co.us,@csp.noreply";
  }
}
