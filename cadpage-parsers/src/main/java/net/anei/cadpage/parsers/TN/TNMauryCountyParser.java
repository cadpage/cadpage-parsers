package net.anei.cadpage.parsers.TN;

import net.anei.cadpage.parsers.dispatch.DispatchH03Parser;

public class TNMauryCountyParser extends DispatchH03Parser {

  public TNMauryCountyParser() {
    super("MAURY COUNTY", "TN");
  }

  @Override
  public String getFilter() {
    return "911@P1CAD.CommandCentral.com";
  }
}
