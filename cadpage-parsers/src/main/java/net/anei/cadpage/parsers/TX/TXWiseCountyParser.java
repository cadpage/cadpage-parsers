package net.anei.cadpage.parsers.TX;

import net.anei.cadpage.parsers.GroupBestParser;

/**
 * Wise County, TX
 */

public class TXWiseCountyParser extends GroupBestParser {

  public TXWiseCountyParser() {
    super(new TXWiseCountyAParser(), new TXWiseCountyBParser());
  }

}
