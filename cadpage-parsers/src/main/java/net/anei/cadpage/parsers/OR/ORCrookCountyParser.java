package net.anei.cadpage.parsers.OR;


import net.anei.cadpage.parsers.dispatch.DispatchA22Parser;



public class ORCrookCountyParser extends DispatchA22Parser {
  
  public ORCrookCountyParser() {
    super("CROOK COUNTY", "OR");
  }
  
  @Override
  public String getFilter() {
    return "dispatch@prinevillepd.org";
  }
}
