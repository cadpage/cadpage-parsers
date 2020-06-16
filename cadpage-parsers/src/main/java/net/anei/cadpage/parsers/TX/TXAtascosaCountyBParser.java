package net.anei.cadpage.parsers.TX;

import net.anei.cadpage.parsers.dispatch.DispatchA77Parser;

public class TXAtascosaCountyBParser extends DispatchA77Parser {
  
  public TXAtascosaCountyBParser() {
    super("Rapid Notification", TXAtascosaCountyParser.CITY_CODES, "ATASCOSA COUNTY", "TX");
  }
  
  @Override
  public String getFilter() {
    return "so-noreply@acso-tx.org";
  }
}
