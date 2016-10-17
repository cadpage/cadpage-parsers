package net.anei.cadpage.parsers.MN;

import net.anei.cadpage.parsers.dispatch.DispatchA27Parser;

/**
 * Goodhue County, MN
 */

public class MNGoodhueCountyParser extends DispatchA27Parser {
  
  public MNGoodhueCountyParser() {
    super("GOODHUE COUNTY", "MN", "[A-Z]+\\d+[A-Z]?|Z[A-Z]+|[A-Z]{1,3}FD");
  }
  
  @Override
  public String getFilter() {
    return "noreply@cisusa.org";
  }
  
  @Override
  public int getMapFlags() {
    return MAP_FLG_SUPPR_CR;
  }
}
