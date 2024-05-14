package net.anei.cadpage.parsers.WI;

import net.anei.cadpage.parsers.dispatch.DispatchH03Parser;

public class WIWinnebagoCountyCParser extends DispatchH03Parser {

  public WIWinnebagoCountyCParser() {
    super("WINNEBAGO COUNTY", "WI");
  }

  @Override
  public String getFilter() {
    return ".WI@co.winnebago.wi.us";
  }
}
