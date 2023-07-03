package net.anei.cadpage.parsers.TX;

import net.anei.cadpage.parsers.dispatch.DispatchA55Parser;

public class TXCameronCountyAParser extends DispatchA55Parser {
  
  public TXCameronCountyAParser() {
    super("CAMERON COUNTY", "TX");
  }
  
  @Override
  public String getFilter() {
    return "reports@messaging.eforcesoftware.net";
  }

}
