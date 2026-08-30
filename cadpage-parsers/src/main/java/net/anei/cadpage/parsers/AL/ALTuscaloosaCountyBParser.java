package net.anei.cadpage.parsers.AL;

import net.anei.cadpage.parsers.dispatch.DispatchC08Parser;

public class ALTuscaloosaCountyBParser extends DispatchC08Parser {

  public ALTuscaloosaCountyBParser() {
    super("TUSCALOOSA COUNTY", "AL");
  }

  @Override
  public String getFilter() {
    return "no_reply@northstarems.traumasoft.com";
  }

  @Override
  public int getMapFlags() {
    return MAP_FLG_PREFER_GPS;
  }

}
