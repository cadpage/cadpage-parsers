package net.anei.cadpage.parsers.KS;

import net.anei.cadpage.parsers.dispatch.DispatchSPKParser;

/**
 * McPherson County, KS
 */

public class KSMcPhersonCountyParser extends DispatchSPKParser {

  public KSMcPhersonCountyParser() {
    super("MCPHERSON COUNTY", "KS");
  }

  @Override
  public String getFilter() {
    return "mcpherson911@mcphersoncountyks.us";
  }

  @Override
  public int getMapFlags() {
    return MAP_FLG_PREFER_GPS;
  }

}
