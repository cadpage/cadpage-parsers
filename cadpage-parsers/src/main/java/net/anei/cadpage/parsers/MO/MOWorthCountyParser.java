package net.anei.cadpage.parsers.MO;

import net.anei.cadpage.parsers.dispatch.DispatchBCParser;

public class MOWorthCountyParser extends DispatchBCParser {
  
  public MOWorthCountyParser() {
    super("WORTH COUNTY", "MO");
  }
  
  @Override
  public String getFilter() {
    return "ncad@omnigo.com";
  }

}
