package net.anei.cadpage.parsers.ZAU;

import net.anei.cadpage.parsers.general.GeneralParser;

public class ZAUGeneralSlashParser extends GeneralParser {
  
  public ZAUGeneralSlashParser() {
    super("/", CountryCode.AU);
  }
  
  @Override
  public String getLocName() {
    return "Generic (slash field separator)";
  }
}
