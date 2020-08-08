package net.anei.cadpage.parsers.MI;

import net.anei.cadpage.parsers.dispatch.DispatchH03Parser;

public class MIWayneCountyParser extends DispatchH03Parser {

  public MIWayneCountyParser() {
    super("WAYNE COUNTY", "MI");
  }

  @Override
  public String getFilter( ) {
    return "MSP@michigan.gov";
  }

}
