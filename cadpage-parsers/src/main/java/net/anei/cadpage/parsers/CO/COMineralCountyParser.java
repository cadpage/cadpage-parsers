package net.anei.cadpage.parsers.CO;

import net.anei.cadpage.parsers.dispatch.DispatchH03Parser;

public class COMineralCountyParser extends DispatchH03Parser {

  public COMineralCountyParser() {
    super("MINERAL COUNTY", "CO");
  }

  @Override
  public String getFilter() {
    return ".AL@csp.noreply";
  }

}
