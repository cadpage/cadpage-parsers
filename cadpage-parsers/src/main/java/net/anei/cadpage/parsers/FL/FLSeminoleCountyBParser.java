package net.anei.cadpage.parsers.FL;

import net.anei.cadpage.parsers.dispatch.DispatchA27Parser;

public class FLSeminoleCountyBParser extends DispatchA27Parser {

  public FLSeminoleCountyBParser() {
    super("SEMINOLE COUNTY", "FL");
  }

  @Override
  public String getFilter() {
    return "noreply@cisusa.org";
  }

}
