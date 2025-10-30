package net.anei.cadpage.parsers.KY;

import net.anei.cadpage.parsers.dispatch.DispatchA27Parser;

public class KYRowanCountyBParser extends DispatchA27Parser {
  
  public KYRowanCountyBParser() {
    super("ROWAN COUNTY", "KY");
  }
  
  @Override
  public String getFilter() {
    return "cis@cityofmorehead.net";
  }

}
