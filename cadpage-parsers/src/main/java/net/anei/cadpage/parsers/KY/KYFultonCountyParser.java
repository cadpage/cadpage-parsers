package net.anei.cadpage.parsers.KY;

import net.anei.cadpage.parsers.dispatch.DispatchA27Parser;


public class KYFultonCountyParser extends DispatchA27Parser {

  public KYFultonCountyParser() {
    super(CITY_LIST, "FULTON COUNTY", "KY");
  }

  @Override
  public String getFilter() {
    return "fultoncountydispatch@outlook.com,active911@outlook.com";
  }

  private static final String[] CITY_LIST = new String[]{
    "FULTON",
    "HICKMAN"
  };
}
