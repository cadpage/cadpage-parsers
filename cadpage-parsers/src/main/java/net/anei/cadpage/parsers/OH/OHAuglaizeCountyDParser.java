package net.anei.cadpage.parsers.OH;

import net.anei.cadpage.parsers.dispatch.DispatchA24Parser;

public class OHAuglaizeCountyDParser extends DispatchA24Parser {

  public OHAuglaizeCountyDParser() {
    super("AUGLAIZE COUNTY", "OH");
  }

  @Override
  public String getFilter() {
    return "sunsrv@sundance-sys.com";
  }
}
