package net.anei.cadpage.parsers.TX;

import net.anei.cadpage.parsers.dispatch.DispatchA64Parser;

public class TXLeonCountyBParser extends DispatchA64Parser{

  public TXLeonCountyBParser() {
    this("LEON COUNTY", "TX");
  }

  protected TXLeonCountyBParser(String defCity, String defState) {
    super(defCity, defState);
  }
  
  public String getAliasCode() {
    return "TXLeonCountyB";
  }

  public String getFilter() {
    return "cadalerts@eforcesoftware.com,cadalerts@messaging.eforcesoftware.net,cadalerts@messaging.eforcesoftware.net";
  }
}