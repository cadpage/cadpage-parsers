package net.anei.cadpage.parsers.TX;

import net.anei.cadpage.parsers.GroupBestParser;

/**
 * Liberty County, TX
 */
public class TXLibertyCountyParser extends GroupBestParser {
  
  public TXLibertyCountyParser() {
    super(new TXLibertyCountyAParser(), new TXLibertyCountyBParser(), new TXLibertyCountyCParser());
  }
}
