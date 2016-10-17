
package net.anei.cadpage.parsers.AZ;

import net.anei.cadpage.parsers.dispatch.DispatchA27Parser;

/**
 * Navajo County, AZ
 */

public class AZNavajoCountyAParser extends DispatchA27Parser {

  public AZNavajoCountyAParser() {
    super("NAVAJO COUNTY", "AZ", "[A-Z]+\\d+");
  }
  
  @Override
  public String getFilter() {
    return "noreply@cisusa.org";
  }
  
  @Override
  public int getMapFlags() {
    return MAP_FLG_SUPPR_LA;
  }
}
