package net.anei.cadpage.parsers.KY;

import net.anei.cadpage.parsers.dispatch.DispatchA27Parser;

public class KYCaldwellCountyBParser extends DispatchA27Parser {
  
  public KYCaldwellCountyBParser() {
    super("CALDWELL COUNTY", "KY");
  }
  
  @Override
  public String getFilter() {
    return "peaccaldwellky@cissystem.com";
  }

}
