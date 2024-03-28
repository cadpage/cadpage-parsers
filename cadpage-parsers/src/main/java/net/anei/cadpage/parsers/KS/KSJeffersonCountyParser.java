package net.anei.cadpage.parsers.KS;

import net.anei.cadpage.parsers.dispatch.DispatchA25Parser;

public class KSJeffersonCountyParser extends DispatchA25Parser {

  public KSJeffersonCountyParser() {
    super("JEFFERSON COUNTY", "KS");
  }

  @Override
  public String getFilter() {
    return "enterpol@jfcountyks.com";
  }

}
