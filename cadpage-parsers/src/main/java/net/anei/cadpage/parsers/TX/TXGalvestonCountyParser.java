package net.anei.cadpage.parsers.TX;

import net.anei.cadpage.parsers.GroupBestParser;

/**
 * Galaveston County, TX
 */
public class TXGalvestonCountyParser extends GroupBestParser {

  public TXGalvestonCountyParser() {
    super(new TXGalvestonCountyAParser(), new TXGalvestonCountyBParser());
  }
}
