package net.anei.cadpage.parsers.CA;

import net.anei.cadpage.parsers.dispatch.DispatchA69Parser;

public class CASantaClaraCountyCParser extends DispatchA69Parser {

  public CASantaClaraCountyCParser() {
    super("SANTA CLARA COUNTY", "CA");
  }

  public String getFilter() {
    return "scuecc@fire.ca.gov";
  }
}
