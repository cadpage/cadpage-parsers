package net.anei.cadpage.parsers.TX;

import net.anei.cadpage.parsers.dispatch.DispatchA71Parser;


public class TXRobertsonCountyParser extends DispatchA71Parser {

  public TXRobertsonCountyParser() {
    super("ROBERTSON COUNTY", "TX");
  }

  public int getMapFlags() {
    return MAP_FLG_PREFER_GPS | MAP_FLG_SUPPR_LA;
  }
}
