package net.anei.cadpage.parsers.TX;

import net.anei.cadpage.parsers.dispatch.DispatchA72Parser;

public class TXGraysonCountyBParser extends DispatchA72Parser {
  
  public TXGraysonCountyBParser() {
    super("GRAYSON COUNTY", "TX");
  }
  
  @Override
  public String getFilter() {
    return "active911@co.grayson.tx.us";
  }
}
