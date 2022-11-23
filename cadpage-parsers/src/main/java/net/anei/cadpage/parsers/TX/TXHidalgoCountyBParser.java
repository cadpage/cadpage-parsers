package net.anei.cadpage.parsers.TX;

import net.anei.cadpage.parsers.dispatch.DispatchA57Parser;

public class TXHidalgoCountyBParser extends DispatchA57Parser {

  public TXHidalgoCountyBParser() {
    super("HIDALGO COUNTY", "TX");
  }

  @Override
  public String getFilter() {
    return "dispatch@weslacotx.gov";
  }
}
