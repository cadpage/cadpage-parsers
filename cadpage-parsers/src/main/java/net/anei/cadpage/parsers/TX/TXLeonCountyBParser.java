package net.anei.cadpage.parsers.TX;

import java.util.Properties;

import net.anei.cadpage.parsers.dispatch.DispatchA64Parser;

public class TXLeonCountyBParser extends DispatchA64Parser{

  public TXLeonCountyBParser() {
    this("LEON COUNTY", "TX");
  }

  protected TXLeonCountyBParser(String defCity, String defState) {
    super(defCity, defState);
    setupGpsLookupTable(GPS_LOOKUP_TABLE);
  }
  
  public String getAliasCode() {
    return "TXLeonCountyB";
  }

  public String getFilter() {
    return "cadalerts@eforcesoftware.com,cadalerts@messaging.eforcesoftware.net,cadalerts@messaging.eforcesoftware.net";
  }
  
  private static final Properties GPS_LOOKUP_TABLE = buildCodeTable(new String[]{
      "306 NEAL RD",                          "+29.302850,-96.046380"
      
  });
}