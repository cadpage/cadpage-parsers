package net.anei.cadpage.parsers.AL;

import net.anei.cadpage.parsers.dispatch.DispatchA71Parser;

public class ALButlerCountyAParser extends DispatchA71Parser {

  public ALButlerCountyAParser() {
    super("BUTLER COUNTY", "AL");
  }

  @Override
  public String getFilter() {
    return "cad@pikecounty911.org";
  }

  @Override
  public int getMapFlags() {
    return MAP_FLG_PREFER_GPS;
  }
}
