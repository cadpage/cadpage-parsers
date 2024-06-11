package net.anei.cadpage.parsers.MO;

import net.anei.cadpage.parsers.dispatch.DispatchA96Parser;

public class MOScottCountyEParser extends DispatchA96Parser {

  public MOScottCountyEParser() {
    super("SCOTT COUNTY", "MO");
  }

  @Override
  public String getFilter() {
    return "noreply@omnigo.com";
  }
}
