package net.anei.cadpage.parsers.MS;

import net.anei.cadpage.parsers.dispatch.DispatchSPKParser;

public class MSHarrisonCountyGParser extends DispatchSPKParser {

  public MSHarrisonCountyGParser() {
    super("HARRISON COUNTY", "MS");
  }

  @Override
  public String getFilter() {
    return "cad@cityoflongbeachms.com";
  }
}
