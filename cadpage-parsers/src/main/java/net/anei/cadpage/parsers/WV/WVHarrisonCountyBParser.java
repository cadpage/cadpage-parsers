package net.anei.cadpage.parsers.WV;

import net.anei.cadpage.parsers.dispatch.DispatchSPKParser;

public class WVHarrisonCountyBParser extends DispatchSPKParser {

  public WVHarrisonCountyBParser() {
    this("HARRISON COUNTY", "WV");
  }

  protected WVHarrisonCountyBParser(String defCity, String defState) {
    super(defCity, defState);
  }

  @Override
  public String getAliasCode() {
    return "WVHarrisonCountyB";
  }

  @Override
  public String getFilter() {
    return "cad@centrale911.com";
  }
}
