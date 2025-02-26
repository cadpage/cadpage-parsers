package net.anei.cadpage.parsers.ID;

import net.anei.cadpage.parsers.dispatch.DispatchA19Parser;

public class IDPowerCountyParser extends DispatchA19Parser {

  public IDPowerCountyParser() {
    super("POWER COUNTY", "ID");
  }

  @Override
  public String getFilter() {
    return "FlexRapidNotification@dccnotify.com";
  }

}
