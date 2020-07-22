package net.anei.cadpage.parsers.FL;

import net.anei.cadpage.parsers.dispatch.DispatchA27Parser;

public class FLHardeeCountyParser extends DispatchA27Parser {

  public FLHardeeCountyParser() {
    super("HARDEE COUNTY", "FL");
  }

  @Override
  public String getFilter() {
    return "cadexport@hardeeso.com";
  }
}
