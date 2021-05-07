package net.anei.cadpage.parsers.KY;

import net.anei.cadpage.parsers.dispatch.DispatchA27Parser;

public class KYBoydCountyCParser extends DispatchA27Parser {

  public KYBoydCountyCParser() {
    super("BOYD COUNTY", "KY");
  }

  @Override
  public String getFilter() {
    return "noreply@cisusa.org,cadexport@boydcounty911.com,cadexport@windstream.net";
  }

  @Override
  public int getMapFlags() {
    return MAP_FLG_PREFER_GPS;
  }
}
