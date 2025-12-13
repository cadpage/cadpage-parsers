package net.anei.cadpage.parsers.TX;

import net.anei.cadpage.parsers.dispatch.DispatchC03Parser;

public class TXSanPatricoCountyParser extends DispatchC03Parser {
  
  public TXSanPatricoCountyParser() {
    super("SAN PATRICO COUNTY", "TX");
  }
  
  @Override
  public String getFilter() {
    return "police@aptx.gov";
  }
}
