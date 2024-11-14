package net.anei.cadpage.parsers.TX;

import net.anei.cadpage.parsers.dispatch.DispatchA55Parser;

public class TXHendersonCountyAParser extends DispatchA55Parser {

  public TXHendersonCountyAParser() {
    super("HENDERSON COUNTY", "TX");
  }

  @Override
  public String getFilter() {
    return "reports@messaging.eforcesoftware.net";
  }

}
