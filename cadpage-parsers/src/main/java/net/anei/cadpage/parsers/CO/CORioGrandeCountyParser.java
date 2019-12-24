package net.anei.cadpage.parsers.CO;

import net.anei.cadpage.parsers.dispatch.DispatchPremierOneParser;

public class CORioGrandeCountyParser extends DispatchPremierOneParser {
  
  public CORioGrandeCountyParser() {
    super("RIO GRANDE COUNTY", "CO");
  }
  
  @Override
  public String getFilter() {
    return "@state.co.us";
  }
}
