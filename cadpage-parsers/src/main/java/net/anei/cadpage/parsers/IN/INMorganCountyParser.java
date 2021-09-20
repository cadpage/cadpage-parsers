package net.anei.cadpage.parsers.IN;

import java.util.Properties;

import net.anei.cadpage.parsers.dispatch.DispatchA19Parser;

public class INMorganCountyParser extends DispatchA19Parser {

  public INMorganCountyParser() {
    super(CITY_CODES, "MORGAN COUNTY", "IN");
  }

  @Override
  public String getFilter() {
    return "notification@pd.mooresville.in.gov,alerts@interagency.us";
  }

  private static final Properties CITY_CODES = buildCodeTable(new String[] {
      "BRO", "BROOKLYN",
      "MOO", "MOORESVILLE",
      "Moo", "MOORESVILLE",
      "moo", "MOORESVILLE"
  });

}
