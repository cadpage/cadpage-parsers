package net.anei.cadpage.parsers.TX;

import net.anei.cadpage.parsers.GroupBestParser;

/**
 * Kaufman County, TX
 */
public class TXKaufmanCountyParser extends GroupBestParser {
  
  public TXKaufmanCountyParser() {
    super(new TXKaufmanCountyAParser(), new TXKaufmanCountyBParser());
  }
}
