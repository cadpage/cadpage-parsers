package net.anei.cadpage.parsers.KS;

import net.anei.cadpage.parsers.dispatch.DispatchA25Parser;

public class KSWabaunseeCountyParser extends DispatchA25Parser {

  public KSWabaunseeCountyParser() {
    super("WABAUNSEE COUNTY", "KS");
  }

  @Override
  public String getFilter() {
    return "wbso@wbcso.org";
  }
}
