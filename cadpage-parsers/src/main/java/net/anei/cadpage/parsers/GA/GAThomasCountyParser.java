package net.anei.cadpage.parsers.GA;

import net.anei.cadpage.parsers.dispatch.DispatchA57Parser;

public class GAThomasCountyParser extends DispatchA57Parser {
  
  public GAThomasCountyParser() {
    super("THOMAS COUNTY", "GA");
  }
  
  @Override
  public String getFilter() {
    return "cadpaging@thomascountyesa.com,dispatch@thomascountyesa.com";
  }

}
