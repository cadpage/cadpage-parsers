package net.anei.cadpage.parsers.WI;

import net.anei.cadpage.parsers.dispatch.DispatchA63Parser;



public class WIWaukeshaCountyCParser extends DispatchA63Parser {  
  public WIWaukeshaCountyCParser() {
    super("WAUKESHA COUNTY", "WI");
  }
  
  @Override
  public String getFilter() {
    return "PhoenixNotification@elmgrovewi.org";
  }
  
  @Override
  public int getMapFlags() {
    return MAP_FLG_SUPPR_LA;
  }
  
}
