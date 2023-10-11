package net.anei.cadpage.parsers.SC;

import net.anei.cadpage.parsers.dispatch.DispatchA19Parser;

public class SCCalhounCountyParser extends DispatchA19Parser {

  public SCCalhounCountyParser() {
    super("CALHOUN COUNTY", "SC");
  }

  @Override
  public String getFilter() {
    return "generalinfo@ccso.sc.gov";
  }

  @Override
  public int getMapFlags() {
    return MAP_FLG_PREFER_GPS;
  }
}
