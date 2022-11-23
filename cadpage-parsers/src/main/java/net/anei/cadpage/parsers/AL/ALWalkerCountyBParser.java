package net.anei.cadpage.parsers.AL;

import net.anei.cadpage.parsers.dispatch.DispatchA19Parser;

public class ALWalkerCountyBParser extends DispatchA19Parser {

  public ALWalkerCountyBParser() {
    super("WALKER COUNTY", "AL");
  }

  @Override
  public String getFilter() {
    return "FlexRapidNotification@dccnotify.com";
  }

}
