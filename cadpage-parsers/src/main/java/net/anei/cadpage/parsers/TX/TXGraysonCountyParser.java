package net.anei.cadpage.parsers.TX;

import net.anei.cadpage.parsers.dispatch.DispatchA82Parser;

public class TXGraysonCountyParser extends DispatchA82Parser {

  public TXGraysonCountyParser() {
    super("GRAYSON COUNTY", "TX");
  }

  @Override
  public String getFilter() {
    return "ICSAlerts@cityofdenison.com";
  }

}
