package net.anei.cadpage.parsers.OK;

import net.anei.cadpage.parsers.dispatch.DispatchA25Parser;

public class OKCarterCountyDParser extends DispatchA25Parser {
  
  public OKCarterCountyDParser() {
    super("CARTER COUNTY", "OK");
  }
  
  @Override
  public String getFilter() {
    return "smtp@lonegrove.org";
  }
}
