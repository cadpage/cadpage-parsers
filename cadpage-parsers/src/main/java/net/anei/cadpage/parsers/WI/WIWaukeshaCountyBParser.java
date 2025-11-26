package net.anei.cadpage.parsers.WI;

import net.anei.cadpage.parsers.dispatch.DispatchA63Parser;



public class WIWaukeshaCountyBParser extends DispatchA63Parser {  
  public WIWaukeshaCountyBParser() {
    super("WAUKESHA COUNTY", "WI");
  }
  
  @Override
  public String getFilter() {
    return "PhoenixNotification@elmgrovewi.org,dispatch@mukwonagofire.org,systemadmin@mkpd.org";
  }
  
  @Override
  public int getMapFlags() {
    return MAP_FLG_SUPPR_LA;
  }
  
}
