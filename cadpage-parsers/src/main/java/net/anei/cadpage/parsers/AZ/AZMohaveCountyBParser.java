package net.anei.cadpage.parsers.AZ;

import net.anei.cadpage.parsers.dispatch.DispatchA94Parser;

public class AZMohaveCountyBParser extends DispatchA94Parser {

  public AZMohaveCountyBParser() {
    super("MOHAVE COUNTY", "AZ");
  }

  @Override
  public int getMapFlags() {
    return MAP_FLG_PREFER_GPS | MAP_FLG_SUPPR_LA;
  }
}
