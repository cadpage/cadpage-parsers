package net.anei.cadpage.parsers.SC;

import net.anei.cadpage.parsers.dispatch.DispatchSPKParser;

public class SCCalhounCountyParser extends DispatchSPKParser {

  public SCCalhounCountyParser() {
    super("CALHOUN COUNTY", "SC");
  }

  @Override
  public String getFilter() {
    return "cad@calhouscsheriff.com,cad@calhounscsheriff.com";
  }

  @Override
  public int getMapFlags() {
    return MAP_FLG_PREFER_GPS;
  }
}
