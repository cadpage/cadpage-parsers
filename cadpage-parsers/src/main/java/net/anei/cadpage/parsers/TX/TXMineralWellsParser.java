package net.anei.cadpage.parsers.TX;

import net.anei.cadpage.parsers.dispatch.DispatchA19Parser;

public class TXMineralWellsParser extends DispatchA19Parser {

  public TXMineralWellsParser() {
    super("MINERAL WELLS", "TX");
  }

  @Override
  public String getFilter() {
    return "dispatch@mineralwellstx.gov";
  }

  @Override
  public int getMapFlags() {
    return MAP_FLG_PREFER_GPS | MAP_FLG_SUPPR_LA;
  }
}
