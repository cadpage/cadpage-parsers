package net.anei.cadpage.parsers.OK;

import net.anei.cadpage.parsers.dispatch.DispatchA19Parser;

public class OKStephensCountyParser extends DispatchA19Parser {

  public OKStephensCountyParser() {
    super("STEPHENS COUNTY", "OK");
  }

  @Override
  public String getFilter() {
    return "rapid.notifications@duncanok.gov";
  }

}
