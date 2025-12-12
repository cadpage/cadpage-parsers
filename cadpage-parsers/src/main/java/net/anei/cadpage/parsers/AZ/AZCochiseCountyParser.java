package net.anei.cadpage.parsers.AZ;

import net.anei.cadpage.parsers.dispatch.DispatchA71Parser;

public class AZCochiseCountyParser extends DispatchA71Parser {

  public AZCochiseCountyParser() {
    super("COCHISE COUNTY", "AZ");
  }

  @Override
  public String getFilter() {
    return "@azsis.us,eric@fcstucson.com";
  }

}
