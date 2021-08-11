package net.anei.cadpage.parsers.TX;

import net.anei.cadpage.parsers.dispatch.DispatchA82Parser;

public class TXDentonCountyCParser extends DispatchA82Parser {

  public TXDentonCountyCParser() {
    super("DENTON COUNTY", "TX");
  }

  @Override
  public String getFilter() {
    return "ICS-CAD@highlandvillage.org";
  }

}
