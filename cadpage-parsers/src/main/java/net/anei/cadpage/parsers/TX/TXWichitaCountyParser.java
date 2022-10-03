package net.anei.cadpage.parsers.TX;

import net.anei.cadpage.parsers.GroupBestParser;

/**
 * Wichita County, TX
 */

public class TXWichitaCountyParser extends GroupBestParser {

  public TXWichitaCountyParser() {
    super(new TXWichitaCountyAParser(), new TXWichitaCountyBParser());
  }

}
