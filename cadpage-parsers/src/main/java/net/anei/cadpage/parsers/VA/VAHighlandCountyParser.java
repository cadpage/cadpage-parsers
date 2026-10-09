package net.anei.cadpage.parsers.VA;

import net.anei.cadpage.parsers.dispatch.DispatchSPKParser;

public class VAHighlandCountyParser extends DispatchSPKParser {

  public VAHighlandCountyParser() {
    super("HIGHLAND COUNTY", "VA");
  }

  @Override
  public String getFilter() {
    return "noreply@public-safety-cloud.com";
  }
}
