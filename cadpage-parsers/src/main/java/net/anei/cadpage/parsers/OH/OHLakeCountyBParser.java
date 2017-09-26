package net.anei.cadpage.parsers.OH;

import net.anei.cadpage.parsers.dispatch.DispatchA9Parser;

public class OHLakeCountyBParser extends DispatchA9Parser {

  public OHLakeCountyBParser() {
    super("LAKE COUNTY", "OH");
  }
  
  @Override
  public String getFilter() {
    return "Fire_Calls@cityofmentor.com";
  }

}
