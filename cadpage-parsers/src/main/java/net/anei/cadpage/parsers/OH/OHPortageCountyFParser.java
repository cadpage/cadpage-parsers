package net.anei.cadpage.parsers.OH;

import net.anei.cadpage.parsers.dispatch.DispatchA9Parser;

public class OHPortageCountyFParser extends DispatchA9Parser {

  public OHPortageCountyFParser() {
    super("PORTAGE COUNTY", "OH");
  }

  @Override
  public String getFilter() {
    return "dispatch@kent.edu";
  }

}
