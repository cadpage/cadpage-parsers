package net.anei.cadpage.parsers.LA;

import net.anei.cadpage.parsers.dispatch.DispatchA55Parser;

public class LACameronParishParser extends DispatchA55Parser {
  
  public LACameronParishParser() {
    super("CAMERON PARISH", "LA");
  }
  
  @Override
  public String getFilter() {
    return "ereports@eforcesoftware.com";
  }

}
