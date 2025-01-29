package net.anei.cadpage.parsers.TX;

import net.anei.cadpage.parsers.dispatch.DispatchH03Parser;

public class TXRandallCountyParser extends DispatchH03Parser {

  public TXRandallCountyParser() {
    super("RANDALL COUNTY", "TX");
  }

  @Override
  public String getFilter() {
    return "RCSO@rc-sheriff.com";
  }

}
