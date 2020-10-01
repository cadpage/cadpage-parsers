package net.anei.cadpage.parsers.KY;

import net.anei.cadpage.parsers.dispatch.DispatchA70Parser;

public class KYKnoxCountyBParser extends DispatchA70Parser {
  
  public KYKnoxCountyBParser() {
    super("KNOX COUNTY", "KY");
  }
  
  @Override
  public String getFilter() {
    return "noreply@cisusa.org";
  }
}
