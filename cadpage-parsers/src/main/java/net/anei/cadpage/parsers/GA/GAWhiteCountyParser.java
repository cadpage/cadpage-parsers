package net.anei.cadpage.parsers.GA;

import net.anei.cadpage.parsers.dispatch.DispatchA78Parser;

public class GAWhiteCountyParser extends DispatchA78Parser {

  public GAWhiteCountyParser() {
    super("WHITE COUNTY", "GA");
  }

  @Override
  public String getFilter() {
    return "donotreply@WHITECOUNTYPUBLICSAFETYalerts.com";
  }

  @Override
  public int getMapFlags() {
    return MAP_FLG_PREFER_GPS;
  }
}
