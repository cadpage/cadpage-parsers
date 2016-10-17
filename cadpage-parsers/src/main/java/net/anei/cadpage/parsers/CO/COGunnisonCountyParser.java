package net.anei.cadpage.parsers.CO;

import net.anei.cadpage.parsers.dispatch.DispatchA33Parser;

public class COGunnisonCountyParser extends DispatchA33Parser {

  public COGunnisonCountyParser() {
    this("GUNNISON COUNTY");
  }

  public COGunnisonCountyParser(String county) {
    super(county, "CO");
  }
  
  @Override
  public String getAliasCode() {
    return "COGunnisonCounty";
  }

  @Override
  public String getFilter() {
    return "DISPATCHCENTER@CITYOFGUNNISON-CO.GOV";
  }
}
