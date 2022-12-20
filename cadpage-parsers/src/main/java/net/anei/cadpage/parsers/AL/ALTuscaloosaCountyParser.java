package net.anei.cadpage.parsers.AL;

import net.anei.cadpage.parsers.dispatch.DispatchA19Parser;

public class ALTuscaloosaCountyParser extends DispatchA19Parser {

  public ALTuscaloosaCountyParser() {
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
