package net.anei.cadpage.parsers.NC;

import net.anei.cadpage.parsers.dispatch.DispatchGeoconxParser;

/**
 * Grahm County, NC
 */
public class NCGrahamCountyParser extends DispatchGeoconxParser {
  
  public NCGrahamCountyParser() {
    super("GRAHAM COUNTY", "NC", GCX_FLG_NAME_PHONE);
  }
  
  @Override
  public String getFilter() {
    return "dispatch@911email.net";
  }
}
