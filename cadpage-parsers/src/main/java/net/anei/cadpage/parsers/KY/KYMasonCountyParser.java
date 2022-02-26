package net.anei.cadpage.parsers.KY;

import net.anei.cadpage.parsers.dispatch.DispatchA74Parser;

public class KYMasonCountyParser extends DispatchA74Parser {

  public KYMasonCountyParser() {
    super("MASON COUNTY", "KY");
  }

  @Override
  public String getFilter() {
    return "dispatch@911comm3.info,@MaysvilleKYE911.com";
  }

  @Override
  public int getMapFlags() {
    return MAP_FLG_PREFER_GPS;
  }
}
