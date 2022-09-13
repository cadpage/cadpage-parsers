package net.anei.cadpage.parsers.WY;

import net.anei.cadpage.parsers.dispatch.DispatchA20Parser;

/**
 * Park County, WY
 */
public class WYParkCountyParser extends DispatchA20Parser {

  public WYParkCountyParser() {
    super("PARK COUNTY", "WY");
  }

  @Override
  public String getFilter() {
    return "@parkcounty.us,chief@pvfd.net,@parkcounty-wy.gov,@parkcountysheriff-wy.gov,bhcsheriffpaging@gmail.com";
  }

}
