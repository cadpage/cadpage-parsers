package net.anei.cadpage.parsers.GA;

import net.anei.cadpage.parsers.dispatch.DispatchA71Parser;

public class GABanksCountyParser extends DispatchA71Parser {

  public GABanksCountyParser() {
    super("BANKS COUNTY", "GA");
  }

  public int getMapFlags() {
    return MAP_FLG_PREFER_GPS;
  }
}
