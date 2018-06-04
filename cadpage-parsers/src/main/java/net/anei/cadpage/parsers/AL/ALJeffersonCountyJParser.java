package net.anei.cadpage.parsers.AL;

import net.anei.cadpage.parsers.dispatch.DispatchA71Parser;

public class ALJeffersonCountyJParser extends DispatchA71Parser {
    
  public ALJeffersonCountyJParser() {
    super("JEFFERSON COUNTY", "AL"); 
  }
  
  @Override
  public String getFilter() {
    return "administrator@trussville.org";
  }
}
