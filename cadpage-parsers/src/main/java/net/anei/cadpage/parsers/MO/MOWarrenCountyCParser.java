package net.anei.cadpage.parsers.MO;

import net.anei.cadpage.parsers.dispatch.DispatchA27Parser;

public class MOWarrenCountyCParser extends DispatchA27Parser {

  public MOWarrenCountyCParser() {
    super("WARREN COUNTY", "MO");
  }

  @Override
  public String getFilter() {
    return "dispatch@warrencounty911.org";
  }

  @Override
  public int getMapFlags() {
    return MAP_FLG_PREFER_GPS;
  }

}
