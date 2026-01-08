package net.anei.cadpage.parsers.OK;

import net.anei.cadpage.parsers.dispatch.DispatchC04Parser;

public class OKCreekCountyParser extends DispatchC04Parser {

  public OKCreekCountyParser() {
    super("CREEK COUNTY", "OK");
  }
  
  @Override
  public String getFilter() {
    return "SOMS@CREEKCOUNTYSHERIFF.COM";
  }

  @Override
  public int getMapFlags() {
    return MAP_FLG_PREFER_GPS;
  }
}
