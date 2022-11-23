package net.anei.cadpage.parsers.AL;

import net.anei.cadpage.parsers.dispatch.DispatchA71Parser;

public class ALButlerCountyParser extends DispatchA71Parser {

  public ALButlerCountyParser() {
    super("BUTLER COUNTY", "GA");
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
