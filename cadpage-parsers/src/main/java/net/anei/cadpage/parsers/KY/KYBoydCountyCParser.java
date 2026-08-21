package net.anei.cadpage.parsers.KY;

import net.anei.cadpage.parsers.dispatch.DispatchA27Parser;

public class KYBoydCountyCParser extends DispatchA27Parser {

  public KYBoydCountyCParser() {
    super("BOYD COUNTY", "KY");
  }

  @Override
  public String getFilter() {
    return "bc911@boydcountyky.gov,cis@carterco911.com";
  }

  @Override
  public int getMapFlags() {
    return MAP_FLG_PREFER_GPS;
  }
}
