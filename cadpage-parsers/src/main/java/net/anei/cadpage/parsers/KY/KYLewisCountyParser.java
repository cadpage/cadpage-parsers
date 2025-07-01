package net.anei.cadpage.parsers.KY;

import net.anei.cadpage.parsers.dispatch.DispatchA74Parser;

public class KYLewisCountyParser extends DispatchA74Parser {

  public KYLewisCountyParser() {
    super("LEWIS COUNTY", "KY");
  }

  @Override
  public String getFilter() {
    return "dispatch@LewisKYE911.org";
  }

  @Override
  public int getMapFlags() {
    return MAP_FLG_PREFER_GPS;
  }
}
