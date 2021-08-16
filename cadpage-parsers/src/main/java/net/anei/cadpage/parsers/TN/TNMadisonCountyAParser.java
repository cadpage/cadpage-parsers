package net.anei.cadpage.parsers.TN;

import net.anei.cadpage.parsers.dispatch.DispatchA74Parser;

public class TNMadisonCountyAParser extends DispatchA74Parser {

  public TNMadisonCountyAParser() {
    super("MADISON COUNTY", "TN");
  }

  @Override
  public String getFilter() {
    return "Dispatch@MadisonTN911.info";
  }

  @Override
  public int getMapFlags() {
    return MAP_FLG_PREFER_GPS;
  }
}
