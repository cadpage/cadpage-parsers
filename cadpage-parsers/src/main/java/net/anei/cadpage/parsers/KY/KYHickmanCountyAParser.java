package net.anei.cadpage.parsers.KY;

import net.anei.cadpage.parsers.dispatch.DispatchA24Parser;

public class KYHickmanCountyAParser extends DispatchA24Parser {

  public KYHickmanCountyAParser() {
    super("HICKMAN COUNTY", "KY");
  }

  @Override
  public String getFilter() {
    return "paging@10-8systems.com";
  }

}
