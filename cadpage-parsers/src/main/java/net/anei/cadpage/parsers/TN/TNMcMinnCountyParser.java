package net.anei.cadpage.parsers.TN;

import net.anei.cadpage.parsers.dispatch.DispatchA86Parser;

public class TNMcMinnCountyParser extends DispatchA86Parser {

  public TNMcMinnCountyParser() {
    super("MCMINN COUNTY", "TN");
  }

  @Override
  public String getFilter() {
    return "dispatch@mcminntn911.info";
  }

  @Override
  public int getMapFlags() {
    return MAP_FLG_PREFER_GPS;
  }

}
