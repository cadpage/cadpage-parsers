package net.anei.cadpage.parsers.FL;

import net.anei.cadpage.parsers.dispatch.DispatchA52Parser;

public class FLCitrusCountyBParser extends DispatchA52Parser {

  public  FLCitrusCountyBParser() {
    super("CITRUS COUNTY", "FL");
  }

  @Override
  public String getFilter() {
    return "alerts@citruscountyfire.com";
  }

}
