package net.anei.cadpage.parsers.CO;

import net.anei.cadpage.parsers.dispatch.DispatchA59Parser;

public class CORioGrandeCountyBParser extends DispatchA59Parser {
  
  public CORioGrandeCountyBParser() {
    super("RIO GRANDE COUNTY", "CO");
  }
  
  @Override
  public String getFilter() {
    return "centerpd@gmx.com";
  }

}
