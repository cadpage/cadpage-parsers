package net.anei.cadpage.parsers.IN;

import net.anei.cadpage.parsers.dispatch.DispatchA19Parser;

public class INMiamiCountyParser extends DispatchA19Parser {

  public INMiamiCountyParser() {
    super("MIAMI COUNTY", "IN");
  }

  @Override
  public String getFilter() {
    return "noreply@miamicountyin.gov,mcgsmtp@gmail.com";
  }

  @Override
  public int getMapFlags() {
    return MAP_FLG_PREFER_GPS;
  }
}
