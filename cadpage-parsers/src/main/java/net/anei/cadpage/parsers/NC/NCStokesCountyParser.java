package net.anei.cadpage.parsers.NC;

import net.anei.cadpage.parsers.dispatch.DispatchA71Parser;

/**
 *Stokes County, NC
 */
public class NCStokesCountyParser extends DispatchA71Parser {

  public NCStokesCountyParser() {
    super("STOKES COUNTY", "NC");
  }

  @Override
  public int getMapFlags() {
    return MAP_FLG_PREFER_GPS;
  }
}
