
package net.anei.cadpage.parsers.TX;

import net.anei.cadpage.parsers.dispatch.DispatchA78Parser;

public class TXRuskCountyAParser extends DispatchA78Parser {

  public TXRuskCountyAParser() {
    super("RUSK COUNTY", "TX");
  }

  @Override
  public String getFilter() {
    return "donotreply@RUSKSOalerts.com";
  }
}
