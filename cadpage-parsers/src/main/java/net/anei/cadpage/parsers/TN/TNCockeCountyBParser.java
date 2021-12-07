package net.anei.cadpage.parsers.TN;

import net.anei.cadpage.parsers.dispatch.DispatchA86Parser;

public class TNCockeCountyBParser extends DispatchA86Parser {

  public TNCockeCountyBParser() {
    super("COCKE COUNTY", "TN");
  }

  @Override
  public String getFilter() {
    return "dispatch@CockeTNE911.net";
  }
}
