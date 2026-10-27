package net.anei.cadpage.parsers.KS;

import net.anei.cadpage.parsers.dispatch.DispatchA19Parser;
/**
 * Cowley County, KS
 */
public class KSCowleyCountyParser extends DispatchA19Parser {

  public KSCowleyCountyParser() {
    super("COWLEY COUNTY", "KS");
  }

  @Override
  public int getMapFlags() {
    return MAP_FLG_SUPPR_LA | MAP_FLG_PREFER_GPS;
  }

  @Override
  public String adjustMapCity(String city) {
    if (city.equalsIgnoreCase("COUNTY LIMIT")) return "";
    return city;
  }
}
