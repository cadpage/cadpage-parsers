package net.anei.cadpage.parsers.NJ;

import net.anei.cadpage.parsers.dispatch.DispatchA19Parser;

public class NJMonmouthCountyGParser extends DispatchA19Parser {

  public NJMonmouthCountyGParser() {
    super("MONMOUTH COUNTY", "NJ");
  }

  @Override
  public String getFilter() {
    return "FlexRapidNotification@dccnotify.com";
  }
}
