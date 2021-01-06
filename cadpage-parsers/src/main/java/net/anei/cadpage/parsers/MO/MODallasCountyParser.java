package net.anei.cadpage.parsers.MO;

import net.anei.cadpage.parsers.dispatch.DispatchBCParser;


public class MODallasCountyParser extends DispatchBCParser {

  public MODallasCountyParser() {
    super("DALLAS COUNTY", "MO");
  }

  @Override
  public String getFilter() {
    return "NOREPLY@OMNIGO.COM";
  }
}
