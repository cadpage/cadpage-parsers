package net.anei.cadpage.parsers.WV;

import net.anei.cadpage.parsers.dispatch.DispatchSPKParser;

public class WVJacksonCountyParser extends DispatchSPKParser {

  public WVJacksonCountyParser() {
    super("JACKSON COUNTY", "WV");
  }

  @Override
  public String getFilter() {
    return "jacksoncowv911@gmail.com,911Center@Jacksonwv911.com";
  }

}
