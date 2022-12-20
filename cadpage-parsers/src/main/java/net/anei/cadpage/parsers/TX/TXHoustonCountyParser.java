package net.anei.cadpage.parsers.TX;

import net.anei.cadpage.parsers.GroupBestParser;

/**
 * Houston County, TX
 */
public class TXHoustonCountyParser extends GroupBestParser {

  public TXHoustonCountyParser() {
    super(new TXHoustonCountyAParser(), new TXHoustonCountyBParser());
  }
}
