package net.anei.cadpage.parsers.MO;

import net.anei.cadpage.parsers.dispatch.DispatchC16Parser;

public class MONodawayCountyParser extends DispatchC16Parser {

  public MONodawayCountyParser() {
    super("NODAWAY COUNTY", "MO");
  }

  @Override
  public int getMapFlags() {
    return  MAP_FLG_PREFER_GPS;
  }
}
