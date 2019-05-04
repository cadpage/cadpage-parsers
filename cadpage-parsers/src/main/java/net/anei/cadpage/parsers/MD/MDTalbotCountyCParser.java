package net.anei.cadpage.parsers.MD;

import net.anei.cadpage.parsers.dispatch.DispatchSPKParser;

public class MDTalbotCountyCParser extends DispatchSPKParser {
  
  public MDTalbotCountyCParser() {
    super("TALBOT COUNTY", "MD");
  }
  
  @Override
  public String getFilter() {
    return "talbot.notifications@gmail.com";
  }
}
