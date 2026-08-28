package net.anei.cadpage.parsers.TX;

import net.anei.cadpage.parsers.dispatch.DispatchA78Parser;

public class TXMadisonCountyParser extends DispatchA78Parser {

  public TXMadisonCountyParser() {
    super("MADISON COUNTY", "TX");
  }

  @Override
  public String getFilter() {
    return "donotreply@MADISONCOUNTYSHERIFFalerts.com";
  }

  @Override
  public int getMapFlags() {
    return MAP_FLG_PREFER_GPS | MAP_FLG_SUPPR_LA;
  }
}
