package net.anei.cadpage.parsers.MN;

import net.anei.cadpage.parsers.dispatch.DispatchA27Parser;

/**
 * Douglas County, MN
 */

public class MNDouglasCountyBParser extends DispatchA27Parser {
  
  public MNDouglasCountyBParser() {
    super("DOUGLAS COUNTY", "MN");
  }
  
  @Override
  public String getFilter() {
    return "noreply@cisusa.org,cis@co.douglas.mn.us";
  }
}
