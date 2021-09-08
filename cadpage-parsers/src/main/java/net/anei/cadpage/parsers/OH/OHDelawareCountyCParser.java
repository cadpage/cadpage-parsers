package net.anei.cadpage.parsers.OH;

import net.anei.cadpage.parsers.dispatch.DispatchH06Parser;

public class OHDelawareCountyCParser extends DispatchH06Parser {

  public OHDelawareCountyCParser() {
    super("DELAWARE COUNTY", "OH");
  }

  @Override
  public String getFilter() {
    return "del-911@co.delaware.oh.us";
  }
}
