package net.anei.cadpage.parsers.TX;

import net.anei.cadpage.parsers.dispatch.DispatchA71Parser;

public class TXDallasCountyFParser extends DispatchA71Parser {

  public TXDallasCountyFParser() {
    super("DALLAS COUNTY", "TX");
  }

  @Override
  public int getMapFlags() {
    return MAP_FLG_PREFER_GPS | MAP_FLG_SUPPR_LA;
  }

}
