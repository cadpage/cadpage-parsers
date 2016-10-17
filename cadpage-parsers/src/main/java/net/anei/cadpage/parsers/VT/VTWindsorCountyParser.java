package net.anei.cadpage.parsers.VT;

import net.anei.cadpage.parsers.dispatch.DispatchA19Parser;

public class VTWindsorCountyParser extends DispatchA19Parser {
  
  public VTWindsorCountyParser() {
    this("WINDSOR COUNTY");
  }
  
  protected VTWindsorCountyParser(String county) {
    super(county, "VT");
  }
  
  @Override
  public String getAliasCode() {
    return "VTWindsorCounty";
  }
}
