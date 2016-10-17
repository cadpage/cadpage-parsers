package net.anei.cadpage.parsers.MO;

import net.anei.cadpage.parsers.dispatch.DispatchA33Parser;


public class MOCameronParser extends DispatchA33Parser {
  
  
  public MOCameronParser() {
    super("CAMERON", "MO");
  }
  
  @Override
  public String getFilter() {
    return "DISPATCH@CAMERONMO.COM";
  }
}