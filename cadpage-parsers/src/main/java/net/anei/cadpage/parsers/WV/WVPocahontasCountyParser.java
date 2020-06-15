package net.anei.cadpage.parsers.WV;

import net.anei.cadpage.parsers.dispatch.DispatchA19Parser;


public class WVPocahontasCountyParser extends DispatchA19Parser {
  
  public WVPocahontasCountyParser() {
    super("POCAHONTAS COUNTY", "WV");
  }

  @Override
  public String getFilter() {
    return "@pocahontasemergency.com";
  }
}
