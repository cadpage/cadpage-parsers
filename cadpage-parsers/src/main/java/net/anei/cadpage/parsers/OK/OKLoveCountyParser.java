package net.anei.cadpage.parsers.OK;

import net.anei.cadpage.parsers.dispatch.DispatchA25Parser;


public class OKLoveCountyParser extends DispatchA25Parser {
  
  public OKLoveCountyParser() {
    super("LOVE COUNTY", "OK");
  }
  
  @Override
  public String getFilter() {
    return "Alert@loveco911.org,EnterpolAlerts@loveco911.org";
  }
}
