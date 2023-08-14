package net.anei.cadpage.parsers.GA;

import java.util.Properties;

import net.anei.cadpage.parsers.dispatch.DispatchH03Parser;

public class GACobbCountyParser extends DispatchH03Parser {

  public GACobbCountyParser() {
    super(CITY_CODES, "COBB COUNTY", "GA");
  }

  @Override
  public String getFilter() {
    return "CO@Agency.Cobb.premierone.local";
  }

  private static final Properties CITY_CODES = buildCodeTable(new String[] {
      "CC", "COBB COUNTY",
      "MA", "MARIETTA"
  });

}
