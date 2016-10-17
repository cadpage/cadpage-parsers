package net.anei.cadpage.parsers.MS;

import net.anei.cadpage.parsers.general.XXAcadianAmbulanceParser;

/*
Acadian Ambulance, MS

*/

public class MSAcadianAmbulanceParser extends XXAcadianAmbulanceParser {

  public MSAcadianAmbulanceParser() {
    super("MS");
  }
  
  @Override
  public String getLocName() {
    return "Acadian Ambulance, MS";
  }
}
