package net.anei.cadpage.parsers.CA;

import net.anei.cadpage.parsers.dispatch.DispatchA20Parser;

public class CASuisunCityAParser extends DispatchA20Parser {

  public CASuisunCityAParser() {
    super("SUISUN CITY", "CA");
  }
  
  @Override
  public String getFilter() {
    return "rims2text@suisun.com";
  }
}
