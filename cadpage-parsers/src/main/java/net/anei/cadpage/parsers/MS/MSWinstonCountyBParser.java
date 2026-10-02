package net.anei.cadpage.parsers.MS;

import net.anei.cadpage.parsers.dispatch.DispatchSPKParser;

public class MSWinstonCountyBParser extends DispatchSPKParser {

  public MSWinstonCountyBParser() {
    super("WINSTON COUNTY", "MS");
  }

  @Override
  public String getFilter() {
    return "louisvillems911alerts@gmail.com";
  }
}
