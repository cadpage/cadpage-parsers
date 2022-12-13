package net.anei.cadpage.parsers.OR;

import net.anei.cadpage.parsers.dispatch.DispatchA64Parser;

public class ORDouglasCountyBParser extends DispatchA64Parser {

  public ORDouglasCountyBParser() {
    super("DOUGLAS COUNTY", "OR");
  }

  @Override
  public String getFilter() {
    return "ereports@eforcesoftware.com";
  }

}
