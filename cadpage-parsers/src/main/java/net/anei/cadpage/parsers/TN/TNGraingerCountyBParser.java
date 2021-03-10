package net.anei.cadpage.parsers.TN;

import net.anei.cadpage.parsers.dispatch.DispatchA74Parser;

public class TNGraingerCountyBParser extends DispatchA74Parser {

  public TNGraingerCountyBParser() {
    super("GRAINGER COUNTY", "TN");
  }

  @Override
  public String getFilter() {
    return "dispatch@911comm1.info";
  }

  @Override
  public int getMapFlags() {
    return MAP_FLG_PREFER_GPS;
  }
}
