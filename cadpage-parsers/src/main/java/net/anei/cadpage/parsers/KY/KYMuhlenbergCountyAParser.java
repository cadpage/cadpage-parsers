package net.anei.cadpage.parsers.KY;

import net.anei.cadpage.parsers.dispatch.DispatchA27Parser;

/**
 * Muhlenberg County, KY (A)
 */
public class KYMuhlenbergCountyAParser extends DispatchA27Parser {

  public KYMuhlenbergCountyAParser() {
    super("MUHLENBERG COUNTY", "KY");
  }

  @Override
  public String getFilter() {
    return "cis@muhlenberg911.org";
  }

  @Override
  public int getMapFlags() {
    return MAP_FLG_PREFER_GPS;
  }
}
