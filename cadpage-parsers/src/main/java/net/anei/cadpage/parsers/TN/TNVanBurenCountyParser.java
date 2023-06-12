package net.anei.cadpage.parsers.TN;

import net.anei.cadpage.parsers.dispatch.DispatchA86Parser;

public class TNVanBurenCountyParser extends DispatchA86Parser {

  public TNVanBurenCountyParser() {
    super("VAN BUREN COUNTY", "TN");
  }

  @Override
  public String getFilter() {
    return "dispatch@vanburentne911.info";
  }

  @Override
  public int getMapFlags() {
    return MAP_FLG_PREFER_GPS;
  }
}
