package net.anei.cadpage.parsers.KS;

import net.anei.cadpage.parsers.dispatch.DispatchA33Parser;

public class KSCrawfordCountyBParser extends DispatchA33Parser {


  public KSCrawfordCountyBParser() {
    super("CRAWFORD COUNTY", "KS");
  }
  
  @Override
  public String getFilter() {
    return "DISPATCH@FRONTKS.COM";
  }
}
