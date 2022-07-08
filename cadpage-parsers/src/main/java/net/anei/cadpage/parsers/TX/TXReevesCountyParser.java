package net.anei.cadpage.parsers.TX;

import net.anei.cadpage.parsers.dispatch.DispatchA72Parser;

public class TXReevesCountyParser extends DispatchA72Parser {
  
  public TXReevesCountyParser() {
    super("REEVES COUNTY", "TX");
  }
  
  @Override
  public String getFilter() {
    return "TPS_Service@tylertech.com";
  }

}
