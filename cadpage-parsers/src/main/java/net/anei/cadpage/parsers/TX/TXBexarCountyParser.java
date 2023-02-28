package net.anei.cadpage.parsers.TX;

import net.anei.cadpage.parsers.GroupBestParser;

/**
 * Bexar County, TX
 */
public class TXBexarCountyParser extends GroupBestParser {

  public TXBexarCountyParser() {
    super(new TXBexarCountyAParser(), 
          new TXBexarCountyBParser(),
          new TXBexarCountyCParser());
  }
}
