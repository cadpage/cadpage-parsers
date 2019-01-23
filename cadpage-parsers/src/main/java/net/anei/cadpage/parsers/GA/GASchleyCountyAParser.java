package net.anei.cadpage.parsers.GA;

import net.anei.cadpage.parsers.dispatch.DispatchA19Parser;


public class GASchleyCountyAParser extends DispatchA19Parser {
  
  public GASchleyCountyAParser() {
    this("SCHLEY COUNTY");
  }
  
  public GASchleyCountyAParser(String defCounty) {
    super(defCounty, "GA");
  }
  
  @Override
  public String getFilter() {
    return "@alert.active911.com";
  }
  
  @Override
  public String getAliasCode() {
    return "GASchleyCounty";
  }
}
