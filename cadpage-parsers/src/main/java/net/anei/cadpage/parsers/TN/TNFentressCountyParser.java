package net.anei.cadpage.parsers.TN;

import net.anei.cadpage.parsers.dispatch.DispatchA74Parser;

public class TNFentressCountyParser extends DispatchA74Parser {

  public TNFentressCountyParser() {
    super("FENTRESS COUNTY", "TN");
  }

  @Override
  public String getFilter() {
    return "dispatch@fentresstne911.net";
  }

  @Override
  public int getMapFlags() {
    return MAP_FLG_PREFER_GPS;
  }

}
