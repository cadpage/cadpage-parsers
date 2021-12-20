package net.anei.cadpage.parsers.TN;

import net.anei.cadpage.parsers.dispatch.DispatchA74Parser;

public class TNCampbellCountyBParser extends DispatchA74Parser {

  public TNCampbellCountyBParser() {
    super("CAMPBELL COUNTY", "TN");
  }

  @Override
  public String getFilter() {
    return "911cad@laxe911.net";
  }

  @Override
  public int getMapFlags() {
    return MAP_FLG_PREFER_GPS;
  }
}
