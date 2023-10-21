package net.anei.cadpage.parsers.MT;

import net.anei.cadpage.parsers.dispatch.DispatchA55Parser;

public class MTGallatinCountyBParser extends DispatchA55Parser {
  
  public MTGallatinCountyBParser() {
    super("GALLATIN COUNTY", "MT");
  }
  
  @Override
  public String getFilter() {
    return "reports@messaging.eforcesoftware.net";
  }
}
