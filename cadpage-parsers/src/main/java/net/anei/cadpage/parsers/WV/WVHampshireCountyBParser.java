package net.anei.cadpage.parsers.WV;

import net.anei.cadpage.parsers.dispatch.DispatchSPKParser;

/**
 * Hampshire County, WV (B)
 */
public class WVHampshireCountyBParser extends DispatchSPKParser {

  public WVHampshireCountyBParser() {
    super("HAMPSHIRE COUNTY", "WV");
  }

  @Override
  public String getFilter() {
    return "hamp911@hardynet.com";
  }

  @Override
  public int getMapFlags() {
    return MAP_FLG_PREFER_GPS;
  }
}
