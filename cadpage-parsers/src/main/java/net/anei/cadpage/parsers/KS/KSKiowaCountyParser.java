package net.anei.cadpage.parsers.KS;

import net.anei.cadpage.parsers.dispatch.DispatchA25Parser;

public class KSKiowaCountyParser extends DispatchA25Parser {

  public KSKiowaCountyParser() {
    super("KIOWA COUNTY", "SD");
  }

  @Override
  public String getFilter() {
    return "Dispatch@kiowacountyks.org";
  }

}
