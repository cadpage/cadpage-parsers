package net.anei.cadpage.parsers.KS;

import net.anei.cadpage.parsers.dispatch.DispatchSPKParser;

public class KSHarveyCountyBParser extends DispatchSPKParser {
  
  public KSHarveyCountyBParser() {
    super("HARVEY COUNTY", "KS");
  }
  
  @Override
  public String getFilter() {
    return "hvco911@gmail.com";
  }

}
