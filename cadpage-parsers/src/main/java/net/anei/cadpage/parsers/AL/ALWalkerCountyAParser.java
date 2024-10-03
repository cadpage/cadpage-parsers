package net.anei.cadpage.parsers.AL;

import net.anei.cadpage.parsers.dispatch.DispatchA71Parser;

/*
Walker County, AL
*/

public class ALWalkerCountyAParser extends DispatchA71Parser {

  public ALWalkerCountyAParser() {
    super("WALKER COUNTY","AL");
  }

  @Override
  public int getMapFlags() {
    return MAP_FLG_PREFER_GPS;
  }
}
