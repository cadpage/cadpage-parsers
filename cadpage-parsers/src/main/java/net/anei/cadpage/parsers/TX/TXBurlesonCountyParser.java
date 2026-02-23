package net.anei.cadpage.parsers.TX;

import net.anei.cadpage.parsers.GroupBestParser;

/**
 * Burleson County, TX
 */
public class TXBurlesonCountyParser extends GroupBestParser {

  public TXBurlesonCountyParser() {
    super(new TXBurlesonCountyAParser(), new TXBurlesonCountyBParser());
  }
}
