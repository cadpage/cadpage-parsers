package net.anei.cadpage.parsers.VT;

import net.anei.cadpage.parsers.dispatch.DispatchA19Parser;

public class VTWindsorCountyAParser extends DispatchA19Parser {
  
  public VTWindsorCountyAParser() {
    this("WINDSOR COUNTY");
  }
  
  protected VTWindsorCountyAParser(String county) {
    super(county, "VT");
  }
  
  @Override
  public String getAliasCode() {
    return "VTWindsorCounty";
  }
}
