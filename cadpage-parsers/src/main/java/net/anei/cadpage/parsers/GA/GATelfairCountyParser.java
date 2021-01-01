package net.anei.cadpage.parsers.GA;

import net.anei.cadpage.parsers.dispatch.DispatchSPKParser;

public class GATelfairCountyParser extends DispatchSPKParser {

  public GATelfairCountyParser() {
    super("TELFAIR COUNTY", "GA");
  }

  @Override
  public String getFilter() {
    return "telfairwheelere911@windstream.net";
  }
}
