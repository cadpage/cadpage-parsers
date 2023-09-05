package net.anei.cadpage.parsers.GA;

import net.anei.cadpage.parsers.dispatch.DispatchH03Parser;

public class GACherokeeCountyBParser extends DispatchH03Parser {

  public GACherokeeCountyBParser() {
    super("CHEROKEE COUNTY", "GA");
  }

  @Override
  public String getFilter() {
    return ".911@cherokeega.com";
  }
}
