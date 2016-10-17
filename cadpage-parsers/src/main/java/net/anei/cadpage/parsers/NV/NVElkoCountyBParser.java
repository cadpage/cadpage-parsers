package net.anei.cadpage.parsers.NV;

import net.anei.cadpage.parsers.dispatch.DispatchA27Parser;

/**
 * Elko County, NV
 */

public class NVElkoCountyBParser extends DispatchA27Parser {
  
  public NVElkoCountyBParser() {
    super("ELKO COUNTY", "NV", "\\d{8}");
  }
  
  @Override
  public String getFilter() {
    return "noreply@cisusa.org";
  }
}
