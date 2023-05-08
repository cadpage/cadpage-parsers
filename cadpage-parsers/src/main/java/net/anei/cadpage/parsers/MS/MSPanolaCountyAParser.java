package net.anei.cadpage.parsers.MS;

import net.anei.cadpage.parsers.dispatch.DispatchA74Parser;

public class MSPanolaCountyAParser extends DispatchA74Parser {

  public MSPanolaCountyAParser() {
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
