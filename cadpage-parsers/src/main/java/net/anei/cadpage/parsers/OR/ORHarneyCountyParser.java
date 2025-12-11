package net.anei.cadpage.parsers.OR;

import net.anei.cadpage.parsers.dispatch.DispatchA19Parser;

public class ORHarneyCountyParser extends DispatchA19Parser {

  public ORHarneyCountyParser() {
    super("HARNEY COUNTY", "OR");
  }

  @Override
  public String getFilter() {
    return "FRN-harneycountyor@email.getrave.com";
  }

  @Override
  public int getMapFlags() {
    return MAP_FLG_PREFER_GPS;
  }
}
