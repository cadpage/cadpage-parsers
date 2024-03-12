
package net.anei.cadpage.parsers.NC;

import net.anei.cadpage.parsers.dispatch.DispatchA71Parser;



public class NCWarrenCountyParser extends DispatchA71Parser {

  public NCWarrenCountyParser() {
    super("WARREN COUNTY", "NC");
  }

  @Override
  public String getFilter() {
    return "@warrencountync.com";
  }

  @Override
  public int getMapFlags() {
    return MAP_FLG_PREFER_GPS;
  }
}
