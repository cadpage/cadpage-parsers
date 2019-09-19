package net.anei.cadpage.parsers.KY;

import net.anei.cadpage.parsers.dispatch.DispatchA71Parser;

public class KYMartinCountyParser extends DispatchA71Parser {
  
  public KYMartinCountyParser() {
    super("MARTIN COUNTY", "KY");
  }
  
  @Override
  public int getMapFlags() {
    return MAP_FLG_PREFER_GPS;
  }

}
