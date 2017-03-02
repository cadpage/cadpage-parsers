package net.anei.cadpage.parsers.IN;

import net.anei.cadpage.parsers.dispatch.DispatchA68Parser;

public class INDeKalbCountyParser extends DispatchA68Parser {
  
  public INDeKalbCountyParser() {
    super("DEKALB COUNTY", "IN");
  }
  
  @Override
  public String getFilter() {
    return "bhunter@co.dekalb.in.us";
  }
}
