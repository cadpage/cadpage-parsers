package net.anei.cadpage.parsers.OR;

import java.util.Properties;

import net.anei.cadpage.parsers.dispatch.DispatchH02Parser;

public class ORLaneCountyBParser extends DispatchH02Parser {
  
  public ORLaneCountyBParser() {
    super(CITY_CODES, "LANE COUNTY", "OR");
  }
  
  @Override
  public String getFilter() {
    return "DONOTREPLY@SUNGARDREPORTS.NET";
  }
  
  private static final Properties CITY_CODES = buildCodeTable(new String[]{
      "CHE", "CHESHIRE",
      "JUN", "JUNCTION CITY"
  });
}
