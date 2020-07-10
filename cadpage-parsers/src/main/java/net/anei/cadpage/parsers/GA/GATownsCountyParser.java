package net.anei.cadpage.parsers.GA;

import net.anei.cadpage.parsers.dispatch.DispatchSPKParser;

public class GATownsCountyParser extends DispatchSPKParser {

  public GATownsCountyParser() {
    super("TOWNS COUNTY", "GA");
  }

  public String getFilter() {
    return "townscounty911@gmail.com";
  }

}
