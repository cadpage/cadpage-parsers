package net.anei.cadpage.parsers.TX;

import net.anei.cadpage.parsers.GroupBestParser;

/**
 * Leon County, TX
 */
public class TXLeonCountyParser extends GroupBestParser {
  
  public TXLeonCountyParser() {
    super(new TXLeonCountyAParser(), new TXLeonCountyBParser());
  }
}
