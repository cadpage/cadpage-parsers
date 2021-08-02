package net.anei.cadpage.parsers.OR;

import java.util.Properties;

import net.anei.cadpage.parsers.dispatch.DispatchA85Parser;

public class ORLaneCountyDParser extends DispatchA85Parser {

  public ORLaneCountyDParser() {
    super(CITY_CODES, "LANE COUNTY", "OR");
  }

  @Override
  public String getFilter() {
    return "do.not.reply@ci.florence.or.us";
  }

  private static final Properties CITY_CODES = buildCodeTable(new String[] {
      "233", "FLORENCE",
      "DEA", "DEADWOOD",
      "FLO", "FLORENCE",
      "MAP", "MAPLETON",
      "PO0", "FLORENCE",
      "WES", "WESTLAKE"
  });
}
