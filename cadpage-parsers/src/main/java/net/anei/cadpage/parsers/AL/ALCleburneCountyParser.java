package net.anei.cadpage.parsers.AL;

import net.anei.cadpage.parsers.dispatch.DispatchA74Parser;


public class ALCleburneCountyParser extends DispatchA74Parser {
  public ALCleburneCountyParser() {
    super("CLEBURNE COUNTY", "AL");
  }

  @Override
  public String getFilter() {
    return "Dispatch@CleburneCo911.info";
  }
  
  @Override
  public int getMapFlags() {
    return MAP_FLG_PREFER_GPS;
  }
}
