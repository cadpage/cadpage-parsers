package net.anei.cadpage.parsers.KY;

import net.anei.cadpage.parsers.dispatch.DispatchC05Parser;

public class KYHenryCountyCParser extends DispatchC05Parser {

  public KYHenryCountyCParser() {
    super("HENRY COUNTY", "KY");
  }

  @Override
  public String getFilter() {
    return "paging@10-8systems.com";
  }

}
