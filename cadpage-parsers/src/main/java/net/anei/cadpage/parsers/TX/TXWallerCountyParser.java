package net.anei.cadpage.parsers.TX;

import net.anei.cadpage.parsers.dispatch.DispatchA71Parser;

public class TXWallerCountyParser extends DispatchA71Parser {

  public TXWallerCountyParser() {
    super("WALLER COUNTY", "TX");
  }

  @Override
  public int getMapFlags() {
    return MAP_FLG_SUPPR_LA | MAP_FLG_PREFER_GPS;
  }

}
