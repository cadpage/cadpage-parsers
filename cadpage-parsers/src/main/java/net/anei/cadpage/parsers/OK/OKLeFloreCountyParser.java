package net.anei.cadpage.parsers.OK;

import net.anei.cadpage.parsers.dispatch.DispatchA95Parser;

public class OKLeFloreCountyParser extends DispatchA95Parser {

  public OKLeFloreCountyParser() {
    super("LEFLORE COUNTY", "OK");
  }

  @Override
  public String getFilter() {
    return "no-reply@csprosuite.centralsquarecloudgov.com,LeFlore@mobile-text-alerts.com";
  }
}
