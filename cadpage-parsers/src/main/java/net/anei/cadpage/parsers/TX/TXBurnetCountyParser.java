package net.anei.cadpage.parsers.TX;

import net.anei.cadpage.parsers.GroupBestParser;

/**
 * Burnet County, TX
 */
public class TXBurnetCountyParser extends GroupBestParser {

  public TXBurnetCountyParser() {
    super(new TXBurnetCountyAParser(), new TXBurnetCountyBParser());
  }
}
