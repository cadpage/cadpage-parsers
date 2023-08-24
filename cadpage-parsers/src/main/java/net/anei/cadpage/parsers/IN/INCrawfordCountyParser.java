package net.anei.cadpage.parsers.IN;

import net.anei.cadpage.parsers.dispatch.DispatchA24Parser;

public class INCrawfordCountyParser extends DispatchA24Parser {

  public INCrawfordCountyParser() {
    super("CRAWFORD COUNTY", "IN");
  }

  @Override
  public String getFilter() {
    return "paging@10-8systems.com";
  }

}
