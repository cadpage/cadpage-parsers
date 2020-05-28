package net.anei.cadpage.parsers.MO;

import net.anei.cadpage.parsers.dispatch.DispatchA25Parser;

public class MODunklinCountyParser extends DispatchA25Parser {

  public MODunklinCountyParser() {
    super("DUNKLIN COUNTY", "MO");
  }
  
  @Override
  public String getFilter() {
    return "cadalerts@dunklincomo.org";
  }
}
