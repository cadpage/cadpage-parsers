package net.anei.cadpage.parsers.TX;

import net.anei.cadpage.parsers.GroupBestParser;

/**
 * Gregg County, TX
 */
public class TXGreggCountyParser extends GroupBestParser {

  public TXGreggCountyParser() {
    super(new TXGreggCountyAParser(), new TXGreggCountyBParser());
  }
}
