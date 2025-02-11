package net.anei.cadpage.parsers.FL;

import net.anei.cadpage.parsers.dispatch.DispatchA71Parser;

public class FLHardeeCountyBParser extends DispatchA71Parser {

  public FLHardeeCountyBParser() {
    super("HARDEE COUNTY", "FL");
  }

  @Override
  public String getFilter() {
    return "notify@somahub.io";
  }

  @Override
  public int getMapFlags() {
    return MAP_FLG_PREFER_GPS | MAP_FLG_SUPPR_LA;
  }

}
