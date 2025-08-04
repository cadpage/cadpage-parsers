package net.anei.cadpage.parsers.PA;

import net.anei.cadpage.parsers.dispatch.DispatchA27Parser;

public class PALycomingCountyParser extends DispatchA27Parser {

  public PALycomingCountyParser() {
    super("LYCOMING COUNTY", "PA");
  }

  @Override
  public String getFilter() {
    return "911@lyco.org";
  }

  @Override
  public int getMapFlags() {
    return MAP_FLG_PREFER_GPS;
  }
}
