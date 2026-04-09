package net.anei.cadpage.parsers.KS;

import net.anei.cadpage.parsers.dispatch.DispatchA25Parser;

public class KSHarperCountyParser extends DispatchA25Parser {

  public KSHarperCountyParser() {
    super("HARPER COUNTY", "KS");
  }

  @Override
  public String getFilter() {
    return "harpercountyks@gmail.com";
  }
}
