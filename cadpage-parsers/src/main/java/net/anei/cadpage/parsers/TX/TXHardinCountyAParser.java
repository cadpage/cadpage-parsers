package net.anei.cadpage.parsers.TX;

import net.anei.cadpage.parsers.dispatch.DispatchA71Parser;

public class TXHardinCountyAParser extends DispatchA71Parser {

  public TXHardinCountyAParser() {
    super("HARDIN COUNTY", "TX");
  }

  @Override
  public int getMapFlags() {
    return MAP_FLG_PREFER_GPS | MAP_FLG_SUPPR_LA;
  }

}
