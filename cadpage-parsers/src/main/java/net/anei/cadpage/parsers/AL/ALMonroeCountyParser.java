package net.anei.cadpage.parsers.AL;

import net.anei.cadpage.parsers.dispatch.DispatchA71Parser;

public class ALMonroeCountyParser extends DispatchA71Parser {

  public ALMonroeCountyParser() {
    super("MONROE COUNTY", "AL");
  }

  @Override
  public String getFilter() {
    return "cadinfo51@gmail.com";
  }

  @Override
  public int getMapFlags() {
    return MAP_FLG_PREFER_GPS;
  }

}
