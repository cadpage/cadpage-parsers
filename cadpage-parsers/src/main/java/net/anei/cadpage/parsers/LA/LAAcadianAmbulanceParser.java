package net.anei.cadpage.parsers.LA;

import net.anei.cadpage.parsers.general.XXAcadianAmbulanceParser;

/*
Acadian Ambulance, LA

*/

public class LAAcadianAmbulanceParser extends XXAcadianAmbulanceParser {

  public LAAcadianAmbulanceParser() {
    super("LA");
  }
  
  @Override
  public String getLocName() {
    return "Acadian Ambulance, LA";
  }
}
