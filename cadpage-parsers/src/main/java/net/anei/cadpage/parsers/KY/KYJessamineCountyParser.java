package net.anei.cadpage.parsers.KY;

import net.anei.cadpage.parsers.dispatch.DispatchSPKParser;

public class KYJessamineCountyParser extends DispatchSPKParser {

  public KYJessamineCountyParser() {
    super("JESSAMINE COUNTY", "KY");
  }

  @Override
  public String getFilter() {
    return "dispatch@jessamineco.com";
  }

  @Override
  public int getMapFlags() {
    return MAP_FLG_PREFER_GPS;
  }
}
