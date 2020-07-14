package net.anei.cadpage.parsers.GA;

import net.anei.cadpage.parsers.dispatch.DispatchA71Parser;

public class GAFranklinCountyBParser extends DispatchA71Parser {

  public GAFranklinCountyBParser() {
    super("FRANKLIN COUNTY", "GA");
  }

  @Override
  public int getMapFlags() {
    return MAP_FLG_PREFER_GPS;
  }

}
