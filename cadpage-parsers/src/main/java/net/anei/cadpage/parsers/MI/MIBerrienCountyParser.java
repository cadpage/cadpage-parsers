package net.anei.cadpage.parsers.MI;

import net.anei.cadpage.parsers.dispatch.DispatchA87Parser;

public class MIBerrienCountyParser extends DispatchA87Parser {

  public MIBerrienCountyParser() {
    super("BERRIEN COUNTY", "MI");
  }

  @Override
  public String getFilter() {
    return "Dispatch@berriencounty.org";
  }
}
