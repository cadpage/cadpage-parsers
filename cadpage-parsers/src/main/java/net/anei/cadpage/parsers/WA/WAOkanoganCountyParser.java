package net.anei.cadpage.parsers.WA;

import net.anei.cadpage.parsers.dispatch.DispatchA19Parser;

public class WAOkanoganCountyParser extends DispatchA19Parser {
  public WAOkanoganCountyParser() {
    super("OKANOGAN COUNTY", "WA");
  }

  @Override
  public String getFilter() {
    return "FRN-okanoganwa@email.getrave.com";
  }

  @Override
  public int getMapFlags() {
    return MAP_FLG_PREFER_GPS;
  }
}
