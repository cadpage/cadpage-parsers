package net.anei.cadpage.parsers.MN;

import net.anei.cadpage.parsers.dispatch.DispatchA27Parser;

public class MNBrownCountyParser extends DispatchA27Parser {
  
  public MNBrownCountyParser() {
    super("BROWN COUNTY", "MN", "[A-Z]{3,4}");
  }
  
  @Override
  public String getFilter() {
    return "noreply@cisusa.org";
  }
}
