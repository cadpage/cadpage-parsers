package net.anei.cadpage.parsers.MI;

import net.anei.cadpage.parsers.dispatch.DispatchSPKParser;

public class MIPresqueIsleCountyBParser extends DispatchSPKParser {

  public MIPresqueIsleCountyBParser() {
    super("PRESQUE ISLE COUNTY", "MI");
  }

  @Override
  public String getFilter() {
    return "dispatch@presqueislesheriff.com";
  }

  @Override
  public int getMapFlags() {
    return MAP_FLG_PREFER_GPS;
  }
}
