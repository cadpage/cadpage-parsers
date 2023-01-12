package net.anei.cadpage.parsers.OK;

import net.anei.cadpage.parsers.dispatch.DispatchA24Parser;

public class OKMajorCountyParser extends DispatchA24Parser {

  public OKMajorCountyParser() {
    super("MAJOR COUNTY", "OK");
  }

  @Override
  public String getFilter() {
    return "paging@10-8systems.com";
  }

}
