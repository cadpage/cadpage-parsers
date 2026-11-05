package net.anei.cadpage.parsers.MI;

import net.anei.cadpage.parsers.dispatch.DispatchC17Parser;

public class MISchoolcraftCountyParser extends DispatchC17Parser {

  public MISchoolcraftCountyParser() {
    super("SCHOOLCRAFT COUNTY", "MI");
  }

  @Override
  public int getMapFlags() {
    return  MAP_FLG_PREFER_GPS;
  }
}
