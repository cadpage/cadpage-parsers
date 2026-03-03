package net.anei.cadpage.parsers.OH;

import net.anei.cadpage.parsers.dispatch.DispatchA24Parser;

public class OHTrumbullCountyBParser extends DispatchA24Parser {

  public OHTrumbullCountyBParser() {
    super("TRUMBULL COUNTY", "OH");
  }

  @Override
  public String getFilter() {
    return "sunsrv@sundance-sys.com";
  }
}
