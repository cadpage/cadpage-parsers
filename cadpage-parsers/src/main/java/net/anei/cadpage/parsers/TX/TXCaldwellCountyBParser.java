package net.anei.cadpage.parsers.TX;

import net.anei.cadpage.parsers.dispatch.DispatchA72Parser;

public class TXCaldwellCountyBParser extends DispatchA72Parser {

  public TXCaldwellCountyBParser() {
    super("CALDWELL COUNTY", "TX");
  }

  @Override
  public String getFilter() {
    return "rms@cityofluling.net";
  }

}
