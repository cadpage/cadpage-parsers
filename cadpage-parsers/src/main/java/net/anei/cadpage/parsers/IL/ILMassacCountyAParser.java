
package net.anei.cadpage.parsers.IL;

import net.anei.cadpage.parsers.dispatch.DispatchA19Parser;

/**
 * Massac County, IL
 */
public class ILMassacCountyAParser extends DispatchA19Parser {

  public ILMassacCountyAParser() {
    super("MASSAC COUNTY", "IL");
  }

  @Override
  public String getFilter() {
    return "911dispatch@metropolisil.gov";
  }

  @Override
  public int getMapFlags() {
    return MAP_FLG_PREFER_GPS;
  }
}
