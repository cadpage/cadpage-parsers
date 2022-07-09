package net.anei.cadpage.parsers.KS;

import net.anei.cadpage.parsers.dispatch.DispatchA19Parser;

public class KSFordCountyParser extends DispatchA19Parser {
  
  public KSFordCountyParser() {
    super("FORD COUNTY", "KS");
  }
  
  @Override
  public String getFilter() {
    return "@alert.active911.com,Paging@44-Control.net";
  }
}
