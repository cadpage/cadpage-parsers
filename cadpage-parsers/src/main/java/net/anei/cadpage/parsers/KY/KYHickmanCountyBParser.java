package net.anei.cadpage.parsers.KY;

import net.anei.cadpage.parsers.dispatch.DispatchC05Parser;

public class KYHickmanCountyBParser extends DispatchC05Parser {

  public KYHickmanCountyBParser() {
    super("HICKMAN COUNTY", "KY");
  }

  @Override
  public String getFilter() {
    return "paging@10-8systems.com";
  }
}
