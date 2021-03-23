package net.anei.cadpage.parsers.KY;

import net.anei.cadpage.parsers.dispatch.DispatchA27Parser;



public class KYCarrollCountyBParser extends DispatchA27Parser {

  public KYCarrollCountyBParser() {
    super("CARROLL COUNTY", "KY");
  }

  @Override
  public String getFilter() {
    return "noreply@cisusa.org,cad@carrolltonpd.net";
  }

  @Override
  public int getMapFlags() {
    return MAP_FLG_PREFER_GPS;
  }
}
