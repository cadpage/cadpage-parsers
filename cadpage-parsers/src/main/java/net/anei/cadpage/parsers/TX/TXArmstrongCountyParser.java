package net.anei.cadpage.parsers.TX;

import net.anei.cadpage.parsers.dispatch.DispatchA55Parser;

public class TXArmstrongCountyParser extends DispatchA55Parser {
  
  public TXArmstrongCountyParser() {
    super("ARMSTRONG COUNTY", "TX");
  }
  
  @Override
  public String getFilter() {
    return "ereports@eforcesoftware.com";
  }

}
