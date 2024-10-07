package net.anei.cadpage.parsers.WA;

import net.anei.cadpage.parsers.dispatch.DispatchA19Parser;

public class WAGraysHarborCountyBParser extends DispatchA19Parser {

  public WAGraysHarborCountyBParser() {
    super("GRAYS HARBOR COUNTY", "WA");
  }

  @Override
  public String getFilter() {
    return "noreply@gh911.org";
  }
}
