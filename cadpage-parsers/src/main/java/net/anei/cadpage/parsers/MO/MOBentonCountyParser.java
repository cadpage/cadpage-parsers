package net.anei.cadpage.parsers.MO;

import net.anei.cadpage.parsers.dispatch.DispatchBCParser;


public class MOBentonCountyParser extends DispatchBCParser {
  public MOBentonCountyParser() {
    super("BENTON COUNTY", "MO");
  }
   
  @Override
  public String getFilter() {
    return "911.ADMIN@BENTONCOMO.COM";
  }
}
