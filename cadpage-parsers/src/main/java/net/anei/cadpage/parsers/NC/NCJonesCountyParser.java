package net.anei.cadpage.parsers.NC;

import net.anei.cadpage.parsers.dispatch.DispatchA19Parser;

public class NCJonesCountyParser extends DispatchA19Parser {

  public NCJonesCountyParser() {
    this("JONES COUNTY", "NC");
  }

  public NCJonesCountyParser(String defCity, String defState) {
    super(defCity, defState);
  }

  @Override
  public String getAliasCode() {
    return "NCJonesCounty";
  }

  @Override
  public int getMapFlags() {
    return MAP_FLG_PREFER_GPS | MAP_FLG_SUPPR_LA;
  }
}
