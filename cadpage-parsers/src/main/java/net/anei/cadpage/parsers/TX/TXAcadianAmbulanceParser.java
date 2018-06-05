package net.anei.cadpage.parsers.TX;

import net.anei.cadpage.parsers.GroupBestParser;

/**
 * Acadian Ambulance, TX
 */
public class TXAcadianAmbulanceParser extends GroupBestParser {
  
  public TXAcadianAmbulanceParser() {
    super(new TXAcadianAmbulanceAParser(), new TXAcadianAmbulanceBParser());
  }
}
