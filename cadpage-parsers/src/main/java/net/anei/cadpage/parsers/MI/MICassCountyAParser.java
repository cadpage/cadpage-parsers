package net.anei.cadpage.parsers.MI;

import net.anei.cadpage.parsers.dispatch.DispatchA24Parser;

public class MICassCountyAParser extends DispatchA24Parser {

  public MICassCountyAParser() {
    super("CASS COUNTY", "MI");
  }

  @Override
  public String getFilter() {
    return "@traumasoft.com";
  }
}
