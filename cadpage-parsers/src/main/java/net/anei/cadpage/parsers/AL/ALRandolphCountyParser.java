package net.anei.cadpage.parsers.AL;

import net.anei.cadpage.parsers.dispatch.DispatchA86Parser;

public class ALRandolphCountyParser extends DispatchA86Parser {
  
  public ALRandolphCountyParser() {
    super("RANDOLPH COUNTY", "AL");
  }
  
  @Override
  public String getFilter() {
    return "Dispatch@RandolphAL911.info";
  }

}
