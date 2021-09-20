package net.anei.cadpage.parsers.KS;

import net.anei.cadpage.parsers.dispatch.DispatchA25Parser;

public class KSWilsonCountyParser extends DispatchA25Parser {

  public KSWilsonCountyParser() {
    super("WILSON COUNTY", "KS");
  }

  @Override
  public String getFilter() {
    return "wilco911@twinmounds.com";
  }

}
