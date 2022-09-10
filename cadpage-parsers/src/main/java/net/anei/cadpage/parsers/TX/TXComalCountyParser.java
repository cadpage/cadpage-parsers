package net.anei.cadpage.parsers.TX;

import net.anei.cadpage.parsers.GroupBestParser;

/**
 * Comal County, TX
 */
public class TXComalCountyParser extends GroupBestParser {

  public TXComalCountyParser() {
    super(new TXComalCountyAParser(), new TXComalCountyBParser());
  }
}
