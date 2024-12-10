package net.anei.cadpage.parsers.TN;

import net.anei.cadpage.parsers.dispatch.DispatchA86Parser;

public class TNPolkCountyParser extends DispatchA86Parser {

  public TNPolkCountyParser() {
    super("POLK COUNTY", "TN");
  }

  @Override
  public String getFilter() {
    return "Dispatch@PolkTNE911.info";
  }

  @Override
  public int getMapFlags() {
    return MAP_FLG_PREFER_GPS;
  }

}
