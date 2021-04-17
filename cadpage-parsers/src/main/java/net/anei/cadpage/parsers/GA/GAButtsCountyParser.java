package net.anei.cadpage.parsers.GA;

import java.util.Properties;

import net.anei.cadpage.parsers.dispatch.DispatchA19Parser;
/**
 * Butts County, GA
 */
public class GAButtsCountyParser extends DispatchA19Parser {

  public GAButtsCountyParser() {
    super(CITY_CODES, "BUTTS COUNTY", "GA");
  }

  @Override
  public String getFilter() {
    return "active911@buttscounty.org";
  }

  private static final Properties CITY_CODES = buildCodeTable(new String[] {
      "FLO", "Flovilla",
      "JAK", "Jackson",
      "JEN", "Jenkinsburg"
  });
}
