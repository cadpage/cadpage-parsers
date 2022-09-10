package net.anei.cadpage.parsers.KY;

import net.anei.cadpage.parsers.dispatch.DispatchA27Parser;

/**
 * Powell County, KY
 */

public class KYPowellCountyParser extends DispatchA27Parser {

  public KYPowellCountyParser() {
    super("POWELL COUNTY", "KY","[A-Z]{1,3}FD|[A-Z]+\\d+|\\d{8}");
  }

  @Override
  public String getFilter() {
    return "noreply@cisusa.org,jamie.suter@active911.com,powellcounty911center@gmail.com,powellcounty911@cissystem.com";
  }
}
