package net.anei.cadpage.parsers.OH;

import net.anei.cadpage.parsers.dispatch.DispatchCiscoParser;

/**
 * Warren County, OH
 */
public class OHWarrenCountyBParser extends DispatchCiscoParser {
  
  public OHWarrenCountyBParser() {
    super("WARREN COUNTY", "OH");
  }
  
  @Override
  public String getFilter() {
    return "dispatch@lebanonohio.gov";
  }
  
  @Override
  public int getMapFlags() {
    return MAP_FLG_SUPPR_LA;
  }
}
