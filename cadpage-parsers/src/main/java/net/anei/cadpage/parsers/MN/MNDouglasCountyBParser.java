package net.anei.cadpage.parsers.MN;

import net.anei.cadpage.parsers.dispatch.DispatchA70Parser;

/**
 * Douglas County, MN
 */

public class MNDouglasCountyBParser extends DispatchA70Parser {
  
  public MNDouglasCountyBParser() {
    super("DOUGLAS COUNTY", "MN");
  }
  
  @Override
  public String getFilter() {
    return "noreply@cisusa.org";
  }
}
