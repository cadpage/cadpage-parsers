package net.anei.cadpage.parsers.TX;

import net.anei.cadpage.parsers.GroupBestParser;

/**
 * Collin County, TX
 */
public class TXCollinCountyParser extends GroupBestParser {
  
  public TXCollinCountyParser() {
    super(new TXCollinCountyAParser(), new TXCollinCountyBParser());
  }
}
