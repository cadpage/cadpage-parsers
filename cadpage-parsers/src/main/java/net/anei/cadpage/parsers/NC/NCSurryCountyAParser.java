package net.anei.cadpage.parsers.NC;

import net.anei.cadpage.parsers.dispatch.DispatchA71Parser;

/**
 * Surry County, NC
 */
public class NCSurryCountyAParser extends DispatchA71Parser {

  public NCSurryCountyAParser() {
    super("SURRY COUNTY", "NC");
  }

  @Override
  public int getMapFlags() {
    return MAP_FLG_PREFER_GPS;
  }
}
