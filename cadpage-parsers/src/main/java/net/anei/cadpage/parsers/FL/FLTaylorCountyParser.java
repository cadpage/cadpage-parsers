package net.anei.cadpage.parsers.FL;

import net.anei.cadpage.parsers.dispatch.DispatchA98Parser;

public class FLTaylorCountyParser extends DispatchA98Parser {

  public FLTaylorCountyParser() {
    super("TAYLOR COUNTY", "FL");
  }

  @Override
  public int getMapFlags() {
    return MAP_FLG_PREFER_GPS | MAP_FLG_SUPPR_LA;
  }
}
