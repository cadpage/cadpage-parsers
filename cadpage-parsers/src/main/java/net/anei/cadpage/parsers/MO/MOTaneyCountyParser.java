package net.anei.cadpage.parsers.MO;

import net.anei.cadpage.parsers.dispatch.DispatchBCParser;

public class MOTaneyCountyParser extends DispatchBCParser {
  
  public MOTaneyCountyParser() {
    super("TANEY COUNTY", "MO");
  }
  
  @Override
  public String getFilter() {
    return "taneyco@omnigo.com";
  }

}
