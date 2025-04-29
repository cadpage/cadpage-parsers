package net.anei.cadpage.parsers.TX;

import net.anei.cadpage.parsers.dispatch.DispatchA77Parser;

public class TXHillCountyBParser extends DispatchA77Parser {

  public TXHillCountyBParser() {
    super("RN Test", "HILL COUNTY", "TX");
  }

  @Override
  public String getFilter() {
    return "911@co.hill.tx.us";
  }

  @Override
  public int getMapFlags() {
    return MAP_FLG_PREFER_GPS | MAP_FLG_SUPPR_LA;
  }

}
