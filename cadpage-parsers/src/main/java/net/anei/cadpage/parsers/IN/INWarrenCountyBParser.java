package net.anei.cadpage.parsers.IN;

import net.anei.cadpage.parsers.dispatch.DispatchSPKParser;

public class INWarrenCountyBParser extends DispatchSPKParser {

  public INWarrenCountyBParser() {
    super("WARREN COUNTY", "IN");
  }

  @Override
  public String getFilter() {
    return "cad@fwrdc.net,dispatch@fountaincounty.in.gov";
  }

  @Override
  public int getMapFlags() {
    return MAP_FLG_PREFER_GPS;
  }

}
