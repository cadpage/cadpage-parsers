package net.anei.cadpage.parsers.TX;

import net.anei.cadpage.parsers.GroupBestParser;

/**
 * Caldwell County, TX
 */
public class TXCaldwellCountyParser extends GroupBestParser {

  public TXCaldwellCountyParser() {
    super(new TXCaldwellCountyAParser(), new TXCaldwellCountyBParser());
  }
}
