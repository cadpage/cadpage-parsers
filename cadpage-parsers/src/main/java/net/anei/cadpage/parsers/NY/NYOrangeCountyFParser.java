package net.anei.cadpage.parsers.NY;

import net.anei.cadpage.parsers.dispatch.DispatchC02Parser;

public class NYOrangeCountyFParser extends DispatchC02Parser {
  
  public NYOrangeCountyFParser() {
    super("ORANGE COUNTY", "NY");
  }
  
  @Override
  public String getFilter() {
    return "kjactive911@gmail.com";
  }

}
