package net.anei.cadpage.parsers.GA;

import net.anei.cadpage.parsers.dispatch.DispatchA19Parser;

public class GAMaconCountyBParser extends DispatchA19Parser {

  public GAMaconCountyBParser() {
    this("MACON COUNTY");
  }

  GAMaconCountyBParser(String county) {
    super(county, "GA");
  }

  @Override
  public String getFilter() {
    return "dispatch@mfre911.com";
  }

  @Override
  public String getAliasCode() {
    return "GAMaconCountyB";
  }

  @Override
  public int getMapFlags() {
    return MAP_FLG_PREFER_GPS;
  }
}
