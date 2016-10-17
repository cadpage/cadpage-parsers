package net.anei.cadpage.parsers.OH;

import net.anei.cadpage.parsers.dispatch.DispatchCiscoParser;

/**
 * Allen County, OH
 */
public class OHAllenCountyAParser extends DispatchCiscoParser {

  public OHAllenCountyAParser() {
    super("ALLEN COUNTY", "OH");
  }

  @Override
  public String getFilter() {
    return "interface@acso-oh.us";
  }
}
  