package net.anei.cadpage.parsers.TX;

import net.anei.cadpage.parsers.GroupBestParser;

/**
 * Montgomery County, TX
 */
public class TXMontgomeryCountyParser extends GroupBestParser {
  
  public TXMontgomeryCountyParser() {
    super(new TXMontgomeryCountyAParser(), new TXMontgomeryCountyBParser());
  }
}
