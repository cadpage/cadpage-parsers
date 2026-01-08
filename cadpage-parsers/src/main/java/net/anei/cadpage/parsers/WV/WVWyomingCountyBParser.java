package net.anei.cadpage.parsers.WV;

import net.anei.cadpage.parsers.dispatch.DispatchA19Parser;

public class WVWyomingCountyBParser extends DispatchA19Parser {

  public WVWyomingCountyBParser() {
    super("WYOMING COUNTY", "WV");
  }

  @Override
  public String getFilter() {
    return "FlexRapidNotification@dccnotify.com";
  }

}
