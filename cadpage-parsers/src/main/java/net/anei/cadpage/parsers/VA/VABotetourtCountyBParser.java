package net.anei.cadpage.parsers.VA;

import net.anei.cadpage.parsers.dispatch.DispatchA71Parser;

public class VABotetourtCountyBParser extends DispatchA71Parser {

  public VABotetourtCountyBParser() {
    super("BOTETOURT COUNTY", "VA");
  }

  @Override
  public int getMapFlags() {
    return MAP_FLG_PREFER_GPS;
  }
}
