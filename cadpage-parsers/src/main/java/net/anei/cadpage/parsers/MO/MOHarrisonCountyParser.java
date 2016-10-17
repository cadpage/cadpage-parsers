package net.anei.cadpage.parsers.MO;

import net.anei.cadpage.parsers.dispatch.DispatchBCParser;


public class MOHarrisonCountyParser extends DispatchBCParser {
  public MOHarrisonCountyParser() {
    super("HARRISON COUNTY", "MO");
  }
   
  @Override
  public String getFilter() {
    return "@grm.net";
  }
}
