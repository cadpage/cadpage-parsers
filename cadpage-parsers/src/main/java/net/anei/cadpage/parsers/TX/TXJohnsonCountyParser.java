package net.anei.cadpage.parsers.TX;

import net.anei.cadpage.parsers.GroupBestParser;

/**
 * Johnson County, TX
 */
public class TXJohnsonCountyParser extends GroupBestParser {
  
  public TXJohnsonCountyParser() {
    super(new TXJohnsonCountyAParser(), new TXJohnsonCountyBParser());
  }
}
