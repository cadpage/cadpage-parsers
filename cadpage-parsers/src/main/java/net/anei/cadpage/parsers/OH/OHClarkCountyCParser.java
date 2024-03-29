package net.anei.cadpage.parsers.OH;

import net.anei.cadpage.parsers.dispatch.DispatchA19Parser;

public class OHClarkCountyCParser extends DispatchA19Parser {

  public OHClarkCountyCParser() {
    super("CLARK COUNTY", "OH");
  }

  @Override
  public String getFilter() {
    return "@SPRINGFIELDOHIO.GOV,@alert.active911.com,FlexRapidNotification@dccnotify.com,911Relay@ClarkCountyCommunicationsCe.onmicrosoft.com";
  }

  @Override
  public int getMapFlags() {
    return MAP_FLG_PREFER_GPS;
  }
}
