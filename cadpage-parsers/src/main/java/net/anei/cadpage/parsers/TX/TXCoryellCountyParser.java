package net.anei.cadpage.parsers.TX;

import net.anei.cadpage.parsers.dispatch.DispatchA20Parser;

public class TXCoryellCountyParser extends DispatchA20Parser {
  
  public TXCoryellCountyParser() {
    super("CORYELL COUNTY", "TX");
  }
  
  @Override
  public String getFilter() {
    return "cityofcopperascove@gmail.com";
  }

}
