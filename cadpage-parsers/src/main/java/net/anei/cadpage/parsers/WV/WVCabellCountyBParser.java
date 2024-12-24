package net.anei.cadpage.parsers.WV;

import net.anei.cadpage.parsers.dispatch.DispatchA95Parser;

public class WVCabellCountyBParser extends DispatchA95Parser {

  public WVCabellCountyBParser() {
    super("CABELL COUNTY", "WV");
  }

  @Override
  public String getFilter() {
    return "dispatch@ccerc911.org";
  }

}
