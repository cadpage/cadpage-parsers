package net.anei.cadpage.parsers.GA;

import net.anei.cadpage.parsers.dispatch.DispatchSPKParser;

public class GAGreeneCountyParser extends DispatchSPKParser {

  public GAGreeneCountyParser() {
    super("GREENE COUNTY", "GA");
  }

  @Override
  public String getFilter() {
    return "911admin@greene911.com";
  }

  @Override
  public int getMapFlags() {
    return MAP_FLG_PREFER_GPS;
  }
}
