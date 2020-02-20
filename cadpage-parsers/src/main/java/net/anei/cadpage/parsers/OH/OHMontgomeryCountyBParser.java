package net.anei.cadpage.parsers.OH;

import net.anei.cadpage.parsers.dispatch.DispatchA19Parser;

/**
 * Montgomery County, OH
 */
public class OHMontgomeryCountyBParser extends DispatchA19Parser {
  
  public OHMontgomeryCountyBParser() {
    super("MONTGOMERY COUNTY", "OH");
  }
  
  @Override
  public String getFilter() {
    return "DISPATCH@vandaliaohio.org,dispatch-noreply@ketteringoh.org";
  }
  
  @Override
  public int getMapFlags() {
    return MAP_FLG_PREFER_GPS;
  }
}
