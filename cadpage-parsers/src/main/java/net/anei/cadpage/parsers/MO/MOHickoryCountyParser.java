package net.anei.cadpage.parsers.MO;

import net.anei.cadpage.parsers.dispatch.DispatchBCParser;

public class MOHickoryCountyParser extends DispatchBCParser {

  public MOHickoryCountyParser() {
    super("HICKORY COUNTY", "MO");
  }

  @Override
  public String getFilter() {
    return "noreply@omnigo.com";
  }
}
