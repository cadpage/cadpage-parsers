package net.anei.cadpage.parsers.KY;

import net.anei.cadpage.parsers.dispatch.DispatchA74Parser;

public class KYEdmonsonCountyBParser extends DispatchA74Parser {

  public KYEdmonsonCountyBParser() {
    super("EDMONSON COUNTY", "KY");
  }

  @Override
  public String getFilter() {
    return "dispatch@edmonsoncoe911.info";
  }

  @Override
  public int getMapFlags() {
    return MAP_FLG_PREFER_GPS;
  }

}
