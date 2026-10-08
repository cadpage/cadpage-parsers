package net.anei.cadpage.parsers.TX;

import net.anei.cadpage.parsers.GroupBestParser;

/**
 * Hopkins County, TX
 */
public class TXHopkinsCountyParser extends GroupBestParser {

  public TXHopkinsCountyParser() {
    super(new TXHopkinsCountyAParser(), new TXHopkinsCountyBParser());
  }
}
