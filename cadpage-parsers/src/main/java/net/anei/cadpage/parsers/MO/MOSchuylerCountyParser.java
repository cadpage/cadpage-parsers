package net.anei.cadpage.parsers.MO;

import net.anei.cadpage.parsers.dispatch.DispatchBCParser;

public class MOSchuylerCountyParser extends DispatchBCParser {
  public MOSchuylerCountyParser() {
    super("SCHUYLER COUNTY", "MO");
  }

  @Override
  public String getFilter() {
    return "noreply@omnigo.com";
  }

}
