package net.anei.cadpage.parsers.VT;

import net.anei.cadpage.parsers.dispatch.DispatchA19Parser;

public class VTChittendenCountyAParser extends DispatchA19Parser {
  
  public VTChittendenCountyAParser() {
    super("CHITTENDEN COUNTY", "VT");
  }
  
  @Override
  public String getFilter() {
    return "@alert.active911.com";
  }
}
