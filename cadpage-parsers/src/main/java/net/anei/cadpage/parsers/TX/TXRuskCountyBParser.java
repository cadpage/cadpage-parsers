
package net.anei.cadpage.parsers.TX;

import net.anei.cadpage.parsers.dispatch.DispatchA53Parser;


public class TXRuskCountyBParser extends DispatchA53Parser {

  public TXRuskCountyBParser() {
    super("RUSK COUNTY", "TX");
  }
  
  @Override
  public String getFilter() {
    return "hpdactive911@ruskcountyoem.org";
  }
}
