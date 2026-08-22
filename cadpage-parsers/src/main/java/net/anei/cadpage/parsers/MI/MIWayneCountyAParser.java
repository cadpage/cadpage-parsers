package net.anei.cadpage.parsers.MI;

import net.anei.cadpage.parsers.dispatch.DispatchH03Parser;

public class MIWayneCountyAParser extends DispatchH03Parser {

  public MIWayneCountyAParser() {
    this("WAYNE COUNTY", "MI");
  }

  MIWayneCountyAParser(String defCity, String defState) {
    super(defCity, defState);
  }

  @Override
  public String getFilter( ) {
    return "MSP@michigan.gov";
  }

}
