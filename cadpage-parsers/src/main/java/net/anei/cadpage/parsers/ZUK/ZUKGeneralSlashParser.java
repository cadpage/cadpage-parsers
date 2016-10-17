package net.anei.cadpage.parsers.ZUK;

import net.anei.cadpage.parsers.general.GeneralParser;

public class ZUKGeneralSlashParser extends GeneralParser {
  
  public ZUKGeneralSlashParser() {
    super("/", CountryCode.UK);
  }
  
  @Override
  public String getLocName() {
    return "Generic (slash field separator)";
  }
}
