package net.anei.cadpage.parsers.VT;

import net.anei.cadpage.parsers.dispatch.DispatchA71Parser;

public class VTChittendenCountyFParser extends DispatchA71Parser {
  
  public VTChittendenCountyFParser() {
    super("CHITTENDEN COUNTY", "VT");
  }
  
  @Override
  public String getFilter() {
    return "Teledir@smcvt.edu";
  }

}
