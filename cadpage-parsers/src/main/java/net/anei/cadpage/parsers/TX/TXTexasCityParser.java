package net.anei.cadpage.parsers.TX;

import net.anei.cadpage.parsers.GroupBestParser;

/**
 * Texis County, TX
 */

public class TXTexasCityParser extends GroupBestParser {

  public TXTexasCityParser() {
    super(new TXTexasCityAParser(), new TXHarrisCountyGParser());
  }
}
