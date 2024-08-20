package net.anei.cadpage.parsers.OH;

import net.anei.cadpage.parsers.dispatch.DispatchA19Parser;

public class OHClintonCountyBParser extends DispatchA19Parser {

  public OHClintonCountyBParser() {
    super("CLINTON COUNTY", "OH");
  }

  @Override
  public String getFilter() {
    return "FlexRapidNotification@dccnotify.com";
  }

}
