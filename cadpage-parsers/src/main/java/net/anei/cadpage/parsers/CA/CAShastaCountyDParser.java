package net.anei.cadpage.parsers.CA;

import net.anei.cadpage.parsers.dispatch.DispatchA52Parser;

public class CAShastaCountyDParser extends DispatchA52Parser {

  public CAShastaCountyDParser() {
    super("SHASTA COUNTY", "CA");
  }

  @Override
  public String getFilter() {
    return "SHASCOM@ci.redding.ca.us";
  }

  @Override
  public int getMapFlags() {
    return MAP_FLG_PREFER_GPS | MAP_FLG_SUPPR_LA;
  }

}
