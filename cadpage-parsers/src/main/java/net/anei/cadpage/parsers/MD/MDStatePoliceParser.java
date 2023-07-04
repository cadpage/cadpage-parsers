package net.anei.cadpage.parsers.MD;

import net.anei.cadpage.parsers.dispatch.DispatchSPKParser;

public class MDStatePoliceParser extends DispatchSPKParser {

  public MDStatePoliceParser() {
    super("", "MD");
  }

  @Override
  public String getFilter() {
    return "MSP_CAD@server.com";
  }

  @Override
  public String getLocName() {
    return "Maryland State Police";
  }
}
