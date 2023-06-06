package net.anei.cadpage.parsers.PA;

import net.anei.cadpage.parsers.dispatch.DispatchH04Parser;

public class PAJuniataCountyBParser extends DispatchH04Parser {

  public PAJuniataCountyBParser() {
    super("JUNIATA COUNTY", "PA");
  }

  @Override
  public String getFilter() {
    return "noreply@jcpc911.com";
  }

  @Override
  public int getMapFlags() {
    return MAP_FLG_PREFER_GPS;
  }
}
