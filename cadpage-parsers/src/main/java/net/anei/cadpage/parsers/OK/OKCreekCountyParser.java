package net.anei.cadpage.parsers.OK;

import net.anei.cadpage.parsers.dispatch.DispatchA99Parser;

public class OKCreekCountyParser extends DispatchA99Parser {

  public OKCreekCountyParser() {
    super("CREEK COUNTY", "OK");
  }

  @Override
  public int getMapFlags() {
    return MAP_FLG_PREFER_GPS;
  }
}
