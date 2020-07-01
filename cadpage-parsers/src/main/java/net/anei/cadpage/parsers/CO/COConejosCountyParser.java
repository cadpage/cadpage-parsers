package net.anei.cadpage.parsers.CO;

import net.anei.cadpage.parsers.dispatch.DispatchPremierOneParser;

public class COConejosCountyParser extends DispatchPremierOneParser {

  public COConejosCountyParser() {
    super("CONEJOS COUNTY", "CO");
  }

  @Override
  public String getFilter() {
    return "AL@csp.noreply";
  }

}
