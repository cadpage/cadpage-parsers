package net.anei.cadpage.parsers.TN;

import net.anei.cadpage.parsers.dispatch.DispatchA74Parser;

public class TNCockeCountyBParser extends DispatchA74Parser {

  public TNCockeCountyBParser() {
    super("COCKE COUNTY", "TN");
  }

  @Override
  public String getFilter() {
    return "dispatch@911comm2.info";
  }
}
