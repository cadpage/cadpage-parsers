package net.anei.cadpage.parsers.KY;

import net.anei.cadpage.parsers.dispatch.DispatchSPKParser;

public class KYRockCastleCountyBParser extends DispatchSPKParser {

  public KYRockCastleCountyBParser() {
    super("ROCKCASTLE COUNTY", "KY");
  }

  @Override
  public String getFilter() {
    return "Rock911@windstream.net";
  }

}
