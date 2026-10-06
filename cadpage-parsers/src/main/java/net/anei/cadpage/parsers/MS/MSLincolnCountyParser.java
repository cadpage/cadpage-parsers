package net.anei.cadpage.parsers.MS;

import net.anei.cadpage.parsers.dispatch.DispatchC14Parser;

public class MSLincolnCountyParser extends DispatchC14Parser {

  public MSLincolnCountyParser() {
    super("LINCOLN COUNTY", "MS");
  }

  @Override
  public String getFilter() {
    return "cadalerts@alerts.adsisoftware.com";
  }
}
