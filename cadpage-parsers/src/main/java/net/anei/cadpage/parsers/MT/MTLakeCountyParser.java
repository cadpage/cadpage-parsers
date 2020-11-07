package net.anei.cadpage.parsers.MT;

import net.anei.cadpage.parsers.dispatch.DispatchA49Parser;

public class MTLakeCountyParser extends DispatchA49Parser {

  public MTLakeCountyParser() {
    super("LAKE COUNTY", "MT");
  }
  
  @Override
  public String getFilter() {
    return "cadpage@e9.com";
  }
}
