
package net.anei.cadpage.parsers.AL;

import net.anei.cadpage.parsers.dispatch.DispatchA71Parser;

/**
 * Henry County, AL
 */
public class ALHaleCountyParser extends DispatchA71Parser {

  public ALHaleCountyParser() {
    super("HALE COUNTY", "AL");
  }

  @Override
  public int getMapFlags() {
    return MAP_FLG_PREFER_GPS;
  }
}
