package net.anei.cadpage.parsers.VT;

import net.anei.cadpage.parsers.dispatch.DispatchA73Parser;

public class VTChittendenCountyDParser extends DispatchA73Parser {
  
  public VTChittendenCountyDParser() {
    this("CHITTENDEN COUNTY");
  }
  
  VTChittendenCountyDParser(String county) {
    super(county, "VT");
  }
  
  @Override
  public String getAliasCode() {
    return "VTChittendenCountyD";
  }
}
