package net.anei.cadpage.parsers.NC;

import net.anei.cadpage.parsers.dispatch.DispatchA71Parser;

/**
 * Surry County, NC
 */
public class NCSurryCountyParser extends DispatchA71Parser {

  public NCSurryCountyParser() {
    super("SURRY COUNTY", "NC");
  }

  @Override
  public int getMapFlags() {
    return MAP_FLG_PREFER_GPS;
  }
}
