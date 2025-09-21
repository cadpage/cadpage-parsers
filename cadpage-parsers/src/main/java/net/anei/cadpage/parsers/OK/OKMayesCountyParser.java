package net.anei.cadpage.parsers.OK;

import net.anei.cadpage.parsers.dispatch.DispatchA95Parser;


public class OKMayesCountyParser extends DispatchA95Parser {

  public OKMayesCountyParser() {
    super("MAYES COUNTY", "OK");
  }

  @Override
  public String getFilter() {
    return "911@mayes.okcounties.org";
  }
}
