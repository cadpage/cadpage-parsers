package net.anei.cadpage.parsers.MO;

import net.anei.cadpage.parsers.dispatch.DispatchA33Parser;

public class MOMontgomeryCountyBParser extends DispatchA33Parser {

  public MOMontgomeryCountyBParser() {
    super("MONTGOMERY COUNTY", "MO");
  }

  @Override
  public String getFilter() {
    return "noreply@omnigo.com";
  }
}
