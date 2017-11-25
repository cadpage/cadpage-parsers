package net.anei.cadpage.parsers.MN;

import net.anei.cadpage.parsers.dispatch.DispatchA27Parser;

/**
 * Douglas County, MN
 */

public class MNDouglasCountyAParser extends DispatchA27Parser {
  
  public MNDouglasCountyAParser() {
    super("DOUGLAS COUNTY", "MN", "\\d+[A-Z]{2}");
  }
  
  @Override
  public String getFilter() {
    return "noreply@cisusa.org";
  }
}
