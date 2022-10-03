package net.anei.cadpage.parsers.KS;

import net.anei.cadpage.parsers.dispatch.DispatchA25Parser;

public class KSPhillipsCountyParser extends DispatchA25Parser {

  public KSPhillipsCountyParser() {
    super("PHILLIPS COUNTY", "KS");
  }

  @Override
  public String getFilter() {
    return "phillipstac@phillipsso.com";
  }
}
