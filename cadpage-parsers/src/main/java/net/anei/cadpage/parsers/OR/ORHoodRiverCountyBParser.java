package net.anei.cadpage.parsers.OR;

import net.anei.cadpage.parsers.dispatch.DispatchA98Parser;

public class ORHoodRiverCountyBParser extends DispatchA98Parser {

  public ORHoodRiverCountyBParser() {
    super("HOOD RIVER COUNTY", "OR");
  }

  @Override
  public String getFilter() {
    return "CADnotifications@hoodrivercounty.gov";
  }

  @Override
  public int getMapFlags() {
    return MAP_FLG_PREFER_GPS;
  }
}
