package net.anei.cadpage.parsers.KY;

import net.anei.cadpage.parsers.dispatch.DispatchA27Parser;

/**
 * Allen County, KY
 */
public class KYAllenCountyParser extends DispatchA27Parser {

  public KYAllenCountyParser() {
    super("ALLEN COUNTY", "KY");
  }

  @Override
  public String getFilter() {
    return "dispatch@scottsvillepolice.org";
  }

  @Override
  public int getMapFlags() {
    return MAP_FLG_PREFER_GPS;
  }
}
