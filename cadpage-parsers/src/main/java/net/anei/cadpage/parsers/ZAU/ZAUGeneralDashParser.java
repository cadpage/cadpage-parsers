package net.anei.cadpage.parsers.ZAU;

import net.anei.cadpage.parsers.general.GeneralParser;

public class ZAUGeneralDashParser extends GeneralParser {
  
  public ZAUGeneralDashParser() {
    super("-", CountryCode.AU);
  }
  
  @Override
  public String getLocName() {
    return "Generic (dash field separator)";
  }

}
