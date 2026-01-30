package net.anei.cadpage.parsers.CO;

import net.anei.cadpage.parsers.dispatch.DispatchH03Parser;

public class CORioGrandeCountyCParser extends DispatchH03Parser {

  public CORioGrandeCountyCParser() {
    super("RIO GRANDE COUNTY", "CO");
  }

  @Override
  public String getFilter() {
    return "@state.co.us";
  }
}
