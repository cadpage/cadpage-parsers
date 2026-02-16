package net.anei.cadpage.parsers.AZ;

import net.anei.cadpage.parsers.dispatch.DispatchA27Parser;

public class AZCoconinoCountyAParser extends DispatchA27Parser {

  public AZCoconinoCountyAParser() {
    super("COCONINO COUNTY", "AZ");
  }

  @Override
  public String getFilter() {
    return "noreply@cisusa.org,williamspd@cissystem.com";
  }

}
