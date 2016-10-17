package net.anei.cadpage.parsers.MN;

import net.anei.cadpage.parsers.dispatch.DispatchA27Parser;

/**
 * Wabasha County, MN
 */

public class MNWabashaCountyParser extends DispatchA27Parser {
  
  public MNWabashaCountyParser() {
    super("WABASHA COUNTY", "MN", "\\d{8}|[A-Z]{1,3}FD");
  }
  
  @Override
  public String getFilter() {
    return "noreply@cisusa.org";
  }
}
