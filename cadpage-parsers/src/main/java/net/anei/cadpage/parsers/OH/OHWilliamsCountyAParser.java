package net.anei.cadpage.parsers.OH;

import net.anei.cadpage.parsers.dispatch.DispatchA19Parser;

public class OHWilliamsCountyAParser extends DispatchA19Parser {

  public OHWilliamsCountyAParser() {
    super("WILLIAMS COUNTY", "OH");
  }

  @Override
  public String getFilter() {
    return "BryanCAD,info@sundance-sys.com";
  }
}
