package net.anei.cadpage.parsers.CO;

import net.anei.cadpage.parsers.dispatch.DispatchPremierOneParser;

public class COMoffatCountyParser extends DispatchPremierOneParser {
  
  public COMoffatCountyParser() {
    super("MOFFAT COUNTY", "CO");
  }
  
  @Override
  public String getFilter() {
    return "CG@state.co.us,CG@csp.noreply";
  }
}
