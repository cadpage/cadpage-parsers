package net.anei.cadpage.parsers.TX;

import net.anei.cadpage.parsers.dispatch.DispatchA82Parser;

public class TXGraysonCountyCParser extends DispatchA82Parser {

  public TXGraysonCountyCParser() {
    super("GRAYSON COUNTY", "TX");
  }

  @Override
  public String getFilter() {
    return "notifications@athenaics.com";
  }
}
