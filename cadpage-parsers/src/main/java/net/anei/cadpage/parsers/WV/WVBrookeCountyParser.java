package net.anei.cadpage.parsers.WV;

import net.anei.cadpage.parsers.dispatch.DispatchA19Parser;

public class WVBrookeCountyParser extends DispatchA19Parser {
  
  public WVBrookeCountyParser() {
    super("BROOKE COUNTY", "WV");
  }
  
  @Override
  public String getFilter() {
    return "rapid@brooke911.com";
  }

}
