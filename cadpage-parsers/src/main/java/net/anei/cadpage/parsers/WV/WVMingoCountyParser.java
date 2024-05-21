package net.anei.cadpage.parsers.WV;

import net.anei.cadpage.parsers.dispatch.DispatchA71Parser;

public class WVMingoCountyParser extends DispatchA71Parser {

  public WVMingoCountyParser() {
    super("MINGO COUNTY", "WV");
  }

  @Override
  public String getFilter() {
    return "notify@somahub.io";
  }

  @Override
  public int getMapFlags() {
    return MAP_FLG_PREFER_GPS;
  }

}
