package net.anei.cadpage.parsers.OK;

import net.anei.cadpage.parsers.dispatch.DispatchSPKParser;

public class OKWagonerCountyParser extends DispatchSPKParser {

  public OKWagonerCountyParser() {
    super("WAGONER COUNTY", "OK");
  }

  @Override
  public String getFilter() {
    return "@wagonercounty.ok.gov";
  }
}
