package net.anei.cadpage.parsers.TX;

import net.anei.cadpage.parsers.dispatch.DispatchA71Parser;
/**
 * Van Zandt County, TX
 */
public class TXVanZandtCountyAParser extends DispatchA71Parser {

  public TXVanZandtCountyAParser() {
    super("VAN ZANDT COUNTY", "TX");
  }

  @Override
  public int getMapFlags() {
    return MAP_FLG_SUPPR_LA | MAP_FLG_PREFER_GPS;
  }
}
