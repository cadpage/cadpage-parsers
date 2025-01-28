package net.anei.cadpage.parsers.PA;

import net.anei.cadpage.parsers.dispatch.DispatchA27Parser;


public class PATiogaCountyAParser extends DispatchA27Parser {

  public PATiogaCountyAParser() {
    super("TIOGA COUNTY", "PA");
  }

  @Override
  public String getFilter() {
    return "911@tiogacountypa.us";
  }

  @Override
  public int getMapFlags() {
    return MAP_FLG_PREFER_GPS;
  }
}
