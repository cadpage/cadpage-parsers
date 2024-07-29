package net.anei.cadpage.parsers.AL;

import net.anei.cadpage.parsers.dispatch.DispatchA74Parser;

public class ALLamarCountyParser extends DispatchA74Parser {

  public ALLamarCountyParser() {
    super("LAMAR COUNTY", "AL");
  }

  @Override
  public String getFilter() {
    return "dispatch@911comm1.info";
  }

}
