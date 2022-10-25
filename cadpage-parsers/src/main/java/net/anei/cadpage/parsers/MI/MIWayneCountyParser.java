package net.anei.cadpage.parsers.MI;

import net.anei.cadpage.parsers.dispatch.DispatchH03Parser;

public class MIWayneCountyParser extends DispatchH03Parser {

  public MIWayneCountyParser() {
    this("WAYNE COUNTY", "MI");
  }

  MIWayneCountyParser(String defCity, String defState) {
    super(defCity, defState);
  }

  @Override
  public String getFilter( ) {
    return "MSP@michigan.gov";
  }

}
