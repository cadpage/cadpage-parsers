package net.anei.cadpage.parsers.TN;

import net.anei.cadpage.parsers.dispatch.DispatchC04Parser;

public class TNMaconCountyBParser extends DispatchC04Parser {

  public TNMaconCountyBParser() {
    super("MACON COUNTY", "TN");
  }

  @Override
  public String getFilter() {
    return "NOREPLY@TNLAFAYETTEPD.MMMICRO.COM";
  }

  @Override
  public int getMapFlags() {
    return MAP_FLG_PREFER_GPS;
  }
}
