package net.anei.cadpage.parsers.TN;

import net.anei.cadpage.parsers.dispatch.DispatchA86Parser;

public class TNChesterCountyParser extends DispatchA86Parser {

  public TNChesterCountyParser() {
    super("CHESTER COUNTY", "TN");
  }

  @Override
  public String getFilter() {
    return "dispatch@chestertn911.info";
  }

  @Override
  public int getMapFlags() {
    return MAP_FLG_PREFER_GPS;
  }
}
