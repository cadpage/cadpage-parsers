package net.anei.cadpage.parsers.OR;

import net.anei.cadpage.parsers.dispatch.DispatchA55Parser;

public class ORCoosCountyAParser extends DispatchA55Parser {

  public ORCoosCountyAParser() {
    super("COOS COUNTY", "OR");
  }

  @Override
  public String getFilter() {
    return "ereports@eforcesoftware.com,cadalerts@eforcesoftware.com";
  }
}
