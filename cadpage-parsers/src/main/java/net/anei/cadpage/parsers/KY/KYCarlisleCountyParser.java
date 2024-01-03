package net.anei.cadpage.parsers.KY;

import net.anei.cadpage.parsers.dispatch.DispatchA24Parser;

public class KYCarlisleCountyParser extends DispatchA24Parser {

  public KYCarlisleCountyParser() {
    super("CARLISLE COUNTY", "KY");
  }

  @Override
  public String getFilter() {
    return "paging@10-8systems.com";
  }
}
