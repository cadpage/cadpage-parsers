package net.anei.cadpage.parsers.TX;

import net.anei.cadpage.parsers.GroupBestParser;

/**
 * Hill County, TX
 */
public class TXHillCountyParser extends GroupBestParser {

  public TXHillCountyParser() {
    super(new TXHillCountyAParser(), new TXHillCountyBParser());
  }
}
