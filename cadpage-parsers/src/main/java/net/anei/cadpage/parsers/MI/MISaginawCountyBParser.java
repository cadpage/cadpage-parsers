package net.anei.cadpage.parsers.MI;

import java.util.Properties;

import net.anei.cadpage.parsers.dispatch.DispatchC01Parser;

public class MISaginawCountyBParser extends DispatchC01Parser {

  public MISaginawCountyBParser() {
    super(CITY_CODES, "SAGINAW COUNTY", "MI");
  }

  @Override
  public String getFilter() {
    return "SaginawAlerts@saginawcounty.com";
  }

  private static final Properties CITY_CODES = buildCodeTable(new String[]{
      "01", "BURT",
      "06", "BRIDGEPORT TWP",
      "20", "TITTABAWASSEE TWP",
      "22", "SPAULDING TWP",
      "25", "FREELAND",
      "26", "FREELAND"
  });
}