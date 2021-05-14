package net.anei.cadpage.parsers.TX;

import net.anei.cadpage.parsers.GroupBestParser;

/**
 * Cherokee County, TX
 */
public class TXCherokeeCountyParser extends GroupBestParser {

  public TXCherokeeCountyParser() {
    super(new TXCherokeeCountyAParser(), new TXCherokeeCountyBParser());
  }
}
