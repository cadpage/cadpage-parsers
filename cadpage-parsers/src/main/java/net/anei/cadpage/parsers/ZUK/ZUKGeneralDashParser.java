package net.anei.cadpage.parsers.ZUK;

import net.anei.cadpage.parsers.general.GeneralParser;

public class ZUKGeneralDashParser extends GeneralParser {
  
  public ZUKGeneralDashParser() {
    super("-", CountryCode.UK);
  }
  
  @Override
  public String getLocName() {
    return "Generic (dash field separator)";
  }

}
