package net.anei.cadpage.parsers.TX;

import net.anei.cadpage.parsers.dispatch.DispatchA72Parser;

public class TXMilamCountyParser extends DispatchA72Parser {

  public TXMilamCountyParser() {
    super("MILAM COUNTY", "TX");
  }

  @Override
  public String getFilter() {
    return "rockdalepdtx@gmail.com";
  }

}
