package net.anei.cadpage.parsers.CA;

import net.anei.cadpage.parsers.dispatch.DispatchA19Parser;

public class CATrinityCountyParser extends DispatchA19Parser {

  public CATrinityCountyParser() {
    super("TRINITY COUNTY", "CA");
  }

  @Override
  public String getFilter() {
    return "rapid_notifications@trinitycounty.org,noreply@trinitycounty.org";
  }

  @Override
  public int getMapFlags() {
    return MAP_FLG_PREFER_GPS | MAP_FLG_SUPPR_LA;
  }
}
