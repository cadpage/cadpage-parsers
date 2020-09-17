package net.anei.cadpage.parsers.KY;

import net.anei.cadpage.parsers.dispatch.DispatchA74Parser;

public class KYMagoffinCountyParser extends DispatchA74Parser {
  
  public KYMagoffinCountyParser() {
    super("MAGOFFIN COUNTY", "KY");
  }
  
  @Override
  public String getFilter() {
    return "dispatch@MagoffinKY911.info";
  }

  @Override
  public int getMapFlags() {
    return MAP_FLG_PREFER_GPS;
  }
}
