package net.anei.cadpage.parsers.VA;

import net.anei.cadpage.parsers.dispatch.DispatchA71Parser;

public class VABlandCountyParser extends DispatchA71Parser {

  public VABlandCountyParser() {
    super("BLAND COUNTY", "VA");
  }

  @Override
  public int getMapFlags() {
    return MAP_FLG_PREFER_GPS;
  }

}
