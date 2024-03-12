package net.anei.cadpage.parsers.KY;

import net.anei.cadpage.parsers.dispatch.DispatchA71Parser;

public class KYHardinCountyEParser extends DispatchA71Parser {

  public KYHardinCountyEParser() {
    super("HARDIN COUNTY", "KY");
  }

  @Override
  public String getFilter() {
    return "paging@10-8systems.com";
  }
}
