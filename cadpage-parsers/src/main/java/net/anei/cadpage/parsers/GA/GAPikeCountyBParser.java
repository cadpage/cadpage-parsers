package net.anei.cadpage.parsers.GA;

import net.anei.cadpage.parsers.dispatch.DispatchSPKParser;

public class GAPikeCountyBParser extends DispatchSPKParser {
  
  public GAPikeCountyBParser() {
    super("PIKE COUNTY", "GA");
  }
  
  @Override
  public String getFilter() {
    return "e911cadservice@pikecoes.com";
  }

}
