package net.anei.cadpage.parsers.MO;

import net.anei.cadpage.parsers.dispatch.DispatchA27Parser;



public class MOGasconadeCountyAParser extends DispatchA27Parser {

  public MOGasconadeCountyAParser() {
    super("GASCONADE COUNTY", "MO");
  }

  @Override
  public String getFilter() {
    return "gc911text@gasconadecounty911.com";
  }

  @Override
  public int getMapFlags() {
    return MAP_FLG_PREFER_GPS;
  }
}
