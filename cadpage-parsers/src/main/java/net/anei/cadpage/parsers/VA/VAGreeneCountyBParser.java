package net.anei.cadpage.parsers.VA;

import net.anei.cadpage.parsers.dispatch.DispatchA19Parser;

public class VAGreeneCountyBParser extends DispatchA19Parser {

  public VAGreeneCountyBParser() {
    super("GREENE COUNTY", "VA");
  }

  @Override
  public String getFilter() {
    return "gcsoadmin@gcvasheriff.us";
  }

  @Override
  public int getMapFlags() {
    return MAP_FLG_PREFER_GPS;
  }
}
