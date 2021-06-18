package net.anei.cadpage.parsers.TX;

import net.anei.cadpage.parsers.dispatch.DispatchA19Parser;

public class TXHillCountyParser extends DispatchA19Parser {

  public TXHillCountyParser() {
    super("HILL COUNTY", "TX");
  }

  @Override
  public String getFilter() {
    return "cad@co.hill.tx.us";
  }

  @Override
  public int getMapFlags() {
    return MAP_FLG_SUPPR_LA | MAP_FLG_PREFER_GPS;
  }

}
