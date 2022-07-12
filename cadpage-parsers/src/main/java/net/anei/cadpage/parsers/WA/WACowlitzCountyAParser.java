package net.anei.cadpage.parsers.WA;

import net.anei.cadpage.parsers.dispatch.DispatchA19Parser;

public class WACowlitzCountyAParser extends DispatchA19Parser {
  
  public WACowlitzCountyAParser() {
    super("COWLITZ COUNTY", "WA");
  }

  @Override
  public String getFilter() {
    return "postmaster@cowlitz911.org";
  }
  
  @Override
  public int getMapFlags() {
    return MAP_FLG_PREFER_GPS;
  }

}
