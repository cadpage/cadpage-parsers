package net.anei.cadpage.parsers.TN;

import net.anei.cadpage.parsers.dispatch.DispatchA74Parser;

public class TNMcNairyCountyParser extends DispatchA74Parser {

  public TNMcNairyCountyParser() {
    super("MCNAIRY COUNTY", "TN");
  }

  @Override
  public String getFilter() {
    return "dispatch@mcnairytn911.info";
  }

  @Override
  public int getMapFlags() {
    return MAP_FLG_PREFER_GPS;
  }
}
