package net.anei.cadpage.parsers.CA;

import net.anei.cadpage.parsers.dispatch.DispatchA19Parser;

public class CATulareCountyCParser extends DispatchA19Parser {
  
  public CATulareCountyCParser() {
    super("TULARE COUNTY", "CA");
  }
  
  @Override
  public String getFilter() {
    return "FlexRapidNotification@dccnotify.com";
  }
}
