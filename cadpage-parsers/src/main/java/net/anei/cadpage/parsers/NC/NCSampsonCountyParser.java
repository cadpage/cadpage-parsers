package net.anei.cadpage.parsers.NC;

import net.anei.cadpage.parsers.dispatch.DispatchA71Parser;


public class NCSampsonCountyParser extends DispatchA71Parser {

  public NCSampsonCountyParser() {
    super("SAMPSON COUNTY", "NC");
  }

  @Override
  public int getMapFlags() {
    return MAP_FLG_PREFER_GPS;
  }
}
