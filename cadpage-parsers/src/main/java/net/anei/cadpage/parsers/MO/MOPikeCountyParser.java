package net.anei.cadpage.parsers.MO;

import net.anei.cadpage.parsers.dispatch.DispatchA33Parser;


public class MOPikeCountyParser extends DispatchA33Parser {
  
  
  public MOPikeCountyParser() {
    super("PIKE COUNTY", "MO");
  }
  
  @Override
  public String getFilter() {
    return "louisianapd.dispatch@gmail.com";
  }
}