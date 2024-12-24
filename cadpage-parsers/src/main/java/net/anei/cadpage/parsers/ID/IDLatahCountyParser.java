package net.anei.cadpage.parsers.ID;

import net.anei.cadpage.parsers.dispatch.DispatchA19Parser;

public class IDLatahCountyParser extends DispatchA19Parser {

  public IDLatahCountyParser() {
    super("LATAH COUNTY", "ID");
  }

  @Override
  public String getFilter() {
    return "FlexRapidNotification@dccnotify.com";
  }

}
