package net.anei.cadpage.parsers.TX;

import net.anei.cadpage.parsers.GroupBestParser;

/**
 * Rockwall County, TX
 */
public class TXRockwallCountyParser extends GroupBestParser {
  
  public TXRockwallCountyParser() {
    super(new TXRockwallCountyAParser(), new TXRockwallCountyBParser());
  }
}
