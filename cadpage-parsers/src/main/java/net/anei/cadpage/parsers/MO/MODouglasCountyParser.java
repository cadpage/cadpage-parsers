package net.anei.cadpage.parsers.MO;

import net.anei.cadpage.parsers.dispatch.DispatchBCParser;

public class MODouglasCountyParser extends DispatchBCParser {

  public MODouglasCountyParser() {
    super("DOUGLAS COUNTY", "MO");
  }

  @Override
  public String getFilter() {
    return "noreply@omnigo.com";
  }
}
