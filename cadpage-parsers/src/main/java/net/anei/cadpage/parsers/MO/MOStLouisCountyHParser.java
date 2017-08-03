package net.anei.cadpage.parsers.MO;

import net.anei.cadpage.parsers.dispatch.DispatchA33Parser;


public class MOStLouisCountyHParser extends DispatchA33Parser {
  
  
  public MOStLouisCountyHParser() {
    super("ST LOUIS COUNTY", "MO");
  }
  
  @Override
  public String getFilter() {
    return "RECORDS@BRIDGETONMO.COM";
  }
}