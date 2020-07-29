package net.anei.cadpage.parsers.TX;

import net.anei.cadpage.parsers.dispatch.DispatchA70Parser;

public class TXHaysCountyBParser extends DispatchA70Parser {

  public TXHaysCountyBParser() {
    super("HAYS COUNTY", "TX");
  }

  @Override
  public String getFilter() {
    return "noreply@sanmarcostx.gov,noreply@cisusa.org,cisalerts@sanmarcostx.gov";
  }
}
