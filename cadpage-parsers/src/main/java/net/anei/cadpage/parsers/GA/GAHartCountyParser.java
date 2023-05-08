package net.anei.cadpage.parsers.GA;

import net.anei.cadpage.parsers.dispatch.DispatchSPKParser;

public class GAHartCountyParser extends DispatchSPKParser {

  public GAHartCountyParser() {
    super("HART COUNTY", "GA");
  }

  @Override
  public String getFilter() {
    return "hartcong911@hartcountyga.gov";
  }
}
