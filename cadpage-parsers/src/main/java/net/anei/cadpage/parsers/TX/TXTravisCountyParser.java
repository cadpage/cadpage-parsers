package net.anei.cadpage.parsers.TX;

import net.anei.cadpage.parsers.GroupBestParser;

/**
 * Travis County, TX
 */

public class TXTravisCountyParser extends GroupBestParser {
  
  public TXTravisCountyParser() {
    super(new TXTravisCountyAParser(), new TXTravisCountyBParser());
  }
 
}
