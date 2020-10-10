package net.anei.cadpage.parsers.MO;

import net.anei.cadpage.parsers.dispatch.DispatchA81Parser;

public class MOWebsterCountyBParser extends DispatchA81Parser {
  
  public MOWebsterCountyBParser() {
    super("WEBSTER COUNTY", "MO");
  }
  
  @Override
  public String getFilter() {
    return "cadalerts@webster911.org";
  }
}
