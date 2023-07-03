package net.anei.cadpage.parsers.NC;

import net.anei.cadpage.parsers.dispatch.DispatchA71Parser;

public class NCAlleghanyCountyBParser extends DispatchA71Parser {

  public NCAlleghanyCountyBParser() {
    super("ALLEGHANY COUNTY", "NC");
  }

  @Override
  public int getMapFlags() {
    return MAP_FLG_PREFER_GPS;
  }

}
