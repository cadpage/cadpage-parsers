package net.anei.cadpage.parsers.IN;

import net.anei.cadpage.parsers.dispatch.DispatchA68Parser;

public class INDeKalbCountyAParser extends DispatchA68Parser {
  
  public INDeKalbCountyAParser() {
    super("DEKALB COUNTY", "IN");
  }
  
  @Override
  public String getFilter() {
    return "bhunter@co.dekalb.in.us";
  }
}
