package net.anei.cadpage.parsers.TX;

import net.anei.cadpage.parsers.dispatch.DispatchA55Parser;

public class TXColoradoCountyParser extends DispatchA55Parser {

  public TXColoradoCountyParser() {
    super("COLORADO COUNTY", "TX");
  }

  @Override
  public String getFilter() {
    return "reports@messaging.eforcesoftware.net";
  }

}
