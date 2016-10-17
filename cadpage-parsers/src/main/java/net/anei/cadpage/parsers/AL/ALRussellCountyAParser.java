package net.anei.cadpage.parsers.AL;

import net.anei.cadpage.parsers.dispatch.DispatchA19Parser;


public class ALRussellCountyAParser extends DispatchA19Parser {
  
  public ALRussellCountyAParser() {
    super("RUSSELL COUNTY", "AL");
  }
  
  @Override
  public String getFilter() {
    return "leecountyal@gmail.com,ripnrun@rusco911.com,account@ooma.com";
  }
}
