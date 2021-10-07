package net.anei.cadpage.parsers.TN;

import net.anei.cadpage.parsers.dispatch.DispatchA74Parser;

public class TNClaiborneCountyParser extends DispatchA74Parser {

  public TNClaiborneCountyParser() {
    super("CLAIBORNE COUNTY", "TN");
  }

  @Override
  public String getFilter() {
    return "dispatch@911comm1.info,Dispatch@ClaiborneTNE911.net";
  }

  @Override
  public int getMapFlags() {
    return MAP_FLG_PREFER_GPS;
  }
}
