package net.anei.cadpage.parsers.TX;

import net.anei.cadpage.parsers.dispatch.DispatchBCParser;

public class TXDawsonCountyParser extends DispatchBCParser {

  public TXDawsonCountyParser() {
    super("DAWSON COUNTY", "TX");
  }

  @Override
  public String getFilter() {
    return "noreply@omnigo.com";
  }

}
