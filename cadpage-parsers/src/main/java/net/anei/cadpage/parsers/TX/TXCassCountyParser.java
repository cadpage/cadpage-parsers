package net.anei.cadpage.parsers.TX;

import net.anei.cadpage.parsers.dispatch.DispatchA78Parser;

public class TXCassCountyParser extends DispatchA78Parser {

  public TXCassCountyParser() {
    super("CASS COUNTY", "TX");
  }

  @Override
  public String getFilter() {
    return "donotreply@CASSCOUNTYSHERIFFSOFFICEalerts.com";
  }
}
