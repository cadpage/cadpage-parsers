package net.anei.cadpage.parsers.GA;

import net.anei.cadpage.parsers.dispatch.DispatchA73Parser;

public class GAMaconCountyBParser extends DispatchA73Parser {
  
  public GAMaconCountyBParser() {
    this("MACON COUNTY");
  }
  
  GAMaconCountyBParser(String county) {
    super(county, "GA");
  }
  
  @Override
  public String getFilter() {
    return "@alert.active911.com,middleflint@mfre911.com";
  }
  
  @Override
  public String getAliasCode() {
    return "GAMaconCountyB";
  }
}
