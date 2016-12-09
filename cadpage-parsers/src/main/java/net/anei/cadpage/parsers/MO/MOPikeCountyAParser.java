package net.anei.cadpage.parsers.MO;

import net.anei.cadpage.parsers.dispatch.DispatchA33Parser;


public class MOPikeCountyAParser extends DispatchA33Parser {
  
  
  public MOPikeCountyAParser() {
    super("PIKE COUNTY", "MO");
  }
  
  @Override
  public String getFilter() {
    return "louisianapd.dispatch@gmail.com";
  }
}