package net.anei.cadpage.parsers.KY;

import net.anei.cadpage.parsers.dispatch.DispatchA27Parser;

/**
 * Clark County, KY
 */
public class KYClarkCountyParser extends DispatchA27Parser {

  public KYClarkCountyParser() {
    super("CLARK COUNTY", "KY","[A-Z]{1,3}FD|[-A-Z]+\\d+|\\d{8}");
  }

  @Override
  public String getFilter() {
    return "noreply@cisusa.org,grant.wyatt@active911.com,winchesterpd@cissystem.com";
  }

}
