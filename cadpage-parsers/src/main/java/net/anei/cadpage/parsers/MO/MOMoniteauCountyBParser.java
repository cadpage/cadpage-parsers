package net.anei.cadpage.parsers.MO;

import net.anei.cadpage.parsers.dispatch.DispatchBCParser;

public class MOMoniteauCountyBParser extends DispatchBCParser {

  public MOMoniteauCountyBParser() {
    super("MONITEAU COUNTY", "MO");
  }

  @Override
  public String getFilter() {
    return "noreply@omnigo.com";
  }
}
