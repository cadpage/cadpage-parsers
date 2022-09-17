package net.anei.cadpage.parsers.TN;

import net.anei.cadpage.parsers.dispatch.DispatchA86Parser;

public class TNCumberlandCountyBParser extends DispatchA86Parser {

  public TNCumberlandCountyBParser() {
    super("CUMBERLAND COUNTY", "TN");
  }

  @Override
  public String getFilter() {
    return "dispatch@cumberlandtn911.info";
  }

  @Override
  public int getMapFlags() {
    return MAP_FLG_PREFER_GPS;
  }

}
