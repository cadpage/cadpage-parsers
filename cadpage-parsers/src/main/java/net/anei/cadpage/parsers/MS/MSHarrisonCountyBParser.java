package net.anei.cadpage.parsers.MS;

import net.anei.cadpage.parsers.dispatch.DispatchA72Parser;

public class MSHarrisonCountyBParser extends DispatchA72Parser {

  public MSHarrisonCountyBParser() {
    super("HARRISON COUNTY", "MS");
  }

  @Override
  public String getFilter() {
    return "fdalerts@gulfport-ms.gov";
  }
}
