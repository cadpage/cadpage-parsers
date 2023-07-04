package net.anei.cadpage.parsers.LA;

import net.anei.cadpage.parsers.dispatch.DispatchSPKParser;

public class LAWestFelicianaParishParser extends DispatchSPKParser {

  public LAWestFelicianaParishParser() {
    super("WEST FELICIANA PARISH", "LA");
  }

  @Override
  public String getFilter() {
    return "wfdisp@wfpso.org";
  }

  @Override
  public int getMapFlags() {
    return MAP_FLG_PREFER_GPS | MAP_FLG_SUPPR_LA;
  }
}
