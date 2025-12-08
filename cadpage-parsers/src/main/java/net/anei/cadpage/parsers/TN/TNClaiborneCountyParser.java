package net.anei.cadpage.parsers.TN;

import net.anei.cadpage.parsers.dispatch.DispatchC04Parser;

public class TNClaiborneCountyParser extends DispatchC04Parser {

  public TNClaiborneCountyParser() {
    super("CLAIBORNE COUNTY", "TN");
  }

  @Override
  public int getMapFlags() {
    return MAP_FLG_PREFER_GPS;
  }
}
