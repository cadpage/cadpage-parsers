package net.anei.cadpage.parsers.TX;

import net.anei.cadpage.parsers.dispatch.DispatchA71Parser;

public class TXJohnsonCountyBParser extends DispatchA71Parser {

  public TXJohnsonCountyBParser() {
    super("JOHNSON COUNTY", "TX");
  }

  @Override
  public String getFilter() {
    return "notify@somahub.io";
  }

  @Override
  public int getMapFlags() {
    return  MAP_FLG_PREFER_GPS | MAP_FLG_SUPPR_LA;
  }
}
