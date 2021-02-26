package net.anei.cadpage.parsers.CA;

import net.anei.cadpage.parsers.dispatch.DispatchA49Parser;

public class CASanBernardinoCountyEParser extends DispatchA49Parser {

  public CASanBernardinoCountyEParser() {
    super("SAN BERNARDINO COUNTY", "CA");
  }

  @Override
  public String getFilter() {
    return "cadalert@adsisoftware.com";
  }

}
