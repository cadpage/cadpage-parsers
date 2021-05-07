package net.anei.cadpage.parsers.SD;

import net.anei.cadpage.parsers.dispatch.DispatchA25Parser;

public class SDKiowaCountyParser extends DispatchA25Parser {

  public SDKiowaCountyParser() {
    super("KIOWA COUNTY", "SD");
  }

  @Override
  public String getFilter() {
    return "Dispatch@kiowacountyks.org";
  }

}
