package net.anei.cadpage.parsers.MO;

import java.util.Properties;

import net.anei.cadpage.parsers.dispatch.DispatchH02Parser;

public class MOJohnsonCountyBParser extends DispatchH02Parser {

  public MOJohnsonCountyBParser() {
    super(CITY_CODES, "JOHNSON COUNTY", "MO");
  }

  @Override
  public String getFilter() {
    return "CAD@JOCO911.ORG";
  }

  private static final Properties CITY_CODES = buildCodeTable(new String[] {
      "BATE", "BATES CITY",
      "CENT", "CENTERVILLE",
      "GARD", "GARDEN CITY",
      "HOLD", "HOLDEN",
      "KING", "KINGSVILLE",
      "KNOB", "KNOB NOSTER",
      "ODES", "ODESSA",
      "WARR", "WARRENSBURG"
  });

}
