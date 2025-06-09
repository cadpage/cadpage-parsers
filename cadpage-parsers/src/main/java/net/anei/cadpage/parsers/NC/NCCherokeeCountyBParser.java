package net.anei.cadpage.parsers.NC;

import net.anei.cadpage.parsers.dispatch.DispatchA71Parser;

public class NCCherokeeCountyBParser extends DispatchA71Parser {

  public NCCherokeeCountyBParser() {
    super("CHEROKEE COUNTY", "NC");
  }

  @Override
  public String getFilter() {
    return "Do-Not-Reply-Dispatch@cherokeecounty-nc.gov";
  }
}
