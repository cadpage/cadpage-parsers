package net.anei.cadpage.parsers.TX;

import net.anei.cadpage.parsers.GroupBestParser;

/**
 * Dallas County, TX
 */
public class TXDallasCountyParser extends GroupBestParser {
  
  public TXDallasCountyParser() {
    super(new TXDallasCountyAParser(), new TXDallasCountyBParser(), new TXDallasCountyCParser());
  }
}
