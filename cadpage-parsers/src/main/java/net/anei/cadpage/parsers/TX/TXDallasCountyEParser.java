package net.anei.cadpage.parsers.TX;

import net.anei.cadpage.parsers.dispatch.DispatchA27Parser;

public class TXDallasCountyEParser extends DispatchA27Parser {

  public TXDallasCountyEParser() {
    super("DALLAS COUNTY", "TX");
  }

  @Override
  public String getFilter() {
    return "rfd@cor.gov,richardsonpdtx@cissystem.com";
  }

  @Override
  public int getMapFlags() {
    return MAP_FLG_PREFER_GPS | MAP_FLG_SUPPR_LA;
  }
}
