package net.anei.cadpage.parsers.TX;

import net.anei.cadpage.parsers.GroupBestParser;

/**
 * Lubbock County, TX
 */
public class TXLubbockCountyParser extends GroupBestParser {
  
  public TXLubbockCountyParser() {
    super(new TXLubbockCountyAParser(), 
          new TXLubbockCountyBParser(),
          new TXLubbockCountyCParser());
  }
}
