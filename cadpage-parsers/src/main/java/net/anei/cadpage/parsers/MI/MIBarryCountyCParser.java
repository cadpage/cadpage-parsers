package net.anei.cadpage.parsers.MI;

import net.anei.cadpage.parsers.dispatch.DispatchC08Parser;

public class MIBarryCountyCParser extends DispatchC08Parser {

  public MIBarryCountyCParser() {
    super("BARRY COUNTY", "MI");
  }

  @Override
  public String getFilter() {
    return "no_reply@rockford.traumasoft.com";
  }

  @Override
  public int getMapFlags() {
    return MAP_FLG_PREFER_GPS;
  }
}
