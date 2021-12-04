package net.anei.cadpage.parsers.TN;

import net.anei.cadpage.parsers.dispatch.DispatchA74Parser;


public class TNMooreCountyParser extends DispatchA74Parser {

  public TNMooreCountyParser() {
    super("MOORE COUNTY", "TN");
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
