package net.anei.cadpage.parsers.IA;

import net.anei.cadpage.parsers.dispatch.DispatchA47Parser;

public class IALucasCountyParser extends DispatchA47Parser {

  public IALucasCountyParser() {
    super("from dispatch", CITY_LIST, "LUCAS COUNTY", "IA", null);
  }

  @Override
  public String getFilter() {
    return "swmail@lucascosheriff.org";
  }

  private static final String[] CITY_LIST = new String[] {

      // Cities
      "CHARITON",
      "DERBY",
      "LUCAS",
      "RUSSELL",
      "WILLIAMSON",

      // Townships
      "BENTON",
      "CEDAR",
      "ENGLISH",
      "JACKSON",
      "LIBERTY",
      "LINCOLN",
      "OTTER CREEK",
      "PLEASANT",
      "UNION",
      "WARREN",
      "WASHINGTON",
      "WHITEBREAST",

      // Unincorporated areas
      "NORWOOD"
  };

}
