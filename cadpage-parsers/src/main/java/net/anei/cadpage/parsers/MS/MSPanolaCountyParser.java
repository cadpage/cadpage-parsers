package net.anei.cadpage.parsers.MS;

import net.anei.cadpage.parsers.dispatch.DispatchA74Parser;

public class MSPanolaCountyParser extends DispatchA74Parser {

  public MSPanolaCountyParser() {
    super("PANOLA COUNTY", "MS");
  }

  @Override
  public String getFilter() {
    return "dispatch@911comm3.info,dispatch@PanolaCoE911.com";
  }

  @Override
  public int getMapFlags() {
    return MAP_FLG_SUPPR_LA | MAP_FLG_PREFER_GPS;
  }

}
