package net.anei.cadpage.parsers.KY;

import net.anei.cadpage.parsers.dispatch.DispatchC05Parser;

public class KYTrimbleCountyCParser extends DispatchC05Parser {
  
  public KYTrimbleCountyCParser() {
    super("TRIMBLE COUNTY", "KY");
  }

  @Override
  public String getFilter() {
    return "paging@10-8systems.com";
  }
}
