package net.anei.cadpage.parsers.KY;

import net.anei.cadpage.parsers.dispatch.DispatchA27Parser;

public class KYLoganCountyBParser extends DispatchA27Parser {
  
  public KYLoganCountyBParser() {
    super("LOGAN COUNTY", "KY");
  }
  
  @Override
  public  String getFilter() {
    return "noreply@cis.com,noreply@cisusa.org";
  }
  
  @Override
  public int getMapFlags() {
    return MAP_FLG_PREFER_GPS;
  }
}
