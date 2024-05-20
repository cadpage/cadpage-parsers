package net.anei.cadpage.parsers.OK;

import net.anei.cadpage.parsers.dispatch.DispatchA89Parser;

public class OKWoodsCountyParser extends DispatchA89Parser {

  public OKWoodsCountyParser() {
    super("WOODS COUNTY", "OK");
  }

  @Override
  public String getFilter() {
    return "Dispatch@WoodsOKE911.info";
  }

  @Override
  public int getMapFlags() {
    return MAP_FLG_PREFER_GPS;
  }
}
