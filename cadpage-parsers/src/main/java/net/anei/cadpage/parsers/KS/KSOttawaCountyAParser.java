package net.anei.cadpage.parsers.KS;

import net.anei.cadpage.parsers.dispatch.DispatchA33Parser;

public class KSOttawaCountyAParser extends DispatchA33Parser {

  public KSOttawaCountyAParser() {
    super("OTTAWA COUNTY", "KS");
  }

  @Override
  public String getFilter() {
    return "noreply@omnigo.com";
  }
}
