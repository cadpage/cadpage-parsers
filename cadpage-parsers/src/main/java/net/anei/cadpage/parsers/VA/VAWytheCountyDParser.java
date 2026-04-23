package net.anei.cadpage.parsers.VA;

import net.anei.cadpage.parsers.dispatch.DispatchA71Parser;

public class VAWytheCountyDParser extends DispatchA71Parser {

  public VAWytheCountyDParser() {
    super("WYTHE COUNTY", "VA");
  }

  public int getMapFlags() {
    return MAP_FLG_PREFER_GPS;
  }
}
