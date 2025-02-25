package net.anei.cadpage.parsers.TX;

import net.anei.cadpage.parsers.GroupBestParser;

/**
 * Brazos County, TX
 */
public class TXBrazosCountyParser extends GroupBestParser {

  public TXBrazosCountyParser() {
    super(new TXBrazosCountyAParser(), new TXBrazosCountyBParser());
  }
}
