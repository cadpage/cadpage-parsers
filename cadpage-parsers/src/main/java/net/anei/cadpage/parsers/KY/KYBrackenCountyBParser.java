package net.anei.cadpage.parsers.KY;

import net.anei.cadpage.parsers.dispatch.DispatchA27Parser;

public class KYBrackenCountyBParser extends DispatchA27Parser {
  
  public KYBrackenCountyBParser() {
    super("BRACKEN COUNTY", "KY");
  }
  
  @Override
  public String getFilter() {
    return "noreply@cisusa.org";
  }

}
