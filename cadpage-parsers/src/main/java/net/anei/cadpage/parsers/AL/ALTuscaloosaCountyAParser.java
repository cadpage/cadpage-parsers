package net.anei.cadpage.parsers.AL;

import net.anei.cadpage.parsers.dispatch.DispatchA19Parser;

public class ALTuscaloosaCountyAParser extends DispatchA19Parser {

  public ALTuscaloosaCountyAParser() {
    super("TUSCALOOSA COUNTY", "AL");
  }

  @Override
  public String getFilter() {
    return "batsignal@tuscaloosa.com";
  }

  @Override
  public int getMapFlags() {
    return MAP_FLG_PREFER_GPS;
  }
}
