package net.anei.cadpage.parsers.TX;

import net.anei.cadpage.parsers.dispatch.DispatchA72Parser;

public class TXUvaldeCountyParser extends DispatchA72Parser {

  public TXUvaldeCountyParser() {
    super("UVALDE COUNTY", "TX");
  }

  @Override
  public String getFilter() {
    return "active911.uvaldetx@gmail.com";
  }
}
