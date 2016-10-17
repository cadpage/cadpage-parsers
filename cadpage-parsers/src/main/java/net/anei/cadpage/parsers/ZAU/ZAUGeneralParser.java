package net.anei.cadpage.parsers.ZAU;

import net.anei.cadpage.parsers.general.GeneralParser;

public class ZAUGeneralParser extends GeneralParser {

  /**
   * Default constructor
   */
  public ZAUGeneralParser() {
    super(CountryCode.AU);
  }
  
  @Override
  public String getLocName() {
    return "Generic Location";
  }
}
