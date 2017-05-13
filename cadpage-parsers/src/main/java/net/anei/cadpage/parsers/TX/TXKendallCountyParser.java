package net.anei.cadpage.parsers.TX;

import net.anei.cadpage.parsers.GroupBestParser;

/**
 * Kendall County, TX
 */
public class TXKendallCountyParser extends GroupBestParser {
  
  public TXKendallCountyParser() {
    super(new TXKendallCountyAParser(), new TXKendallCountyBParser());
  }
}
