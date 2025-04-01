package net.anei.cadpage.parsers.TX;

import net.anei.cadpage.parsers.dispatch.DispatchA72Parser;

public class TXWiseCountyAParser extends DispatchA72Parser {
  
  public TXWiseCountyAParser() {
    super("WISE COUNTY", "TX");
  }
  
  @Override
  public String getFilter() {
    return "RMS@cityofbridgeport.net";
  }
}
