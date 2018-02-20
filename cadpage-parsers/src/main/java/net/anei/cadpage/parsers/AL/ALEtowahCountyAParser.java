package net.anei.cadpage.parsers.AL;

import net.anei.cadpage.parsers.dispatch.DispatchGeoconxParser;

/**
 * Etowah County, AL
 */
public class ALEtowahCountyAParser extends DispatchGeoconxParser {
  
  public ALEtowahCountyAParser() {
    super("ETOWAH COUNTY", "AL", GCX_FLG_LEAD_PLACE | GCX_FLG_TRAIL_PLACE);
  }
  
  @Override
  public String getFilter() {
    return "etowahcoal911@911email.net";
  }
}
