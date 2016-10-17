package net.anei.cadpage.parsers.TN;

import net.anei.cadpage.parsers.dispatch.DispatchGeoconxParser;


public class TNJohnsonCountyParser extends DispatchGeoconxParser {
  
  public TNJohnsonCountyParser() {
    super("JOHNSON COUNTY", "TN", GCX_FLG_NAME_PHONE);
  }
  
  @Override
  public String getFilter() {
    return "911 dispatch";
  }
}
