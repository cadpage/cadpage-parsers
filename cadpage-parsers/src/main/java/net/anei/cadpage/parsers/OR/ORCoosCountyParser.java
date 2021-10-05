package net.anei.cadpage.parsers.OR;

import net.anei.cadpage.parsers.dispatch.DispatchA55Parser;

public class ORCoosCountyParser extends DispatchA55Parser {

  public ORCoosCountyParser() {
    super("COOS COUNTY", "OR");
  }

  @Override
  public String getFilter() {
    return "ereports@eforcesoftware.com,cadalerts@eforcesoftware.com";
  }
}
