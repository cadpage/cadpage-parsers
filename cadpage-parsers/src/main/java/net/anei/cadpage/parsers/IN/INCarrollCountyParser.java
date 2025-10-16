package net.anei.cadpage.parsers.IN;

import net.anei.cadpage.parsers.dispatch.DispatchSPKParser;

public class INCarrollCountyParser extends DispatchSPKParser {

  public INCarrollCountyParser() {
    super("CARROLL COUNTY", "IN");
  }

  @Override
  public String getFilter() {
    return "noreply@public-safety-cloud.com";
  }

  @Override
  public int getMapFlags() {
    return MAP_FLG_PREFER_GPS;
  }
}
