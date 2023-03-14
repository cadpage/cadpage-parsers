package net.anei.cadpage.parsers.TX;

import net.anei.cadpage.parsers.GroupBestParser;

/**
 * Grayson County, TX
 */
public class TXGraysonCountyParser extends GroupBestParser {

  public TXGraysonCountyParser() {
    super(new TXGraysonCountyAParser(), new TXGraysonCountyBParser());
  }
}
