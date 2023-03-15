package net.anei.cadpage.parsers.KY;

import net.anei.cadpage.parsers.dispatch.DispatchA27Parser;

public class KYBeckinridgeCountyParser extends DispatchA27Parser {
  
  public KYBeckinridgeCountyParser() {
    super("BECKINRIDGE COUNTY", "KY");
  }

  @Override
  public String getFilter() {
    return "noreply@cisusa.org";
  }
}
