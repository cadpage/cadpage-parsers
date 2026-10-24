package net.anei.cadpage.parsers.TX;

import net.anei.cadpage.parsers.GroupBestParser;

/**
 * Bosque County, TX
 */
public class TXBosqueCountyParser extends GroupBestParser {

  public TXBosqueCountyParser() {
    super(new TXBosqueCountyAParser(), new TXBosqueCountyBParser());
  }
}
