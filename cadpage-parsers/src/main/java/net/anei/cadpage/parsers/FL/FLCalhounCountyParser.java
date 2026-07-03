package net.anei.cadpage.parsers.FL;

import net.anei.cadpage.parsers.dispatch.DispatchA98Parser;

public class FLCalhounCountyParser extends DispatchA98Parser {

  public FLCalhounCountyParser() {
    super("CALHOUN COUNTY", "FL");
  }

  @Override
  public String getFilter() {
    return "no-reply@smartcopcloud.com";
  }

  @Override
  public int getMapFlags() {
    return MAP_FLG_PREFER_GPS | MAP_FLG_SUPPR_LA;
  }
}
