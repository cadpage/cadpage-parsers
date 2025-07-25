package net.anei.cadpage.parsers.CO;

import net.anei.cadpage.parsers.dispatch.DispatchH03Parser;

public class COMoffatCountyParser extends DispatchH03Parser {

  public COMoffatCountyParser() {
    super("MOFFAT COUNTY", "CO");
  }

  @Override
  public String getFilter() {
    return "CG@state.co.us,CG@csp.noreply";
  }
}
