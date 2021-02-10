package net.anei.cadpage.parsers.SC;

import net.anei.cadpage.parsers.dispatch.DispatchSPKParser;

public class SCEdgefieldCountyParser extends DispatchSPKParser {

  public SCEdgefieldCountyParser() {
    super("EDGEFIELD COUNTY", "SC");
  }

  @Override
  public String getFilter() {
    return "cad@edgefieldcountysheriff.org";
  }

}
