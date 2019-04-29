package net.anei.cadpage.parsers.IN;

import net.anei.cadpage.parsers.dispatch.DispatchSPKParser;

public class INStarkeCountyBParser extends DispatchSPKParser {
  
  public INStarkeCountyBParser() {
    super("STARKE COUNTY", "IN");
  }
  
  @Override
  public String getFilter() {
    return "dispatch75@co.starke.in.us";
  }

  @Override
  public String adjustMapAddress(String addr) {
    return INStarkeCountyParser.baseAdjustMapAddress(addr);
  }
}
