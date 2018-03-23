package net.anei.cadpage.parsers.general;

import net.anei.cadpage.parsers.TX.TXBurnetCountyParser;

public class XXAcadianAmbulanceParser extends TXBurnetCountyParser {
  
  public XXAcadianAmbulanceParser(String defState) {
    super("", defState);
  }
  
  @Override
  public String getAliasCode() {
    return "XXAcadianAmbulance";
  }

  @Override
  public String getFilter() {
    return "bastropactive911@gmail.com";
  }
}
