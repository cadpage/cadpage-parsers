package net.anei.cadpage.parsers.KY;

import net.anei.cadpage.parsers.dispatch.DispatchA24Parser;

public class KYLetcherCountyParser extends DispatchA24Parser {

  public KYLetcherCountyParser() {
    super("LETCHER COUNTY", "KY");
  }

  @Override
  public String getFilter() {
    return "paging@10-8systems.com";
  }

}
