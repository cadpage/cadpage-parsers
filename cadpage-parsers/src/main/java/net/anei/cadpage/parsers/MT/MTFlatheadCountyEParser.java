package net.anei.cadpage.parsers.MT;

import net.anei.cadpage.parsers.dispatch.DispatchA49Parser;

public class MTFlatheadCountyEParser extends DispatchA49Parser {
  
  public MTFlatheadCountyEParser() {
    super("FLATHEAD COUNTY", "MT");
  }
  
  @Override
  public String getFilter() {
    return "cadpage@e9.com";
  }

}
