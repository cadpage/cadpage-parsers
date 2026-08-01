package net.anei.cadpage.parsers.VT;

import net.anei.cadpage.parsers.dispatch.DispatchA71Parser;

public class VTWashingtonCountyBParser extends DispatchA71Parser {

  public VTWashingtonCountyBParser() {
    super("WASHINGTON COUNTY", "VT");
  }

  @Override
  public String getFilter() {
    return "@montpelier-vt.org";
  }

}
