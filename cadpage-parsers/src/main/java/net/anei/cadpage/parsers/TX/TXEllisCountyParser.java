package net.anei.cadpage.parsers.TX;

import net.anei.cadpage.parsers.GroupBestParser;

/**
 * Ellis County, TX
 */
public class TXEllisCountyParser extends GroupBestParser {
  
  public TXEllisCountyParser() {
    super(new TXEllisCountyAParser(), new TXEllisCountyBParser(), new TXEllisCountyCParser());
  }
}
