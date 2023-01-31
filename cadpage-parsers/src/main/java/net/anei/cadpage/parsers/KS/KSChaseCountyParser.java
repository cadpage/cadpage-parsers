package net.anei.cadpage.parsers.KS;

import net.anei.cadpage.parsers.dispatch.DispatchBCParser;

public class KSChaseCountyParser extends DispatchBCParser {

  public KSChaseCountyParser() {
    super("CHASE COUNTY", "KS");
  }

  @Override
  public String getFilter() {
    return "noreply@omnigo.com";
  }

}
