package net.anei.cadpage.parsers.OR;

import java.util.Properties;

import net.anei.cadpage.parsers.dispatch.DispatchA85Parser;

public class ORGrantCountyParser extends DispatchA85Parser {

  public ORGrantCountyParser() {
    super(CITY_CODES, "GRANT COUNTY", "OR");
  }

  @Override
  public String getFilter() {
    return "DispatchText@grantcounty-or.gov";
  }

  private static final Properties CITY_CODES = buildCodeTable(new String[] {
      "DAY", "DAYVILLE",
      "JOH", "JOHN DAY",
      "MT.", "MT VERNON",
      "PRA", "PRAIRIE CITY",
      "SEN", "SENECA"
  });
}
