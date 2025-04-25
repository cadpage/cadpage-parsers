package net.anei.cadpage.parsers.KY;

import net.anei.cadpage.parsers.dispatch.DispatchA27Parser;

public class KYLivingstonCountyParser extends DispatchA27Parser {

  public KYLivingstonCountyParser() {
    super(CITY_LIST, "LIVINGSTON COUNTY", "KY");
  }

  @Override
  public String getFilter() {
    return "noreply911@livingstonco.ky.gov,livingstoncoky@cissystem.com";
  }

  private static final String[] CITY_LIST = new String[] {

      // Cities
      "CARRSVILLE",
      "GRAND RIVERS",
      "SALEM",
      "SMITHLAND",

      // Census-designated places
      "BURNA",
      "LEDBETTER",

      // Other unincorporated communities
      "HAMPTON",
      "JOY",
      "LOLA",
      "IUKA",
      "TILINE"
  };

}
