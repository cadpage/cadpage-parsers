package net.anei.cadpage.parsers.KY;

import net.anei.cadpage.parsers.dispatch.DispatchB3Parser;

public class KYBallardCountyParser extends DispatchB3Parser {
  
  public KYBallardCountyParser() {
    super("BALLARD COUNTY E911:", CITY_LIST, "BALLARD COUNTY", "KY", B2_FORCE_CALL_CODE);
  }
  
  @Override
  public String getFilter() {
    return "BALLARD COUNTY E911@BRTC.NET";
  }

  private static final String[] CITY_LIST = new String[]{
      
      // Cities
      "BARLOW",
      "BLANDVILLE",
      "KEVIL",
      "LACENTER",
      "WICKLIFFE",

      // Census-designated places
      "BANDANA",
      "LOVELACEVILLE",

      // Other unincorporated communities
      "MONKEYS EYEBROW",
      "NEW YORK"


  };
}
