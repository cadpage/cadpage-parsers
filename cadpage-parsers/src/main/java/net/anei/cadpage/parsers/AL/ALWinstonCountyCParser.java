package net.anei.cadpage.parsers.AL;

import net.anei.cadpage.parsers.dispatch.DispatchA24Parser;

public class ALWinstonCountyCParser extends DispatchA24Parser {

  public ALWinstonCountyCParser() {
    super("WINSTON COUNTY", "AL");
  }

  @Override
  public String getFilter() {
    return "winstondispatch@digitalrms.com";
  }
}
