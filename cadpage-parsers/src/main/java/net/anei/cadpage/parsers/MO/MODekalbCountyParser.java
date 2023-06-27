package net.anei.cadpage.parsers.MO;

import net.anei.cadpage.parsers.dispatch.DispatchA33Parser;
import net.anei.cadpage.parsers.dispatch.DispatchBCParser;

public class MODekalbCountyParser extends DispatchBCParser {

  public MODekalbCountyParser() {
    super(CITY_LIST, "DEKALB COUNTY", "MO", DispatchA33Parser.A33_X_ADDR_EXT);
  }

  @Override
  public String getFilter() {
    return "DEKALBCODISPATCH@OMNIGO.COM,LACLEDECOES@OMNIGO.COM";
  }

  private static final String[] CITY_LIST = new String[] {
      "ALTA VISTA",
      "AMITY",
      "CAMERON",
      "CLARKSDALE",
      "FAIRPORT",
      "FORDHAM",
      "MAYSVILLE",
      "OAK",
      "ORCHID",
      "OSBORN",
      "SANTA ROSA",
      "STEWARTSVILLE",
      "UNION STAR",
      "WEATHERBY",
      "WOOD"
  };

}
