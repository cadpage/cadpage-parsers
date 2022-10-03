package net.anei.cadpage.parsers.KS;

import net.anei.cadpage.parsers.dispatch.DispatchA25Parser;

public class KSNessCountyParser extends DispatchA25Parser {

  public KSNessCountyParser() {
    super("NESS COUNTY", "KS");
  }

  @Override
  public String getFilter() {
    return "nesssheriff@gbta.net";
  }
}
