package net.anei.cadpage.parsers.TX;

import net.anei.cadpage.parsers.dispatch.DispatchC03Parser;

public class TXAransasPassParser extends DispatchC03Parser {

  public TXAransasPassParser() {
    super("ARANSAS PASS", "TX");
  }

  @Override
  public String getFilter() {
    return "police@aptx.gov";
  }
}
