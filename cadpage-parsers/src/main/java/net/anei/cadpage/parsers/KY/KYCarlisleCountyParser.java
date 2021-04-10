package net.anei.cadpage.parsers.KY;

import net.anei.cadpage.parsers.dispatch.DispatchA74Parser;

public class KYCarlisleCountyParser extends DispatchA74Parser {

  public KYCarlisleCountyParser() {
    super("CARLISLE COUNTY", "KY");
  }

  @Override
  public String getFilter() {
    return "Dispatch@CarlisleKY911.info";
  }
}
