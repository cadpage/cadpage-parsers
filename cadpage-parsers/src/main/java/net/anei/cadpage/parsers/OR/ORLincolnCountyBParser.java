package net.anei.cadpage.parsers.OR;

import java.util.Properties;

import net.anei.cadpage.parsers.dispatch.DispatchA85Parser;

public class ORLincolnCountyBParser extends DispatchA85Parser {

  public ORLincolnCountyBParser() {
    super(CITY_CODES, "LINCOLN COUNTY", "OR");
  }

  @Override
  public String getFilter() {
    return "no-reply@cityoftoledo.org";
  }

  private static final Properties CITY_CODES = buildCodeTable(new String[] {
      "TOL", "TOLEDO"
  });

}
