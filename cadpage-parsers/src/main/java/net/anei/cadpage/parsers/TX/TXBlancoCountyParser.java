package net.anei.cadpage.parsers.TX;

import net.anei.cadpage.parsers.GroupBestParser;

/**
 * Blanco County, TX
 */
public class TXBlancoCountyParser extends GroupBestParser {

  public TXBlancoCountyParser() {
    super(new TXBlancoCountyAParser(), new TXBlancoCountyBParser());
  }
}
