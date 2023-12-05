package net.anei.cadpage.parsers.TX;

import net.anei.cadpage.parsers.dispatch.DispatchA41Parser;

public class TXFortBendCountyAParser extends DispatchA41Parser {

  public TXFortBendCountyAParser() {
    super(TXFortBendCountyParser.CITY_CODES, "FORT BEND COUNTY", "TX", "[A-Z]{2,3}\\d?|EOUT");
  }

  @Override
  public String getFilter() {
    return "fbcsotibsys@co.fort-bend.tx.us";
  }
}
