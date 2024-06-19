package net.anei.cadpage.parsers.AL;

import net.anei.cadpage.parsers.dispatch.DispatchA71Parser;

public class ALChiltonCountyBParser extends DispatchA71Parser {

  public ALChiltonCountyBParser() {
    super("CHILTON COUNTY", "AL");
  }

  @Override
  public int getMapFlags() {
    return MAP_FLG_PREFER_GPS;
  }

}
