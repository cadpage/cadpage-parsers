package net.anei.cadpage.parsers.WI;

import net.anei.cadpage.parsers.dispatch.DispatchProphoenixParser;

public class WIWaukeshaCountyBParser extends DispatchProphoenixParser {

  public WIWaukeshaCountyBParser() {
    super("WAUKESHA COUNTY", "WI");
  }
  
  @Override
  public String getFilter() {
    return "dispatch@mukwonagofire.org,systemadmin@mkpd.org";
  }
}