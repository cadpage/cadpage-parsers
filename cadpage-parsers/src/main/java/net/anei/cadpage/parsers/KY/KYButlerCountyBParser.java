package net.anei.cadpage.parsers.KY;

import net.anei.cadpage.parsers.dispatch.DispatchA74Parser;

public class KYButlerCountyBParser extends DispatchA74Parser {

  public KYButlerCountyBParser() {
    super("BUTLER COUNTY", "KY");
  }

  @Override
  public String getFilter() {
    return "Dispatch@ButlerKY911.info";
  }

  @Override
  public int getMapFlags() {
    return MAP_FLG_PREFER_GPS;
  }
}
