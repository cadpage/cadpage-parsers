package net.anei.cadpage.parsers.KY;

import net.anei.cadpage.parsers.dispatch.DispatchA27Parser;

public class KYKnoxCountyCParser extends DispatchA27Parser {
  
  public KYKnoxCountyCParser() {
    super("KNOX COUNTY", "KY");
  }
  
  @Override
  public String getFilter() {
    return "noreply@cisusa.org";
  }
}
