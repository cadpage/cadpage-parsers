package net.anei.cadpage.parsers.TX;

import net.anei.cadpage.parsers.dispatch.DispatchA82Parser;

public class TXGraysonCountyAParser extends DispatchA82Parser {

  public TXGraysonCountyAParser() {
    super("GRAYSON COUNTY", "TX");
  }

  @Override
  public String getFilter() {
    return "ICSAlerts@cityofdenison.com";
  }

}
