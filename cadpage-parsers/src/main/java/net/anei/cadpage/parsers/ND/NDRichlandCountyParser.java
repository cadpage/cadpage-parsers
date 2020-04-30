package net.anei.cadpage.parsers.ND;

import net.anei.cadpage.parsers.dispatch.DispatchA27Parser;

public class NDRichlandCountyParser extends DispatchA27Parser {
  
  public NDRichlandCountyParser() {
    super("RICHLAND COUNTY", "ND");
  }
  
  @Override
  public String getFilter() {
    return "noreply@cis.com";
  }

}
