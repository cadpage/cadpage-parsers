package net.anei.cadpage.parsers.NC;

import net.anei.cadpage.parsers.dispatch.DispatchA24Parser;

public class NCGuilfordCountyDParser extends DispatchA24Parser {

  public NCGuilfordCountyDParser() {
    super("GUILFORD COUNTY", "NC");
  }

  @Override
  public String getFilter() {
    return "911@fmisolutions.com";
  }

}
