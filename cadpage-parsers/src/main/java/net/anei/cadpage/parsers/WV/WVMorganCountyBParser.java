package net.anei.cadpage.parsers.WV;

import net.anei.cadpage.parsers.dispatch.DispatchA17Parser;

public class WVMorganCountyBParser extends DispatchA17Parser {
  
  public WVMorganCountyBParser() {
    super("MORGAN COUNTY", "WV");
  }
  
  @Override
  public String getFilter() {
    return "911morgancowv@morgancountywv.comcastbiz.net";
  }

}
