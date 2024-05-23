package net.anei.cadpage.parsers.WI;

import net.anei.cadpage.parsers.dispatch.DispatchA19Parser;

public class WIRockCountyBParser extends DispatchA19Parser {

  public WIRockCountyBParser() {
    super("ROCK COUNTY", "WI");
  }

  @Override
  public String getFilter() {
    return "@alert.active911.com,messaging@iamresponding.com,noreplyspillman@pdmonroe.com";
  }
}
