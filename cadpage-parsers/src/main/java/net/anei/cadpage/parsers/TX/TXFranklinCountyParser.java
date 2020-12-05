package net.anei.cadpage.parsers.TX;

import net.anei.cadpage.parsers.dispatch.DispatchA71Parser;

public class TXFranklinCountyParser extends DispatchA71Parser {

  public TXFranklinCountyParser() {
    super("FRANKLIN COUNTY", "TX");
  }

  @Override
  public String getFilter() {
    return "franklin@co.franklin.tx.us";
  }

  @Override
  public int getMapFlags() {
    return MAP_FLG_PREFER_GPS | MAP_FLG_SUPPR_LA;
  }
}
