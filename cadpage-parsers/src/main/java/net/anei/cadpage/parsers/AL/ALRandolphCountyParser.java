package net.anei.cadpage.parsers.AL;

import net.anei.cadpage.parsers.dispatch.DispatchA74Parser;

public class ALRandolphCountyParser extends DispatchA74Parser {
  
  public ALRandolphCountyParser() {
    super("RANDOLPH COUNTY", "AL");
  }
  
  @Override
  public String getFilter() {
    return "Dispatch@RandolphAL911.info";
  }

}
