package net.anei.cadpage.parsers.GA;

import net.anei.cadpage.parsers.dispatch.DispatchSPKParser;

public class GADawsonCountyParser extends DispatchSPKParser {
  
  public GADawsonCountyParser() {
    super("DAWSON COUNTY", "GA");
  }
  
  @Override
  public String getFilter() {
    return "dispatch@dawsoncounty.org,@dawsoncountyga.gov";
  }
  
  @Override
  public int getMapFlags() {
    return MAP_FLG_PREFER_GPS;
  }

}
