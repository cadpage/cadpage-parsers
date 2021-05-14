package net.anei.cadpage.parsers.TX;

import net.anei.cadpage.parsers.GroupBestParser;

/**
 * Smith County, TX
 */

public class TXSmithCountyParser extends GroupBestParser {

  public TXSmithCountyParser() {
    super(new TXSmithCountyAParser(), new TXSmithCountyBParser());
  }

}
