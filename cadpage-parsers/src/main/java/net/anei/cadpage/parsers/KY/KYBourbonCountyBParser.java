package net.anei.cadpage.parsers.KY;

import net.anei.cadpage.parsers.dispatch.DispatchA24Parser;
/**
 * Bourbon County, KY
 */
public class KYBourbonCountyBParser extends DispatchA24Parser {

  @Override
  public String getFilter() {
    return "@alert.active911.com";
  }

  public KYBourbonCountyBParser() {
    super("BOURBON COUNTY", "KY");
  }

}
