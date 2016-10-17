package net.anei.cadpage.parsers.NC;

import net.anei.cadpage.parsers.dispatch.DispatchSouthernParser;

public class NCChowanCountyParser extends DispatchSouthernParser {
  
  public NCChowanCountyParser() {
    super(CITY_LIST, "CHOWAN COUNTY", "NC", DSFLAG_DISPATCH_ID | DSFLAG_ID_OPTIONAL | DSFLAG_FOLLOW_CROSS);
  }
  
  private static final String[] CITY_LIST = new String[]{
    "EDENTON",
    "ROCKYHOCK",
    "SELWIN",
    "SIGN PINE",
    "TYNER"
  };
}
