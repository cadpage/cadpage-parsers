package net.anei.cadpage.parsers.TX;

import net.anei.cadpage.parsers.GroupBestParser;

/**
 * Tarrant County, TX
 */

public class TXTarrantCountyParser extends GroupBestParser {
  
  public TXTarrantCountyParser() {
    super(new TXTarrantCountyAParser(), new TXTarrantCountyBParser(),
        new TXTarrantCountyCParser());
  }
 
}
