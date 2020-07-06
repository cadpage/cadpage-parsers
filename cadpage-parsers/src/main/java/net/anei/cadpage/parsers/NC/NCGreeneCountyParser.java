
package net.anei.cadpage.parsers.NC;

import net.anei.cadpage.parsers.dispatch.DispatchA71Parser;

public class NCGreeneCountyParser extends DispatchA71Parser {

  public NCGreeneCountyParser() {
    super("GREENE COUNTY", "NC");
  }

  @Override
  public int getMapFlags() {
    return MAP_FLG_PREFER_GPS;
  }
}
