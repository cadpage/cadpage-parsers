package net.anei.cadpage.parsers.TX;

import java.util.Properties;

import net.anei.cadpage.parsers.dispatch.DispatchA55Parser;

public class TXLeonCountyBParser extends DispatchA55Parser{

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
    return "ereports@eforcesoftware.com";
  }

  private static final Properties GPS_LOOKUP_TABLE = buildCodeTable(new String[]{
      "306 NEAL RD",                          "+29.302850,-96.046380"

  });
}