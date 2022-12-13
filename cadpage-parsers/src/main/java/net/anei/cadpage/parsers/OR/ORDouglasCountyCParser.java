package net.anei.cadpage.parsers.OR;

import net.anei.cadpage.parsers.dispatch.DispatchA55Parser;

public class ORDouglasCountyCParser extends DispatchA55Parser {

  public ORDouglasCountyCParser() {
    super("DOUGLAS COUNTY", "OR");
  }

  @Override
  public String getFilter() {
    return "ereports@eforcesoftware.com";
  }

}
