package net.anei.cadpage.parsers.NJ;

import net.anei.cadpage.parsers.dispatch.DispatchC06Parser;

public class NJAtlanticCountyEParser extends DispatchC06Parser {

  public NJAtlanticCountyEParser() {
    super("ATLANTIC COUNTY", "NJ");
  }

  @Override
  public String getFilter() {
    return "@Enfwebmail.onmicrosoft.com";
  }
}
