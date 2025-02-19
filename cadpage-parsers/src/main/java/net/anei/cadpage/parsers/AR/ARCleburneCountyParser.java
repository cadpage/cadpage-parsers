package net.anei.cadpage.parsers.AR;

import net.anei.cadpage.parsers.dispatch.DispatchA99Parser;

public class ARCleburneCountyParser extends DispatchA99Parser {

  public ARCleburneCountyParser() {
    super("CLEBURNE COUNTY", "AR");
  }

  @Override
  public int getMapFlags() {
    return MAP_FLG_PREFER_GPS;
  }
}
