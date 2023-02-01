package net.anei.cadpage.parsers.OH;

import net.anei.cadpage.parsers.dispatch.DispatchA19Parser;

public class OHLawrenceCountyBParser extends DispatchA19Parser {

  public OHLawrenceCountyBParser() {
    super("LAWRENCE COUNTY", "OH");
  }

  @Override
  public String getFilter() {
    return "FlexRapidNotification@dccnotify.com";
  }

}
