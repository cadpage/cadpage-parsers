package net.anei.cadpage.parsers.TX;

import net.anei.cadpage.parsers.GroupBestParser;

/**
 * Hardin County, TX
 */
public class TXHardinCountyParser extends GroupBestParser {

  public TXHardinCountyParser() {
    super(new TXHardinCountyAParser(), new TXHardinCountyBParser());
  }
}
