package net.anei.cadpage.parsers.ZCAAB;

import net.anei.cadpage.parsers.dispatch.DispatchH03Parser;

public class ZCAABRedDeerCountyBParser extends DispatchH03Parser {

  public ZCAABRedDeerCountyBParser() {
    super("RED DEER COUNTY", "AB");
  }

  @Override
  public String getFilter() {
    return "ECC@reddeer.ca";
  }

}
