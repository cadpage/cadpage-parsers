package net.anei.cadpage.parsers.AL;

import net.anei.cadpage.parsers.dispatch.DispatchA86Parser;

/**
 * Cherokee County, AL
 */
public class ALCherokeeCountyParser extends DispatchA86Parser {

  public ALCherokeeCountyParser() {
    super("CHEROKEE COUNTY", "AL");
  }

  @Override
  public String getFilter() {
    return "Dispatch@CherokeeALE911.net";
  }

  @Override
  public int getMapFlags() {
    return MAP_FLG_SUPPR_SR | MAP_FLG_PREFER_GPS;
  }
}
