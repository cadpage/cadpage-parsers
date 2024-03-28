package net.anei.cadpage.parsers.NJ;

import net.anei.cadpage.parsers.dispatch.DispatchH06Parser;

/**
 * Burlington County, NJ
 */
public class NJBurlingtonCountyCParser extends DispatchH06Parser {

  public NJBurlingtonCountyCParser() {
    super("BURLINGTON COUNTY", "NJ");
  }

  @Override
  public String getFilter() {
    return "CAD@co.burlington.nj.us";
  }

  @Override
  public int getMapFlags() {
    return MAP_FLG_PREFER_GPS;
  }
}
