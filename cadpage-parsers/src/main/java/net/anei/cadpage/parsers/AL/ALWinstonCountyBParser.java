package net.anei.cadpage.parsers.AL;

import net.anei.cadpage.parsers.dispatch.DispatchA74Parser;

public class ALWinstonCountyBParser extends DispatchA74Parser {

  public ALWinstonCountyBParser() {
    super("WINSTON COUNTY", "AL");
  }

  @Override
  public String getFilter() {
    return "dispatch@winston911.net";
  }

  @Override
  public int getMapFlags() {
    return MAP_FLG_PREFER_GPS;
  }
}
