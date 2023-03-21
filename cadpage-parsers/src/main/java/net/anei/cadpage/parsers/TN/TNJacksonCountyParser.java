package net.anei.cadpage.parsers.TN;

import net.anei.cadpage.parsers.dispatch.DispatchA71Parser;

public class TNJacksonCountyParser extends DispatchA71Parser {
  
  public TNJacksonCountyParser() {
    super("JACKSON COUNTY", "TN");
  }
  
  
  @Override
  public int getMapFlags() {
    return MAP_FLG_PREFER_GPS;
  }

}
