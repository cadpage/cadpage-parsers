package net.anei.cadpage.parsers.MI;

import net.anei.cadpage.parsers.dispatch.DispatchH03Parser;

public class MIOtsegoCountyParser extends DispatchH03Parser {

  public MIOtsegoCountyParser() {
    super("OTSEGO COUNTY", "MI");
  }

  @Override
  public String getFilter() {
    return "MSP@michigan.gov";
  }

}
