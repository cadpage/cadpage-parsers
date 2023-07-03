package net.anei.cadpage.parsers.TX;

import net.anei.cadpage.parsers.dispatch.DispatchA72Parser;

public class TXCameronCountyBParser extends DispatchA72Parser {

  public TXCameronCountyBParser() {
    super("CAMERON COUNTY", "TX");
  }

  @Override
  public String getFilter() {
    return "e911@myspi.org";
  }
}
