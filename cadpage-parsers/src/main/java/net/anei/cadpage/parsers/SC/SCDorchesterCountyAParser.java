package net.anei.cadpage.parsers.SC;

import net.anei.cadpage.parsers.dispatch.DispatchA71Parser;



public class SCDorchesterCountyAParser extends DispatchA71Parser {

  public SCDorchesterCountyAParser() {
    super("DORCHESTER COUNTY", "SC");
  }

  @Override
  public int getMapFlags() {
    return MAP_FLG_PREFER_GPS;
  }
}
