package net.anei.cadpage.parsers.OH;

import net.anei.cadpage.parsers.dispatch.DispatchC13Parser;

public class OHGreeneCountyCParser extends DispatchC13Parser {

  public OHGreeneCountyCParser() {
    super("GREENE COUNTY", "OH");
  }

  @Override
  public int getMapFlags() {
    return MAP_FLG_PREFER_GPS;
  }
}
