package net.anei.cadpage.parsers.NE;

import net.anei.cadpage.parsers.dispatch.DispatchA19Parser;


public class NEGrandIslandParser extends DispatchA19Parser {

  public NEGrandIslandParser() {
    super("GRAND ISLAND", "NE");
  }

  @Override
  public String getFilter() {
    return "gipdadmin@gipolice.org,gipdadmin@ginepolice.gov";
  }

}
