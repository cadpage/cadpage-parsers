package net.anei.cadpage.parsers.TX;

import net.anei.cadpage.parsers.dispatch.DispatchA27Parser;

public class TXCarsonCountyParser extends DispatchA27Parser {

  public TXCarsonCountyParser() {
    super("CARSON COUNTY", "TX");
  }

  @Override
  public String getFilter() {
    return "carsondispatch@co.carson.tx.us";
  }
  
  @Override
  public int getMapFlags() {
    return MAP_FLG_SUPPR_LA | MAP_FLG_PREFER_GPS;
  }

}
