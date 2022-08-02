package net.anei.cadpage.parsers.IA;

import net.anei.cadpage.parsers.dispatch.DispatchA47Parser;

public class IAIdaCountyParser extends DispatchA47Parser {

  public IAIdaCountyParser() {
    super(CITY_LIST, "IDA COUNTY", "IA", null);
  }

  @Override
  public String getFilter() {
    return "swmail@idacountysheriff.uss";
  }


  private static final String[] CITY_LIST = new String[] {
      // Cities
      "ARTHUR",
      "BATTLE CREEK",
      "GALVA",
      "HOLSTEIN",
      "IDA GROVE",

      // Townships
      "BATTLE",
      "BLAINE",
      "CORWIN",
      "DOUGLAS",
      "GALVA",
      "GARFIELD",
      "GRANT",
      "GRIGGS",
      "HAYES",
      "LOGAN",
      "MAPLE",
      "SILVER CREEK"
  };
}
