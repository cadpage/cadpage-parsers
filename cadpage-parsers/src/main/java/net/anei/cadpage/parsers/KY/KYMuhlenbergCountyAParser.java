package net.anei.cadpage.parsers.KY;

import net.anei.cadpage.parsers.dispatch.DispatchA86Parser;

/**
 * Muhlenberg County, KY (A)
 */
public class KYMuhlenbergCountyAParser extends DispatchA86Parser {

  public KYMuhlenbergCountyAParser() {
    super("MUHLENBERG COUNTY", "KY");
  }

  @Override
  public String getFilter() {
    return "Dispatch@MuhlenbergKYE911.info";
  }

  @Override
  public int getMapFlags() {
    return MAP_FLG_PREFER_GPS;
  }
}
