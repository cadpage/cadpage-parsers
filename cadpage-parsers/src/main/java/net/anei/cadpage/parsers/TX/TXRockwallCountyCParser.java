package net.anei.cadpage.parsers.TX;

import net.anei.cadpage.parsers.dispatch.DispatchH04Parser;

public class TXRockwallCountyCParser extends DispatchH04Parser {
  
  public TXRockwallCountyCParser() {
    super("ROCKWALL COUNTY", "TX");
  }
  
  @Override
  public String getFilter() {
    return "@rockwall.com";
  }
}
