package net.anei.cadpage.parsers.GA;

import net.anei.cadpage.parsers.dispatch.DispatchB2Parser;


public class GAHabershamCountyParser extends DispatchB2Parser {

  public GAHabershamCountyParser() {
    super("HABERSHAM E-911:", CITY_LIST, "HABERSHAM COUNTY", "GA");
  }
 
  private static final String[] CITY_LIST = new String[]{
    "ALTO",
    "BALDWIN",
    "CLARKESVILLE",
    "CORNELIA",
    "DEMOREST",
    "MT AIRY",
    "MOUNT AIRY",
    "TALLULAH FALLS",
    "TURNERVILLE",
    };
}
