package net.anei.cadpage.parsers.TN;

import net.anei.cadpage.parsers.dispatch.DispatchGeoconxParser;


public class TNUnionCountyParser extends DispatchGeoconxParser {
  
  public TNUnionCountyParser() {
    super("UNION COUNTY", "TN", GCX_FLG_NAME_PHONE);
  }
  
  @Override
  public String getFilter() {
    return "dispatch@911email.net";
  }
}
