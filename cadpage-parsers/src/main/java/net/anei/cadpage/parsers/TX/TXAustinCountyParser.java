package net.anei.cadpage.parsers.TX;

import net.anei.cadpage.parsers.GroupBestParser;

/**
 * Austin County, TX
 */
public class TXAustinCountyParser extends GroupBestParser {

  public TXAustinCountyParser() {
    super(new TXAustinCountyAParser(), new TXAustinCountyBParser());
  }
}
