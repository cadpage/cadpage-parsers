package net.anei.cadpage.parsers.MO;

import net.anei.cadpage.parsers.dispatch.DispatchA96Parser;

public class MOCrawfordCountyBParser extends DispatchA96Parser {

  public MOCrawfordCountyBParser() {
    super("CRAWFORD COUNTY", "MO");
  }

  @Override
  public String getFilter() {
    return "crawfordco@omnigo.com";
  }
}