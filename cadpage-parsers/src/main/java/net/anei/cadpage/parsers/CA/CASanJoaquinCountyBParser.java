package net.anei.cadpage.parsers.CA;

import net.anei.cadpage.parsers.dispatch.DispatchA84Parser;

public class CASanJoaquinCountyBParser extends DispatchA84Parser {

  public CASanJoaquinCountyBParser() {
    super("SAN JOAQUIN COUNTY", "CA");
  }

  @Override
  public int getMapFlags() {
    return MAP_FLG_SUPPR_LA | MAP_FLG_PREFER_GPS;
  }
}
