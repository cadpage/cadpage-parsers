package net.anei.cadpage.parsers.KS;

import net.anei.cadpage.parsers.dispatch.DispatchA25Parser;

public class KSGreeleyCountyParser extends DispatchA25Parser {

  public KSGreeleyCountyParser() {
    super("GREELEY COUNTY", "KS");
  }

  @Override
  public String getFilter() {
    return "alerts@fairpoint.net";
  }

}
