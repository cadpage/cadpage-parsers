package net.anei.cadpage.parsers.MO;

import net.anei.cadpage.parsers.dispatch.DispatchSPKParser;

public class MOStoneCountyCParser extends DispatchSPKParser {
  public MOStoneCountyCParser() {
    super("STONE COUNTY", "MO");
  }

  @Override
  public String getFilter() {
    return "stoneco911@gmail.com";
  }
}
