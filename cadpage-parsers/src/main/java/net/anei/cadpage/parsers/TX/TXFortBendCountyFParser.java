package net.anei.cadpage.parsers.TX;

import net.anei.cadpage.parsers.dispatch.DispatchH03Parser;

public class TXFortBendCountyFParser extends DispatchH03Parser {

  public TXFortBendCountyFParser() {
    super("FORT BEND COUNTY", "TX");
  }

  @Override
  public String getFilter() {
    return "SLPD@leaguecity.com";
  }

}
