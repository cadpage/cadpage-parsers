package net.anei.cadpage.parsers.CO;

import net.anei.cadpage.parsers.dispatch.DispatchA55Parser;

public class CORioGrandeCountyBParser extends DispatchA55Parser {

  public CORioGrandeCountyBParser() {
    super("RIO GRANDE COUNTY", "CO");
  }

  @Override
  public String getFilter() {
    return "ereports@eforcesoftware.com";
  }

}
