package net.anei.cadpage.parsers.KY;

import net.anei.cadpage.parsers.dispatch.DispatchC08Parser;

public class KYMcCrackenCountyCParser extends DispatchC08Parser {

  public KYMcCrackenCountyCParser() {
    super("MCCRACKEN COUNTY", "KY");
  }

  @Override
  public String getFilter() {
    return "no_reply@mercyregional.traumasoft.com";
  }

  @Override
  public int getMapFlags() {
    return MAP_FLG_PREFER_GPS;
  }
}
