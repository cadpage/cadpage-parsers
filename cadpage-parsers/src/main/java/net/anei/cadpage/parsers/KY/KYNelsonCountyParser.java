package net.anei.cadpage.parsers.KY;

import net.anei.cadpage.parsers.dispatch.DispatchC05Parser;

public class KYNelsonCountyParser extends DispatchC05Parser {

  public KYNelsonCountyParser() {
    super("NELSON COUNTY", "KY");
  }

  @Override
  public String getFilter() {
    return "paging@10-8systems.com";
  }
}
