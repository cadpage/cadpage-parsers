package net.anei.cadpage.parsers.FL;

import net.anei.cadpage.parsers.dispatch.DispatchA90Parser;

public class FLOkaloosaCountyBParser extends DispatchA90Parser {

  public FLOkaloosaCountyBParser() {
    super("OKALOOSA COUNTY", "FL");
  }

  @Override
  public int getMapFlags() {
    return MAP_FLG_PREFER_GPS | MAP_FLG_SUPPR_LA;
  }
}
