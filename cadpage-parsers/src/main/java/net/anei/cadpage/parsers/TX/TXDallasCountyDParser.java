package net.anei.cadpage.parsers.TX;

import net.anei.cadpage.parsers.dispatch.DispatchA53Parser;

public class TXDallasCountyDParser extends DispatchA53Parser {
  
  public TXDallasCountyDParser() {
    super("DALLAS COUNTY", "TX");
  }
  
  @Override
  public String getFilter() {
    return "Dispatch@glennheightstx.gov";
  }

}
