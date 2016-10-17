package net.anei.cadpage.parsers.IN;

import net.anei.cadpage.parsers.dispatch.DispatchA19Parser;

public class INRandolphCountyParser extends DispatchA19Parser {
  
  public INRandolphCountyParser() {
    super("RANDOLPH COUNTY", "IN");
  }
  
  @Override
  public String getFilter() {
    return "andolph.hsem@randolph.in.gov";
  }
}
