package net.anei.cadpage.parsers.MO;

import net.anei.cadpage.parsers.dispatch.DispatchBCParser;

public class MOBollingerCountyParser extends DispatchBCParser {

  public MOBollingerCountyParser() {
    super("BOLLINGER COUNTY", "MO");
  }

  @Override
  public String getFilter() {
    return "noreply@omnigo.com";
  }
}
