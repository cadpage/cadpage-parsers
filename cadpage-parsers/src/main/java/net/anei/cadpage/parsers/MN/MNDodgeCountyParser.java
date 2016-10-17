package net.anei.cadpage.parsers.MN;

import net.anei.cadpage.parsers.dispatch.DispatchA27Parser;

/**
 * Dodge County, MN
 */

public class MNDodgeCountyParser extends DispatchA27Parser {
  
  public MNDodgeCountyParser() {
    super("DODGE COUNTY", "MN", "\\d{8}");
  }
  
  @Override
  public String getFilter() {
    return "noreply@cisusa.org";
  }
}
