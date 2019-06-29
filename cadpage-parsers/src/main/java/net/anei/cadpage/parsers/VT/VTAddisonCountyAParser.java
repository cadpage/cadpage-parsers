package net.anei.cadpage.parsers.VT;

import net.anei.cadpage.parsers.dispatch.DispatchA19Parser;



public class VTAddisonCountyAParser extends DispatchA19Parser {
  
  public VTAddisonCountyAParser() {
    super("ADDISON COUNTY", "VT");
  }
  
  @Override
  public String getFilter() {
    return "@alert.active911.com";
  }
}
