package net.anei.cadpage.parsers.SC;

import net.anei.cadpage.parsers.dispatch.DispatchA71Parser;

public class SCDarlingtonCountyBParser extends DispatchA71Parser {

  public SCDarlingtonCountyBParser() {
    super("DARLINGTON COUNTY", "SC");
  }

  @Override
  public int getMapFlags() {
    return MAP_FLG_PREFER_GPS;
  }
}
