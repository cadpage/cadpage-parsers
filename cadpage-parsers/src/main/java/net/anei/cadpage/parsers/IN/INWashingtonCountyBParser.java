package net.anei.cadpage.parsers.IN;

import net.anei.cadpage.parsers.dispatch.DispatchC08Parser;

public class INWashingtonCountyBParser extends DispatchC08Parser {

  public INWashingtonCountyBParser() {
    super("WASHINGTON COUNTY", "IN");
  }

  @Override
  public String getFilter() {
    return "no_reply@lynxems.traumasoft.com";
  }

  @Override
  public int getMapFlags() {
    return MAP_FLG_PREFER_GPS;
  }
}
