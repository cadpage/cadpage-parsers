package net.anei.cadpage.parsers.TX;

import net.anei.cadpage.parsers.GroupBestParser;

/**
 * Atoscosa County, TX
 */
public class TXBosqueCountyParser extends GroupBestParser {

  public TXBosqueCountyParser() {
    super(new TXBosqueCountyAParser(), new TXBosqueCountyBParser());
  }
}
