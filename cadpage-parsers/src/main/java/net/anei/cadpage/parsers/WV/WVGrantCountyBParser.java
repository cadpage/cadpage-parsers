package net.anei.cadpage.parsers.WV;

import net.anei.cadpage.parsers.dispatch.DispatchSPKParser;

/**
 * Grant County, WV (B)
 */
public class WVGrantCountyBParser extends DispatchSPKParser {

  public WVGrantCountyBParser() {
    super("GRANT COUNTY", "WV");
  }

  @Override
  public String getFilter() {
    return "cadreports@hardynet.com";
  }
}
