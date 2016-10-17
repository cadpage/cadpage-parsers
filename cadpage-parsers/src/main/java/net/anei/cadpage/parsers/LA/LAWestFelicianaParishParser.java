package net.anei.cadpage.parsers.LA;

import net.anei.cadpage.parsers.dispatch.DispatchB2Parser;

public class LAWestFelicianaParishParser extends DispatchB2Parser {
		
  public LAWestFelicianaParishParser() {
    super("911_CENTER:", CITY_LIST, "WEST FELICIANA PARISH", "LA");
  }

  private static final String[] CITY_LIST = new String[] {
    "ANGOLA",
    "BAINS",
    "FRANCISVILLE",
    "TUNICA",
    "WAKEFIELD"
  };

}
