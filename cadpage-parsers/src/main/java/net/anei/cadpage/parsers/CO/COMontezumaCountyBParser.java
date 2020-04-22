package net.anei.cadpage.parsers.CO;

import net.anei.cadpage.parsers.dispatch.DispatchA64Parser;

public class COMontezumaCountyBParser extends DispatchA64Parser {
  
  public  COMontezumaCountyBParser() {
    super("MONTEZUMA COUNTY", "CO");
  }
  
  @Override
  public String getFilter() {
    return "cadalerts@messaging.eforcesoftware.net";
  }
}
