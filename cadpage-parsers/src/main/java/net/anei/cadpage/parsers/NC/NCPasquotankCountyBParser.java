package net.anei.cadpage.parsers.NC;

import net.anei.cadpage.parsers.dispatch.DispatchA95Parser;

public class NCPasquotankCountyBParser extends DispatchA95Parser {

  public NCPasquotankCountyBParser() {
    super("PASQUOTANK COUNTY", "NC");
  }

  @Override public String getFilter() {
    return "Cad@co.pasquotank.nc.us";
  }

  @Override
  public int getMapFlags() {
    return MAP_FLG_PREFER_GPS;
  }
}
