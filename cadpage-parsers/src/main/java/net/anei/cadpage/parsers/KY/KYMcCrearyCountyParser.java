package net.anei.cadpage.parsers.KY;

import net.anei.cadpage.parsers.dispatch.DispatchA74Parser;

public class KYMcCrearyCountyParser extends DispatchA74Parser {

  public KYMcCrearyCountyParser() {
    super("MCCREARY COUNTY", "KY");
  }

  @Override
  public String getFilter() {
    return "dispatch@mccrearyky911.info";
  }

  @Override
  public int getMapFlags() {
    return MAP_FLG_PREFER_GPS;
  }

}
