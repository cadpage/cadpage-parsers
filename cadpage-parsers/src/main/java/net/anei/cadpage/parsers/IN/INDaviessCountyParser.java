package net.anei.cadpage.parsers.IN;

import net.anei.cadpage.parsers.dispatch.DispatchA19Parser;

public class INDaviessCountyParser extends DispatchA19Parser {
  
  public INDaviessCountyParser() {
    super("DAVIESS COUNTY", "IN");
  }
  
  @Override
  public String getFilter() {
    return "ripnrun@dcsheriff.com";
  }
}
