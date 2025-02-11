package net.anei.cadpage.parsers.FL;

import net.anei.cadpage.parsers.dispatch.DispatchA27Parser;

public class FLHardeeCountyAParser extends DispatchA27Parser {

  public FLHardeeCountyAParser() {
    super("HARDEE COUNTY", "FL");
  }

  @Override
  public String getFilter() {
    return "cadexport@hardeeso.com";
  }
}
